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


import io.github.poqdavid.virtualtool.Utils.Invs;
import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;


/**
 * Created by David on 10/23/2016.
 */
public class CommandManager {
    public static CommandSpec enderchestCmd;
    public static CommandSpec anvilCmd;
    public static CommandSpec workbenchCmd;
    public static CommandSpec enchantingtableCmd;
    public static CommandSpec backpackCmd;
    public static CommandSpec backpacklockCmd;
    private final Game game;
    private final VirtualTool vt;
    private final Invs inv;

    public CommandManager(Game game, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        this.inv = new Invs(game, vt);
        registerCommands();
    }

    public void registerCommands() {
        CommandSpec helpCmd = CommandSpec.builder()
                .description(Text.of("/vt help"))
                .executor(new HelpCMD(this.game, this.inv, this.vt))
                .build();

        enderchestCmd = CommandSpec.builder()
                .description(EnderChestCMD.getDescription())
                .executor(new EnderChestCMD(this.game, this.inv, this.vt))
                .build();

        anvilCmd = CommandSpec.builder()
                .description(AnvilCMD.getDescription())
                .executor(new AnvilCMD(this.game, this.inv, this.vt))
                .build();

        workbenchCmd = CommandSpec.builder()
                .description(WorkbenchCMD.getDescription())
                .executor(new WorkbenchCMD(this.game, this.inv, this.vt))
                .build();

        enchantingtableCmd = CommandSpec.builder()
                .description(EnchantingTableCMD.getDescription())
                .executor(new EnchantingTableCMD(this.game, this.inv, this.vt))
                .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("power"))))
                .build();

        backpackCmd = CommandSpec.builder()
                .description(BackpackCMD.getDescription())
                .executor(new BackpackCMD(this.game, this.inv, this.vt))
                .arguments(GenericArguments.optional(GenericArguments.playerOrSource(Text.of("player"))), GenericArguments.optional(GenericArguments.integer(Text.of("size"))), GenericArguments.flags().flag("m").buildWith(GenericArguments.none()))
                .build();

        backpacklockCmd = CommandSpec.builder()
                .description(BackpackLockCMD.getDescription())
                .executor(new BackpackLockCMD(this.game, this.inv, this.vt))
                .arguments(GenericArguments.player(Text.of("player")), GenericArguments.flags().flag("l").flag("u").buildWith(GenericArguments.none()))
                .build();

        CommandSpec vtCommand = CommandSpec.builder()
                .description(MainCMD.getDescription())
                .executor(new MainCMD(this.game, this.inv, this.vt))
                .child(helpCmd, HelpCMD.getAlias())
                .build();

        //--Main---
        game.getCommandManager().register(vt, vtCommand, MainCMD.getAlias());
        //--Tools--
        game.getCommandManager().register(vt, backpacklockCmd, BackpackLockCMD.getAlias());
        if (vt.getSettings().getCommands().isEnderchestEnabled()) {
            game.getCommandManager().register(vt, enderchestCmd, EnderChestCMD.getAlias());
        }
        if (vt.getSettings().getCommands().isAnvilEnabled()) {
            game.getCommandManager().register(vt, anvilCmd, AnvilCMD.getAlias());
        }
        if (vt.getSettings().getCommands().isWorkbenchEnabled()) {
            game.getCommandManager().register(vt, workbenchCmd, WorkbenchCMD.getAlias());
        }
        if (vt.getSettings().getCommands().isEnchantingtableEnabled()) {
            game.getCommandManager().register(vt, enchantingtableCmd, EnchantingTableCMD.getAlias());
        }
        if (vt.getSettings().getCommands().isBackpackEnabled()) {
            game.getCommandManager().register(vt, backpackCmd, BackpackCMD.getAlias());
        }
    }

}
