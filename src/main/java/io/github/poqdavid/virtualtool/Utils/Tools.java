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
import io.github.poqdavid.virtualtool.VirtualTool;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Optional;

/**
 * Created by David on 2/12/2017.
 */
public class Tools {

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

}
