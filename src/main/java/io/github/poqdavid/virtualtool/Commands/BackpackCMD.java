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
package io.github.poqdavid.virtualtool.Commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.poqdavid.virtualtool.Permission.VTPermissions;
import io.github.poqdavid.virtualtool.Utils.Invs;
import io.github.poqdavid.virtualtool.Utils.Plugin;
import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.Humanoid;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
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
 * Created by David on 3/10/2017.
 */
public class BackpackCMD implements CommandExecutor {
    public List<SlotPos> itemspos;
    public Map<String, String> items;

    private Game game;
    private VirtualTool vt;
    private Invs inv;
    private Inventory backpack;

    public BackpackCMD(Game game, Invs inv, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        this.inv = inv;
        this.itemspos = new ArrayList<>();

        itemspos.add(new SlotPos(0, 0));
        itemspos.add(new SlotPos(1, 0));
        itemspos.add(new SlotPos(2, 0));
        itemspos.add(new SlotPos(3, 0));
        itemspos.add(new SlotPos(4, 0));
        itemspos.add(new SlotPos(5, 0));
        itemspos.add(new SlotPos(6, 0));
        itemspos.add(new SlotPos(7, 0));
        itemspos.add(new SlotPos(8, 0));

        itemspos.add(new SlotPos(0, 1));
        itemspos.add(new SlotPos(1, 1));
        itemspos.add(new SlotPos(2, 1));
        itemspos.add(new SlotPos(3, 1));
        itemspos.add(new SlotPos(4, 1));
        itemspos.add(new SlotPos(5, 1));
        itemspos.add(new SlotPos(6, 1));
        itemspos.add(new SlotPos(7, 1));
        itemspos.add(new SlotPos(8, 1));

        itemspos.add(new SlotPos(0, 2));
        itemspos.add(new SlotPos(1, 2));
        itemspos.add(new SlotPos(2, 2));
        itemspos.add(new SlotPos(3, 2));
        itemspos.add(new SlotPos(4, 2));
        itemspos.add(new SlotPos(5, 2));
        itemspos.add(new SlotPos(6, 2));
        itemspos.add(new SlotPos(7, 2));
        itemspos.add(new SlotPos(8, 2));

        itemspos.add(new SlotPos(0, 3));
        itemspos.add(new SlotPos(1, 3));
        itemspos.add(new SlotPos(2, 3));
        itemspos.add(new SlotPos(3, 3));
        itemspos.add(new SlotPos(4, 3));
        itemspos.add(new SlotPos(5, 3));
        itemspos.add(new SlotPos(6, 3));
        itemspos.add(new SlotPos(7, 3));
        itemspos.add(new SlotPos(8, 3));

        itemspos.add(new SlotPos(0, 4));
        itemspos.add(new SlotPos(1, 4));
        itemspos.add(new SlotPos(2, 4));
        itemspos.add(new SlotPos(3, 4));
        itemspos.add(new SlotPos(4, 4));
        itemspos.add(new SlotPos(5, 4));
        itemspos.add(new SlotPos(6, 4));
        itemspos.add(new SlotPos(7, 4));
        itemspos.add(new SlotPos(8, 4));

        itemspos.add(new SlotPos(0, 5));
        itemspos.add(new SlotPos(1, 5));
        itemspos.add(new SlotPos(2, 5));
        itemspos.add(new SlotPos(3, 5));
        itemspos.add(new SlotPos(4, 5));
        itemspos.add(new SlotPos(5, 5));
        itemspos.add(new SlotPos(6, 5));
        itemspos.add(new SlotPos(7, 5));
        itemspos.add(new SlotPos(8, 5));
    }

    public static Text getDescription() {
        return Text.of("/backpack, /bp");
    }

    public static String[] getAlias() {
        return new String[]{"backpack", "bp"};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Server server = vt.getGame().getServer();
        Humanoid humanoid = server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();

        Player player = (Player) humanoid;
        Player playerbyarg = args.<Player>getOne("player").orElse(player);

        if (!player.getUniqueId().equals(playerbyarg.getUniqueId())) {
            if (args.hasAny("m")) {
                this.openBackpackOther(playerbyarg, player, true);
            } else {
                this.openBackpackOther(playerbyarg, player, false);
            }
        } else {
            this.openBackpackOwn(playerbyarg);
        }

        if (src instanceof Player) {

        } else {
            throw new CommandException(Text.of("You can't use this command if you are not a player!"));
        }
        return CommandResult.success();
    }

