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


import io.github.poqdavid.virtualtool.Utils.Inventory;
import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;


/**
 * Created by David on 10/23/2016.
 */
public class CommandManager {
    private Game game;
    private VirtualTool vt;
    private Inventory inv;

    public CommandManager(Game game, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        this.inv = new Inventory(game, vt);
        registerCommands();
    }


    public void registerCommands() {
        CommandSpec helpCmd = CommandSpec.builder()
                .description(Text.of("/vt help"))
                .executor(new HelpCMD(this.game, this.inv, this.vt))
                .permission(HelpCMD.getPermission())
                .build();

        CommandSpec enderchestCmd = CommandSpec.builder()
                .description(EnderChestCMD.getDescription())
                .executor(new EnderChestCMD(this.game, this.inv, this.vt))
                .permission(EnderChestCMD.getPermission())
                .build();

        CommandSpec anvilCmd = CommandSpec.builder()
                .description(AnvilCMD.getDescription())
                .executor(new AnvilCMD(this.game, this.inv, this.vt))
                .permission(AnvilCMD.getPermission())
                .build();

        CommandSpec workbenchCmd = CommandSpec.builder()
                .description(WorkbenchCMD.getDescription())
                .executor(new WorkbenchCMD(this.game, this.inv, this.vt))
                .permission(WorkbenchCMD.getPermission())
                .build();

        CommandSpec enchantingtableCmd = CommandSpec.builder()
                .description(EnchantingTableCMD.getDescription())
                .executor(new EnchantingTableCMD(this.game, this.inv, this.vt))
                .permission(EnchantingTableCMD.getPermission())
                .build();

        CommandSpec vtCommand = CommandSpec.builder()
                .description(MainCMD.getDescription())
                .permission(MainCMD.getPermission())
                .executor(new HelpCMD(this.game, this.inv, this.vt))
                .child(helpCmd, HelpCMD.getAlias())
                .child(enderchestCmd, EnderChestCMD.getAlias())
                .child(anvilCmd, AnvilCMD.getAlias())
                .child(workbenchCmd, WorkbenchCMD.getAlias())
                .child(enchantingtableCmd, EnchantingTableCMD.getAlias())
                .build();

        game.getCommandManager().register(vt, vtCommand, MainCMD.getAlias());
        game.getCommandManager().register(vt, enderchestCmd, EnderChestCMD.getAlias());
        game.getCommandManager().register(vt, anvilCmd, AnvilCMD.getAlias());
        game.getCommandManager().register(vt, workbenchCmd, WorkbenchCMD.getAlias());
        game.getCommandManager().register(vt, enchantingtableCmd, EnchantingTableCMD.getAlias());
    }

}
