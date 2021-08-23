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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.apache.commons.io.FileUtils;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

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

    public static boolean WriteFile(File file, String content, VirtualTool vt) {
        FileWriter filew = null;

        if (file.getParentFile().mkdirs()) {
            vt.getLogger().error("Created missing directories");
        }

        if (file.exists()) {
            try {
                filew = new FileWriter(file.toString(), false);
                filew.write(content);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (filew != null) {
                    try {
                        filew.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        } else {
            try {
                if (file.createNewFile()) {
                    if (!content.equals("lock")) {
                        vt.getLogger().info("Created new file: " + file.getName());
                    }
                    WriteFile(file, content, vt);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static String ContainerToBase64(DataContainer container) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DataFormats.NBT.writeTo(out, container);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
        try (ByteArrayInputStream in = new ByteArrayInputStream(base64)) {
            return DataFormats.NBT.readFrom(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeToJson(DataContainer container) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(writer)).build();
            loader.save(DataTranslators.CONFIGURATION_NODE.translate(container));
            return Base64.getEncoder().encodeToString(writer.toString().getBytes(Charsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static DataContainer deSerializeJson(String json) {
        try (StringReader reader = new StringReader(new String(Base64.getDecoder().decode(json), Charsets.UTF_8))) {
            return DataTranslators.CONFIGURATION_NODE.translate(HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(reader)).build().load()).getContainer();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(CommandSource src, VirtualTool vt) {
        final Server server = vt.getGame().getServer();
        return server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();
    }

    public static Player getPlayer(EntityPlayer entityHuman) {
        final Server server = VirtualTool.getInstance().getGame().getServer();
        return server.getPlayer(((Player) entityHuman).getUniqueId()).get();
    }

    public static EntityPlayerMP getPlayerE(CommandSource src, VirtualTool vt) {
        final Server server = vt.getGame().getServer();
        return (EntityPlayerMP) server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();
    }

    public static Optional<Player> getPlayer(Cause cause) {
        final Optional<Player> player = cause.first(Player.class);
        return player;
    }

    public static void savetojson(Path file, Settings jsonob, VirtualTool vt) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (jsonob == null) {
            WriteFile(file.toFile(), "{}", vt);
        } else {
            WriteFile(file.toFile(), gson.toJson(jsonob, jsonob.getClass()), vt);
        }
    }

    public static Settings loadfromjson(Path file, Settings defob, VirtualTool vt) {

        if (!Files.exists(file)) {
            try {
                savetojson(file, defob, vt);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        Settings out = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file.toString()));
            out = gson.fromJson(br, defob.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return out;
    }

    public static boolean lockbackpack(Player player, boolean log, VirtualTool vt) {
        if (WriteFile(Paths.get(vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".lock").toFile(), "lock", vt)) {
            if (log) {
                vt.getLogger().info(player.getName() + " backpack's locked");
            }
            return true;
        } else {
            if (log) {
                vt.getLogger().error(player.getName() + " backpack's is locked or failed getting locked");
            }
            return false;
        }
    }

    public static boolean unlockbackpack(Player player, boolean log, VirtualTool vt) {
        Path file = Paths.get(vt.getConfigPath() + File.separator + "backpacks" + File.separator + player.getUniqueId().toString() + ".lock");
        File f = new File(file.toString());
        if (f.isFile()) {
            if (f.exists()) {
                if (f.delete()) {
                    if (log) {
                        vt.getLogger().info("Removed lock: " + f.getName());
                    }
                    return true;
                } else {
                    if (log) {
                        vt.getLogger().error("Failed to remove lock: " + f.getName());
                    }
                    return false;
                }
            } else {
                if (log) {
                    vt.getLogger().error("Lock Doesn't exists failed to remove lock: " + f.getName());
                }
                return false;
            }
        } else {
            if (log) {
                vt.getLogger().error("Failed to remove lock: " + f.getName() + "!!");
            }
            return false;
        }
    }

    public static Boolean backpackchecklock(Player player, VirtualTool vt) throws CommandException {
        final Path file = Paths.get(vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".lock");
        return Files.exists(file);
    }

    public static void Backpack_unlockall(VirtualTool vt) {
        try {
            File[] files = new File(vt.getBackpackPath().toString()).listFiles((dir, name) -> name.endsWith(".lock"));
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.exists()) {
                            if (file.delete()) {
                                vt.getLogger().info("Removed lock: " + file.getName());
                            } else {
                                vt.getLogger().error("Failed to remove lock: " + file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ConvertBPS(VirtualTool vt) {
        try {
            final File[] files = new File(vt.getBackpackPath().toString()).listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.exists()) {
                            final Path newBPPath = Paths.get(file.toPath().toString().replace(".json", ".backpack"));
                            final File newBPFile = newBPPath.toFile();
                            if (newBPFile.exists()) {
                                if (file.delete()) {
                                    vt.getLogger().error("Removed old backpack file: " + file.getName());
                                } else {
                                    vt.getLogger().error("Failed to remove old backpack file: " + file.getName());
                                }
                            } else {
                                Gson gson = new Gson();

                                Type type = new TypeToken<Map<String, String>>() {
                                }.getType();

                                Map<String, String> models = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);

                                Map<String, String> models2 = new HashMap<>();

                                if (models != null) {
                                    for (Map.Entry<String, String> entry : models.entrySet()) {
                                        DataContainer dc = Tools.deSerializeJson(entry.getValue());
                                        ItemStack itemst = ItemStack.builder().fromContainer(dc).build();
                                        itemst.setRawData(dc);
                                        models2.put(entry.getKey(), Tools.ItemStackToBase64(itemst));
                                    }
                                }

                                if (SaveConvertedBackpack(newBPFile, models2, vt)) {
                                    if (file.delete()) {
                                        vt.getLogger().error("Removed old backpack file: " + file.getName());
                                    } else {
                                        vt.getLogger().error("Failed to remove old backpack file: " + file.getName());
                                    }
                                } else {
                                    vt.getLogger().error("Failed to convert backpack file: " + file.getPath());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ConvertBP(Player player, VirtualTool vt) {
        final Path filePath = Paths.get(vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".backpack");
        final File file = filePath.toFile();
        try {
            if (file != null) {
                if (file.isFile()) {
                    if (file.exists()) {
                        final Path newBPPath = Paths.get(file.toPath().toString().replace(".json", ".backpack"));
                        final File newBPFile = newBPPath.toFile();
                        if (newBPFile.exists()) {
                            if (file.delete()) {
                                vt.getLogger().error("Removed old backpack file: " + file.getName());
                            } else {
                                vt.getLogger().error("Failed to remove old backpack file: " + file.getName());
                            }
                        } else {
                            Gson gson = new Gson();

                            Type type = new TypeToken<Map<String, String>>() {
                            }.getType();

                            Map<String, String> models = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);

                            Map<String, String> models2 = new HashMap<>();

                            if (models != null) {
                                for (Map.Entry<String, String> entry : models.entrySet()) {
                                    DataContainer dc = Tools.deSerializeJson(entry.getValue());
                                    ItemStack itemst = ItemStack.builder().fromContainer(dc).build();
                                    itemst.setRawData(dc);
                                    models2.put(entry.getKey(), Tools.ItemStackToBase64(itemst));
                                }
                            }

                            if (SaveConvertedBackpack(newBPFile, models2, vt)) {
                                if (file.delete()) {
                                    vt.getLogger().error("Removed old backpack file: " + file.getName());
                                } else {
                                    vt.getLogger().error("Failed to remove old backpack file: " + file.getName());
                                }
                            } else {
                                vt.getLogger().error("Failed to convert backpack file: " + file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean SaveConvertedBackpack(File file, Map<String, String> items, VirtualTool vt) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (items == null || items.isEmpty()) {
            return WriteFile(file, "{}", vt);
        } else {
            return WriteFile(file, gson.toJson(items), vt);
        }
    }

    public static void MakeNewBP(Player player, VirtualTool vt) {
        final Path filePath = Paths.get(vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".backpack");
        final File file = filePath.toFile();

        final Path oldfilePath = Paths.get(vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".json");
        final File oldfile = oldfilePath.toFile();

        if (oldfile.exists()) {
            Tools.ConvertBP(player, vt);
        } else {
            if (!file.exists()) {
                if (!Tools.WriteFile(file, "{}", vt)) {
                    vt.getLogger().error("Failed to create backpack file on player join for " + player.getName());
                }
            }
        }
    }

    public static SlotPos IndxToSP(Integer indx) {
        switch (indx) {
            case 0:
                return SlotPos.of(0, 0);
            case 1:
                return SlotPos.of(1, 0);
            case 2:
                return SlotPos.of(2, 0);
            case 3:
                return SlotPos.of(3, 0);
            case 4:
                return SlotPos.of(4, 0);
            case 5:
                return SlotPos.of(5, 0);
            case 6:
                return SlotPos.of(6, 0);
            case 7:
                return SlotPos.of(7, 0);
            case 8:
                return SlotPos.of(8, 0);

            case 9:
                return SlotPos.of(0, 1);
            case 10:
                return SlotPos.of(1, 1);
            case 11:
                return SlotPos.of(2, 1);
            case 12:
                return SlotPos.of(3, 1);
            case 13:
                return SlotPos.of(4, 1);
            case 14:
                return SlotPos.of(5, 1);
            case 15:
                return SlotPos.of(6, 1);
            case 16:
                return SlotPos.of(7, 1);
            case 17:
                return SlotPos.of(8, 1);

            case 18:
                return SlotPos.of(0, 2);
            case 19:
                return SlotPos.of(1, 2);
            case 20:
                return SlotPos.of(2, 2);
            case 21:
                return SlotPos.of(3, 2);
            case 22:
                return SlotPos.of(4, 2);
            case 23:
                return SlotPos.of(5, 2);
            case 24:
                return SlotPos.of(6, 2);
            case 25:
                return SlotPos.of(7, 2);
            case 26:
                return SlotPos.of(8, 2);

            case 27:
                return SlotPos.of(0, 3);
            case 28:
                return SlotPos.of(1, 3);
            case 29:
                return SlotPos.of(2, 3);
            case 30:
                return SlotPos.of(3, 3);
            case 31:
                return SlotPos.of(4, 3);
            case 32:
                return SlotPos.of(5, 3);
            case 33:
                return SlotPos.of(6, 3);
            case 34:
                return SlotPos.of(7, 3);
            case 35:
                return SlotPos.of(8, 3);

            case 36:
                return SlotPos.of(0, 4);
            case 37:
                return SlotPos.of(1, 4);
            case 38:
                return SlotPos.of(2, 4);
            case 39:
                return SlotPos.of(3, 4);
            case 40:
                return SlotPos.of(4, 4);
            case 41:
                return SlotPos.of(5, 4);
            case 42:
                return SlotPos.of(6, 4);
            case 43:
                return SlotPos.of(7, 4);
            case 44:
                return SlotPos.of(8, 4);

            case 45:
                return SlotPos.of(0, 5);
            case 46:
                return SlotPos.of(1, 5);
            case 47:
                return SlotPos.of(2, 5);
            case 48:
                return SlotPos.of(3, 5);
            case 49:
                return SlotPos.of(4, 5);
            case 50:
                return SlotPos.of(5, 5);
            case 51:
                return SlotPos.of(6, 5);
            case 52:
                return SlotPos.of(7, 5);
            case 53:
                return SlotPos.of(8, 5);

            default:
                return null;
        }


    }

}