    private void openBackpackOther(Player player, Player playerSrc, Boolean modify) throws CommandException {
        if (playerSrc.hasPermission(VTPermissions.COMMAND_BACKPACK_ADMIN_READ)) {
            this.backpackcheck(player);
            if (modify) {
                if (playerSrc.hasPermission(VTPermissions.COMMAND_BACKPACK_ADMIN_MODIFY)) {
                    this.backpackchecklock(player);
                    this.openBackpackother(player, playerSrc, modify);
                } else {
                    throw new CommandPermissionException(Text.of("You don't have permission to modify other backpacks."));
                }
            } else {
                this.openBackpackother(player, playerSrc, modify);
            }
        } else {
            throw new CommandPermissionException(Text.of("You don't have permission to view other backpacks."));
        }
    }

    private void openBackpackother(Player player, Player playerSrc, Boolean modify) {
        if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_SIX)) {
            this.openBackpack(6, player, playerSrc, modify);
        } else {
            if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_FIVE)) {
                this.openBackpack(5, player, playerSrc, modify);
            } else {
                if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_FOUR)) {
                    this.openBackpack(4, player, playerSrc, modify);
                } else {
                    if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_THREE)) {
                        this.openBackpack(3, player, playerSrc, modify);
                    } else {
                        if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_TWO)) {
                            this.openBackpack(2, player, playerSrc, modify);
                        } else {
                            if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_ONE)) {
                                this.openBackpack(1, player, playerSrc, modify);
                            }
                        }
                    }
                }
            }
        }
    }

    private void openBackpackOwn(Player player) throws CommandException {

        this.backpackchecklock(player);

        if (player.hasPermission(VTPermissions.COMMAND_BACKPACK)) {
            if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_SIX)) {
                this.openBackpack(6, player, player, true);
            } else {
                if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_FIVE)) {
                    this.openBackpack(5, player, player, true);
                } else {
                    if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_FOUR)) {
                        this.openBackpack(4, player, player, true);
                    } else {
                        if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_THREE)) {
                            this.openBackpack(3, player, player, true);
                        } else {
                            if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_TWO)) {
                                this.openBackpack(2, player, player, true);
                            } else {
                                if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_ONE)) {
                                    this.openBackpack(1, player, player, true);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
        }
    }

    private void openBackpack(int size, Player player, Player playerSrc, boolean saveit) {
        this.lockbackpack(player);
        items = new HashMap<>();
        Text backpacktitle = Text.of("Backpack");
        if (!playerSrc.getUniqueId().equals(player.getUniqueId())) {
            backpacktitle = Text.of(player.getName() + "'s " + "Backpack");
        }

        this.backpack = Inventory.builder()
                .of(InventoryArchetypes.DOUBLE_CHEST)
                .property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of(backpacktitle)))
                .property("inventorydimension", InventoryDimension.of(9, size))
                .listener(ClickInventoryEvent.class, event -> {
                    if (saveit) {
                        this.loadStacks();
                        this.savebackpack(player, items);
                        items.clear();
                    } else {
                        event.setCancelled(true);
                    }
                })
                .listener(InteractInventoryEvent.Close.class, event -> {
                    this.unlockbackpack(player);
                })
                .build(this.vt.getInstance());
        this.loadbackpack(player);
        playerSrc.openInventory(this.backpack, Cause.of(NamedCause.of("plugin", this.vt.getInstance()), NamedCause.source(playerSrc)));
    }

    private void savebackpack(Player player, Map<String, String> items) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
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

    private void loadStacks() {

        for (SlotPos slotp : this.itemspos) {
            if (this.getbackpack().query(slotp).size() > 0) {
                if (!this.getbackpack().query(slotp).peek().get().getItem().equals(ItemTypes.NONE)) {
                    try {
                        if (this.getbackpack().query(slotp).peek().isPresent()) {
                            items.put(slotp.getX() + "," + slotp.getY(), Plugin.serializeToJson(this.getbackpack().query(slotp).peek().get().toContainer()));
                        }

                    } catch (Exception e) {
                        //e.printStackTrace();
                        // this.loadStacks();
                    }

                }

            }

        }

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

            this.getbackpack().query(SlotPos.of(Integer.parseInt(entry.getKey().split(",")[0].toString()), Integer.parseInt(entry.getKey().split(",")[1].toString()))).set(ItemStack.builder().fromContainer(Plugin.deSerializeJson(entry.getValue())).build());
        }

    }

    private void backpackchecklock(Player player) throws CommandException {

        Path file = Paths.get(this.vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".lock");
        if (Files.exists(file)) {
            throw new CommandPermissionException(Text.of("Sorry currently your backpack is locked."));
        }
    }

    private void backpackcheck(Player player) throws CommandException {

        Path file = Paths.get(this.vt.getConfigPath() + "/backpacks/" + player.getUniqueId().toString() + ".json");
        if (!Files.exists(file)) {
            throw new CommandPermissionException(Text.of("Sorry there is no backpack data for " + player.getName()));
        }
    }

    private void lockbackpack(Player player) {

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
        return this.backpack;
    }
}