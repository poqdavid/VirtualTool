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
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;

/**
 * Created by David on 2/7/2017.
 */
public class Invs {
    private Game game;
    private VirtualTool vt;

    public Invs(Game game, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
    }

    public CommandResult Open(CommandSource src, CommandContext args, InventoryArchetype invArch, Text inventorytitle) {

        return this.Open(src, args, org.spongepowered.api.item.inventory.Inventory.builder().of(invArch).property(InventoryTitle.PROPERTY_NAME, new InventoryTitle(inventorytitle)).build(vt));

    }

    public CommandResult Open(CommandSource src, CommandContext args, String invArch) {
        if (invArch == "enderchest") {
            Player player = Plugin.getPlayer(src, vt);
            return this.Open(src, args, player.getEnderChestInventory());
        }
        return CommandResult.empty();
    }

    public CommandResult Open(CommandSource src, CommandContext args, InventoryArchetype invArch) {
        return this.Open(src, args, org.spongepowered.api.item.inventory.Inventory.builder().of(invArch).build(vt));
    }

    public CommandResult Open(CommandSource src, CommandContext args, org.spongepowered.api.item.inventory.Inventory i) {
        Player player = Plugin.getPlayer(src, vt);
        if (src.getCommandSource().isPresent() && src.getCommandSource().get() instanceof Player) {

            player.openInventory(i, Cause.of(NamedCause.of("plugin", vt), NamedCause.source(player)));
            return CommandResult.success();
        }
        return CommandResult.empty();
    }


}
