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

    public CommandManager(Game game, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        registerCommands();
    }


    public void registerCommands() {
        CommandSpec helpCmd = CommandSpec.builder()
                .description(Text.of("/vt help"))
                .executor(new HelpCMD())
                .permission("VirtualTool.command.help")
                .build();
        CommandSpec enderchestCmd = CommandSpec.builder()
                .description(Text.of("/ec or /vt ec"))
                .executor(new EnderChestCMD())
                .permission("VirtualTool.command.enderchest")
                .build();
        CommandSpec anvilCmd = CommandSpec.builder()
                .description(Text.of("/anvil or /vt av"))
                .executor(new AnvilCMD())
                .permission("VirtualTool.command.anvil")
                .build();
        CommandSpec workbenchCmd = CommandSpec.builder()
                .description(Text.of("/workbench or /wb"))
                .executor(new WorkbenchCMD())
                .permission("VirtualTool.command.workbench")
                .build();
        CommandSpec enchantingtableCmd = CommandSpec.builder()
                .description(Text.of("/enchantingtable or /et"))
                .executor(new EnchantingTableCMD())
                .permission("VirtualTool.command.enchantingtable")
                .build();
        CommandSpec vtCommand = CommandSpec.builder()
                .description(Text.of("/vt"))
                .permission("VirtualTool.command.main")
                .child(helpCmd, "help")
                .child(helpCmd, "")
                .child(enderchestCmd, "enderchest", "ec")
                .child(anvilCmd, "anvil", "av")
                .child(workbenchCmd, "workbench", "wb")
                .child(enchantingtableCmd, "enchantingtable", "et")
                .build();


        game.getCommandManager().register(vt, vtCommand, "virtualtool", "vt");
        game.getCommandManager().register(vt, enderchestCmd, "enderchest", "ec");
        game.getCommandManager().register(vt, anvilCmd, "anvil");
        game.getCommandManager().register(vt, workbenchCmd, "workbench", "wb");
        game.getCommandManager().register(vt, enchantingtableCmd, "enchantingtable", "et");
    }

}
