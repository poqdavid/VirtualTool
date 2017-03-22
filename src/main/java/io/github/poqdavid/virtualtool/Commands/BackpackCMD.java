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

import io.github.poqdavid.virtualtool.Permission.VTPermissions;
import io.github.poqdavid.virtualtool.Utils.Backpack;
import io.github.poqdavid.virtualtool.Utils.Invs;
import io.github.poqdavid.virtualtool.Utils.Tools;
import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by David on 3/10/2017.
 */
public class BackpackCMD implements CommandExecutor {

    private Game game;
    private VirtualTool vt;
    private Invs inv;

    public BackpackCMD(Game game, Invs inv, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        this.inv = inv;

    }

    public static Text getDescription() {
        return Text.of("/backpack, /bp");
    }

    public static String[] getAlias() {
        return new String[]{"backpack", "bp"};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            final Player player_cmd_src = Tools.getPlayer(src, vt);
            final Player player_args = args.<Player>getOne("player").orElse(null);

            if (player_cmd_src.hasPermission(VTPermissions.COMMAND_BACKPACK)) {
                if (player_args != null) {
                    if (!player_cmd_src.getUniqueId().equals(player_args.getUniqueId())) {
                        if (player_cmd_src.hasPermission(VTPermissions.COMMAND_BACKPACK_ADMIN_READ)) {
                            if (args.hasAny("m")) {
                                if (player_cmd_src.hasPermission(VTPermissions.COMMAND_BACKPACK_ADMIN_MODIFY)) {
                                    this.backpackcheck(player_args);
                                    this.backpackchecklock(player_args);
                                    this.lockbackpack(player_args);
                                    final Backpack backpack = new Backpack(player_args, player_cmd_src, this.getBackpackSize(player_args), true, this.vt);
                                    player_cmd_src.openInventory(backpack.getbackpack(), Cause.of(NamedCause.of("plugin", this.vt.getInstance()), NamedCause.source(player_cmd_src)));
                                } else {
                                    throw new CommandPermissionException(Text.of("You don't have permission to modify other backpacks."));
                                }
                            } else {
                                this.backpackcheck(player_args);
                                final Backpack backpack = new Backpack(player_args, player_cmd_src, this.getBackpackSize(player_args), false, vt);
                                player_cmd_src.openInventory(backpack.getbackpack(), Cause.of(NamedCause.of("plugin", this.vt.getInstance()), NamedCause.source(player_cmd_src)));
                            }
                        } else {
                            throw new CommandPermissionException(Text.of("You don't have permission to view other backpacks."));
                        }
                    } else {
                        this.backpackchecklock(player_cmd_src);
                        this.lockbackpack(player_cmd_src);
                        final Backpack backpack = new Backpack(player_cmd_src, player_cmd_src, this.getBackpackSize(player_cmd_src), true, vt);
                        player_cmd_src.openInventory(backpack.getbackpack(), Cause.of(NamedCause.of("plugin", this.vt.getInstance()), NamedCause.source(player_cmd_src)));
                    }
                } else {
                    this.backpackchecklock(player_cmd_src);
                    this.lockbackpack(player_cmd_src);
                    final Backpack backpack = new Backpack(player_cmd_src, player_cmd_src, this.getBackpackSize(player_cmd_src), true, vt);
                    player_cmd_src.openInventory(backpack.getbackpack(), Cause.of(NamedCause.of("plugin", this.vt.getInstance()), NamedCause.source(player_cmd_src)));
                }
            } else {
                throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
            }
        } else {
            throw new CommandException(Text.of("You can't use this command if you are not a player!"));
        }
        return CommandResult.success();
    }

    public int getBackpackSize(Player player) {
        int tempsize = 0;
        if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_SIX)) {
            tempsize = 6;
        } else {
            if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_FIVE)) {
                tempsize = 5;
            } else {
                if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_FOUR)) {
                    tempsize = 4;
                } else {
                    if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_THREE)) {
                        tempsize = 3;
                    } else {
                        if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_TWO)) {
                            tempsize = 2;
                        } else {
                            if (player.hasPermission(VTPermissions.COMMAND_BACKPACK_SIZE_ONE)) {
                                tempsize = 1;
                            }
                        }
                    }
                }
            }
        }
        return tempsize;
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
        } else {
            String content = null;
            try {
                content = new String(Files.readAllBytes(file), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (content == "{}") {
                throw new CommandPermissionException(Text.of("Sorry there is no backpack data for " + player.getName()));
            }
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
}