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
import io.github.poqdavid.virtualtool.Utils.Invs;
import io.github.poqdavid.virtualtool.VirtualTool;
import org.spongepowered.api.Game;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by David on 10/23/2016.
 */
public class HelpCMD implements CommandExecutor {
    private Game game;
    private VirtualTool vt;
    private Invs inv;

    public HelpCMD(Game game, Invs inv, VirtualTool vt) {
        this.game = game;
        this.vt = vt;
        this.inv = inv;
    }

    public static String[] getAlias() {
        return new String[]{"help", "?"};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src.hasPermission(VTPermissions.COMMAND_HELP)) {
            PaginationService paginationService = vt.getInstance().getGame().getServiceManager().provide(PaginationService.class).get();
            PaginationList.Builder builder = paginationService.builder();
            URL url1 = null;
            try {
                url1 = new URL("https://github.com/poqdavid/VirtualTool/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URL url2 = null;
            try {
                url2 = new URL("http://pocketpixels.net/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Text h1 = Text.builder("Author: ").color(TextColors.BLUE).style(TextStyles.BOLD).build();
            Text h2 = Text.builder("POQDavid").color(TextColors.GRAY).style(TextStyles.BOLD).onHover(TextActions.showText(Text.of(url1.toString()))).onClick(TextActions.openUrl(url1)).build();
            Text h3 = Text.builder(",").color(TextColors.BLUE).style(TextStyles.BOLD).build();
            Text h4 = Text.builder(" and Special thanks to ").color(TextColors.GRAY).style(TextStyles.BOLD).build();
            Text h5 = Text.builder("Pocket").color(TextColors.YELLOW).style(TextStyles.BOLD).onHover(TextActions.showText(Text.of(url2.toString()))).onClick(TextActions.openUrl(url2)).build();
            Text h6 = Text.builder("Pixels").color(TextColors.BLUE).style(TextStyles.BOLD).onHover(TextActions.showText(Text.of(url2.toString()))).onClick(TextActions.openUrl(url2)).build();
            builder.title(Text.of(TextColors.DARK_AQUA, "VirtualTool - V" + vt.getVersion()))
                    .header(Text.of(h1, h2, h3, h4, h5, h6))
                    .contents(
                            Text.of(TextColors.BLUE, TextStyles.ITALIC, ""),
                            Text.of(TextColors.GREEN, TextStyles.BOLD, "Commands"),
                            Text.of(TextColors.GOLD, "- ", TextColors.GRAY, "/anvil"),
                            Text.of(TextColors.GOLD, "- ", TextColors.GRAY, "/enderchest, /ec"),
                            Text.of(TextColors.GOLD, "- ", TextColors.GRAY, "/enchantingtable, /et"),
                            Text.of(TextColors.GOLD, "- ", TextColors.GRAY, "/workbench, /wb"),
                            Text.of(TextColors.GOLD, "- ", TextColors.GRAY, "/backpack, /bp")
                    )
                    .padding(Text.of("="))
                    .sendTo(src);
        } else {
            throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
        }

        return CommandResult.success();
    }

}


