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
import org.apache.commons.io.FileUtils;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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
        this.vt = vt;
        this.player_args = player_args;
        this.player_cmd_src = player_cmd_src;
        this.size = size;

        this.itemspos = new ArrayList<>(54);

        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                itemspos.add(new SlotPos(x, y));
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
                        this.savebackpack(this.player_args, this.loadStacks(this.player_args, this.inventory), this.vt);
                    } else {
                        event.setCancelled(true);
                    }
                })
                .listener(InteractInventoryEvent.Close.class, event -> {
                    if (saveit) {
                        Tools.unlockbackpack(this.player_args, false, this.vt);
                    }
                })
                .build(VirtualTool.getInstance());
        this.loadbackpack(this.player_args, this.vt);
    }

    private void savebackpack(Player player, Map<String, String> items, VirtualTool vt) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        Path file = Paths.get(this.vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".backpack");
        if (items == null || items.isEmpty()) {
            Tools.WriteFile(file.toFile(), "{}", this.vt);
        } else {
            Tools.WriteFile(file.toFile(), gson.toJson(items), this.vt);
        }
    }

    private Map<String, String> loadStacks(Player player, Inventory backpack) {
        final Map<String, String> items = new HashMap<>();
        for (SlotPos slotp : this.itemspos) {
            if (backpack.query(slotp).size() > 0) {
                if (!backpack.query(slotp).peek().get().getItem().equals(ItemTypes.NONE)) {
                    try {
                        if (backpack.query(slotp).peek().isPresent()) {
                            items.put(slotp.getX() + "," + slotp.getY(), Tools.ItemStackToBase64(backpack.query(slotp).peek().get()));
                        }
                    } catch (Exception e) {
                        VirtualTool.getInstance().getLogger().error("Failed to load a stack data from inventory for this user: " + player.getName() + " SlotPos: " + slotp.getX() + "X," + slotp.getY() + "Y");
                        e.printStackTrace();
                    }
                }
            }
        }
        return items;
    }

    private void loadbackpack(Player player, VirtualTool vt) {
        Path filePath = Paths.get(vt.getBackpackPath() + File.separator + player.getUniqueId().toString() + ".backpack");
        File file = filePath.toFile();

        if (!file.exists()) {
            Tools.WriteFile(file, "{}", vt);
        }


        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Map<String, String> models = null;
        try {
            models = gson.fromJson(FileUtils.readFileToString(file, Charsets.UTF_8), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (models != null) {
            for (Map.Entry<String, String> entry : models.entrySet()) {
                if (entry != null) {
                    if (entry.getValue() != null) {
                        final SlotPos sp = SlotPos.of(Integer.parseInt(entry.getKey().split(",")[0].toString()), Integer.parseInt(entry.getKey().split(",")[1].toString()));
                        try {
                            final ItemStack itemst = Tools.Base64ToItemStack(entry.getValue());
                            this.inventory.query(sp).set(itemst);
                        } catch (Exception ex) {
                            VirtualTool.getInstance().getLogger().error("Failed to load a stack data from file for this user: " + player.getName() + " SlotPos: " + sp.getX() + "X," + sp.getY() + "Y");
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Inventory getbackpack() {
        return this.inventory;
    }

}
