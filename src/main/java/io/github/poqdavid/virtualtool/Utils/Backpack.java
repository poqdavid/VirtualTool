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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 3/21/2017.
 */
public class Backpack {

    public List<SlotPos> itemspos;
    private Player player_args;
    private Player player_cmd_src;
    private Inventory inventory;
    private Map<String, String> items;
    private VirtualTool vt;
    private Text backpacktitle_text;
    private String backpacktitle_str;
    private int size;

    public Backpack(Player player_args, Player player_cmd_src, int size, Boolean saveit, VirtualTool vt) {
        //if (saveit) {
        //    this.lockbackpack(player_args);
        // }

        this.vt = vt;
        this.player_args = player_args;
        this.player_cmd_src = player_cmd_src;
        this.size = size;

        this.itemspos = new ArrayList<>(54);

        for (int x = 0; x <= 5; x++) {
            for (int y = 0; y <= 8; y++) {
                itemspos.add(new SlotPos(y, x));
            }
        }

        this.items = new HashMap<>();

        if (!player_cmd_src.getUniqueId().equals(this.player_args.getUniqueId())) {
            this.backpacktitle_text = Text.of(this.player_args.getName() + "'s " + "Backpack");
            this.backpacktitle_str = this.player_args.getName() + "'s " + "Backpack";
        } else {
            this.backpacktitle_text = Text.of("Backpack");
            this.backpacktitle_str = "Backpack";
        }

        this.inventory = Inventory.builder()
                .of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of(this.backpacktitle_text)))
                .property("inventorydimension", InventoryDimension.of(9, this.size))
                .listener(ClickInventoryEvent.class, (ClickInventoryEvent event) -> {
                    if (saveit) {

                        this.savebackpack(this.player_args, this.loadStacks(this.inventory));
                    } else {
                        event.setCancelled(true);
                    }
                })
                .listener(InteractInventoryEvent.Close.class, event -> {
                    if (saveit) {
                        this.unlockbackpack(this.player_args);
                    }
                })
                .build(this.vt.getInstance());
        this.loadbackpack(this.player_args);
    }

    private void savebackpack(Player player, Map<String, String> items) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(items);
        if (!Files.exists(this.vt.getConfigPath())) {
            try {
                Files.createDirectories(this.vt.getConfigPath());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        Path file = Paths.get(this.vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".json");
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

    private Map<String, String> loadStacks(Inventory backpack) {
        final Map<String, String> items = new HashMap<>();
        for (SlotPos slotp : this.itemspos) {
            if (backpack.query(slotp).size() > 0) {
                if (!backpack.query(slotp).peek().get().getItem().equals(ItemTypes.NONE)) {
                    try {
                        if (backpack.query(slotp).peek().isPresent()) {
                            items.put(slotp.getX() + "," + slotp.getY(), Tools.serializeToJson(backpack.query(slotp).peek().get().toContainer()));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // this.loadStacks();
                    }
                }
            }

        }

        return items;
    }

    private void loadbackpack(Player player) {
        Path file = Paths.get(this.vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".json");
        if (!Files.exists(file)) {
            try {
                Files.createFile(file);
                try (FileWriter filew = new FileWriter(file.toString())) {
                    filew.write("{}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
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
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> models = gson.fromJson(br, type);

        for (Map.Entry<String, String> entry : models.entrySet()) {
            DataContainer dc  = Tools.deSerializeJson(entry.getValue());
            ItemStack itemst = ItemStack.builder().fromContainer(dc).build();
            itemst.setRawData(dc);
            this.inventory.query(SlotPos.of(Integer.parseInt(entry.getKey().split(",")[0].toString()), Integer.parseInt(entry.getKey().split(",")[1].toString()))).set(itemst);
        }
    }

    private void unlockbackpack(Player player) {

        if (!Files.exists(this.vt.getConfigPath())) {
            try {
                Files.createDirectories(this.vt.getConfigPath());
            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        Path file = Paths.get(this.vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".lock");
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

    public Inventory getbackpack() {
        return this.inventory;
    }

}
