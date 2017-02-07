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
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.nio.file.Path;

/**
 * Created by David on 10/23/2016.
 */
public class HelpCMD implements CommandExecutor {
    private Path path;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        PaginationService paginationService = VirtualTool.getInstance().getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder builder = paginationService.builder();
        builder.title(Text.of(TextColors.DARK_AQUA, "VirtualTool - V" + VirtualTool.getVersion()))
                .contents(
                        Text.of(TextColors.BLUE, TextStyles.UNDERLINE, "GitHub - https://github.com/POQDavid/VirtualTool/"),
                        Text.of(TextColors.BLUE, TextStyles.UNDERLINE, "Post - https://forums.spongepowered.org/"),
                        Text.of(TextColors.BLUE, TextStyles.ITALIC, ""),
                        Text.of(TextColors.BLUE, TextStyles.BOLD, "Commands"),
                        Text.of(TextColors.BLUE, TextStyles.NONE, "- /anvil"),
                        Text.of(TextColors.BLUE, TextStyles.NONE, "- /enderchest or /ec"),
                        Text.of(TextColors.BLUE, TextStyles.NONE, "- /enchantingtable or /et"),
                        Text.of(TextColors.BLUE, TextStyles.NONE, "- /workbench or /wb"),
                        Text.of(TextColors.BLUE, TextStyles.NONE, "- /vt enderchest"),
                        Text.of(TextColors.BLUE, TextStyles.NONE, "- /vt anvil")
                )
                .header(Text.of(TextColors.BLUE, TextStyles.BOLD, "Author - POQDavid"))
                .padding(Text.of("="))
                .sendTo(src);
        return CommandResult.success();
    }
}


