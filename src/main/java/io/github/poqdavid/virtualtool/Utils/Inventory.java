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

import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;

/**
 * Created by David on 2/7/2017.
 */
public class Inventory {
    public static CommandResult Open(CommandSource src, CommandContext args, String open) {
        if (src.getCommandSource().isPresent() && src.getCommandSource().get() instanceof Player) {
            final Player player = (Player) src.getCommandSource().get();


            if (open == "enderchest") {
                player.openInventory(player.getEnderChestInventory(), Cause.of(NamedCause.of("plugin", VirtualTool.getInstance()), NamedCause.source(player)));
                return CommandResult.success();
            }

            if (open == "anvil") {
                org.spongepowered.api.item.inventory.Inventory i = org.spongepowered.api.item.inventory.Inventory.builder().of(InventoryArchetypes.ANVIL).property(InventoryTitle.PROPERTY_NAME, new InventoryTitle(Text.of("Virtual Anvil"))).build(VirtualTool.getInstance());
                player.openInventory(i, Cause.of(NamedCause.of("plugin", VirtualTool.getInstance()), NamedCause.source(player)));
                return CommandResult.success();
            }

            if (open == "workbench") {
                org.spongepowered.api.item.inventory.Inventory i = org.spongepowered.api.item.inventory.Inventory.builder().of(InventoryArchetypes.WORKBENCH).build(VirtualTool.getInstance());
                player.openInventory(i, Cause.of(NamedCause.of("plugin", VirtualTool.getInstance()), NamedCause.source(player)));
                return CommandResult.success();
            }

            if (open == "enchantingtable") {
                org.spongepowered.api.item.inventory.Inventory i = org.spongepowered.api.item.inventory.Inventory.builder().of(InventoryArchetypes.ENCHANTING_TABLE).build(VirtualTool.getInstance());
                player.openInventory(i, Cause.of(NamedCause.of("plugin", VirtualTool.getInstance()), NamedCause.source(player)));
                return CommandResult.success();
            }
        }
        return CommandResult.empty();
    }
}
