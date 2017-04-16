/*
 * This file is part of VirtualTool, licensed under the MIT License (MIT).
 *
 * Copyright (c) POQDavid <http://poqdavid.github.io/VirtualTool>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.poqdavid.virtualtool.Utils;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.poqdavid.virtualtool.VirtualTool;
import net.minecraft.entity.player.EntityPlayerMP;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.apache.commons.io.FileUtils;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by David on 2/12/2017.
 */
public class Tools {

    public static String ContainerToBase64(DataContainer container) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            DataFormats.NBT.writeTo(out, container);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String ItemStackToBase64(ItemStack itemStack) {
        return ContainerToBase64(itemStack.toContainer());
    }

    public static DataContainer Base64ToContainer(String base64) {
        return Base64ToContainer(Base64.getDecoder().decode(base64));
    }

    public static ItemStack Base64ToItemStack(String base64) {
        return ItemStack.builder().fromContainer(Base64ToContainer(base64)).build();
    }

    public static ItemStack Base64ToItemStack(byte[] base64) {
        return ItemStack.builder().fromContainer(Base64ToContainer(base64)).build();
    }

    public static DataContainer Base64ToContainer(byte[] base64) {
        DataContainer container = null;

        try (ByteArrayInputStream in = new ByteArrayInputStream(base64)) {
            container = DataFormats.NBT.readFrom(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return container;
    }

    public static String serializeToJson(DataContainer container) throws IOException {
        StringWriter writer = new StringWriter();
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(writer)).build();
        loader.save(DataTranslators.CONFIGURATION_NODE.translate(container));

        return Base64.getEncoder().encodeToString(writer.toString().getBytes(Charsets.UTF_8));
    }

    public static DataContainer deSerializeJson(String json) {
        StringReader reader = new StringReader(new String(Base64.getDecoder().decode(json), Charsets.UTF_8));
        DataView view = null;
        try {
            view = DataTranslators.CONFIGURATION_NODE.translate(HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(reader)).build().load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view.getContainer();
    }

    public static Player getPlayer(CommandSource src, VirtualTool vt) {
        final Server server = vt.getGame().getServer();
        return server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();
    }

    public static EntityPlayerMP getPlayerE(CommandSource src, VirtualTool vt) {
        final Server server = vt.getGame().getServer();
        return (EntityPlayerMP) server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();
    }

    public static Optional<Player> getPlayer(Cause cause) {
        final Optional<Player> player = cause.first(Player.class);
        return player;
    }

    public static void savetojson(Path file, Settings jsonob) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(jsonob, jsonob.getClass());
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileWriter filew = new FileWriter(file.toString())) {
            if (jsonob == null) {
                filew.write("{}");
            } else {
                filew.write(json.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings loadfromjson(Path file, Settings defob) {
        if (!Files.exists(file)) {
            try {
                savetojson(file, defob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file.toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return gson.fromJson(br, defob.getClass());
    }

    public static void lockbackpack(Player player, VirtualTool vt) {
        if (!Files.exists(vt.getConfigPath())) {
            try {
                Files.createDirectories(vt.getConfigPath());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        Path file = Paths.get(vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".lock");
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unlockbackpack(Player player, VirtualTool vt) {

        if (!Files.exists(vt.getConfigPath())) {
            try {
                Files.createDirectories(vt.getConfigPath());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        Path file = Paths.get(vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".lock");
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            Files.delete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean backpackchecklock(Player player, VirtualTool vt) throws CommandException {
        final Path file = Paths.get(vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".lock");
        return Files.exists(file);
    }

    public static void unlockallbackpacks(VirtualTool vt) {
        try {
            File[] files = new File(vt.getConfigPath() + "/backpacks").listFiles((dir, name) -> name.endsWith(".lock"));
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {

                    if (file.delete()) {
                        vt.getLogger().error("Removed lock: " + file.getName());
                    } else {
                        vt.getLogger().error("Failed to remove lock: " + file.getName());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void ConvertBPS(VirtualTool vt) {
        try {
            File[] files = new File(vt.getConfigPath() + "/backpacks").listFiles((dir, name) -> name.endsWith(".json"));
            assert files != null;
            for (File file : files) {
                if (file.isFile()) {
                    Gson gson = new Gson();

                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();

                    Map<String, String> models = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);

                    Map<String, String> models2 = new HashMap<>();
                    for (Map.Entry<String, String> entry : models.entrySet()) {
                        DataContainer dc = Tools.deSerializeJson(entry.getValue());
                        ItemStack itemst = ItemStack.builder().fromContainer(dc).build();
                        itemst.setRawData(dc);
                        models2.put(entry.getKey(), Tools.ItemStackToBase64(itemst));
                    }


                    SaveConvertedBackpack(Paths.get(file.toPath().toString().replace(".json", ".backpack")), models2);

                    if (file.delete()) {
                        vt.getLogger().error("Removed old backpack file: " + file.getName());
                    } else {
                        vt.getLogger().error("Failed to remove old backpack file: " + file.getName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void SaveConvertedBackpack(Path file, Map<String, String> items) {

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(items);

        try (FileWriter filew = new FileWriter(file.toString())) {
            if (items.isEmpty()) {
                filew.write("{}");
            } else {
                filew.write(json.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
