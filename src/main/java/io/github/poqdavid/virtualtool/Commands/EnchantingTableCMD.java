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
import io.github.poqdavid.virtualtool.Utils.Containers.VirtualEnchantingTable;
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
import org.spongepowered.api.text.Text;

/**
 * Created by David on 2/7/2017.
 */
public class EnchantingTableCMD implements CommandExecutor {
    private final Game game;
    private final VirtualTool vt;
    private final Invs inv;

    public EnchantingTableCMD(Game game, Invs inv, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        this.inv = inv;
    }

    public static Text getDescription() {
        return Text.of("/enchantingtable, /et");
    }

    public static String[] getAlias() {
        return new String[]{"enchantingtable", "et"};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player) {
            if (src.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE)) {
                final Integer maxPower = this.getEnchantingTablePower(Tools.getPlayer(src, this.vt));
                final Integer et_lvl = args.<Integer>getOne("power").orElse(maxPower);
                if (et_lvl >= maxPower) {
                    final VirtualEnchantingTable VET = new VirtualEnchantingTable(Tools.getPlayerE(src, this.vt), maxPower.floatValue());

                    return inv.Open(src, VET, "minecraft:enchanting_table", "Enchant");
                } else {
                    final VirtualEnchantingTable VET = new VirtualEnchantingTable(Tools.getPlayerE(src, this.vt), et_lvl.floatValue());
                    return inv.Open(src, VET, "minecraft:enchanting_table", "Enchant");
                }
            } else {
                throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
            }
        } else {
            throw new CommandException(Text.of("You can't use this command if you are not a player!"));
        }

    }

    public Integer getEnchantingTablePower(Player player) {
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_15))
            return 15;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_14))
            return 14;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_13))
            return 13;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_12))
            return 12;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_11))
            return 11;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_10))
            return 10;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_9))
            return 9;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_8))
            return 8;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_7))
            return 7;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_6))
            return 6;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_5))
            return 5;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_4))
            return 4;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_3))
            return 3;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_2))
            return 2;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_1))
            return 1;
        if (player.hasPermission(VTPermissions.COMMAND_ENCHANTINGTABLE_POWER_0))
            return 0;
        return 15;
    }
}
