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
package io.github.poqdavid.virtualtool;

import io.github.poqdavid.virtualtool.Commands.CommandManager;
import io.github.poqdavid.virtualtool.Permission.VTPermissions;
import io.github.poqdavid.virtualtool.Utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(id = PluginData.id, name = PluginData.name, version = PluginData.version, description = PluginData.description, url = PluginData.url, authors = {PluginData.author1})
public class VirtualTool {
    public PermissionService permservice;
    public PermissionDescription.Builder permdescbuilder;
    private VirtualTool virtualtool;
    private Logger logger;
    private Path configdirpath;
    private Path configfullpath;
    private Path dataDir;
    private Path backpackDir;
    private PluginContainer pluginContainer;
    private Game game;
    private Settings settings;
    private CommandManager cmdManager;

    @Inject
    public VirtualTool(@ConfigDir(sharedRoot = true) Path path, Logger logger, PluginContainer container) {
        this.dataDir = Sponge.getGame().getSavesDirectory().resolve(PluginData.id);
        this.pluginContainer = container;
        this.logger = LoggerFactory.getLogger(PluginData.name);
        this.configdirpath = path.resolve(PluginData.id);
        this.backpackDir = Paths.get(this.configdirpath + "/backpacks");
        this.configfullpath = Paths.get(this.getConfigPath() + "/config.json");
        this.settings = new Settings();
        this.setVirtualTool(this);
    }

    public void setVirtualTool(VirtualTool virtualtool) {
        if (this.virtualtool == null) {
            this.virtualtool = virtualtool;
        }
    }

    @Nonnull
    public Path getConfigPath() {
        return this.configdirpath;
    }

    @Nonnull
    public VirtualTool getInstance() {
        return this.virtualtool;
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    @Nonnull
    public String getVersion() {
        return PluginData.version;
    }

    @Nonnull
    public Settings getSettings() {
        return this.settings;
    }

    @Nonnull
    public Logger getLogger() {
        return logger;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Inject
    public void setGame(Game game) {
        this.game = game;
    }


    @Listener
    public void onGamePreInit(@Nullable final GamePreInitializationEvent event) {
        this.logger.info("Plugin Initializing...");
    }

    @Listener
    public void onGameInit(@Nullable final GameInitializationEvent event) {
        if (Sponge.getServiceManager().getRegistration(PermissionService.class).get().getPlugin().getId().equalsIgnoreCase("sponge")) {
            this.logger.error("Unable to initialize plugin. VirtualTool requires a PermissionService like  LuckPerms, PEX, PermissionsManager.");
            return;
        }
        this.permservice = this.game.getServiceManager().getRegistration(PermissionService.class).get().getProvider();
        this.permdescbuilder = this.permservice.newDescriptionBuilder(this.getPluginContainer()).orElse(null);
        if (this.permdescbuilder != null) {

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_MAIN)
                    .description(Text.of("Allows the use of /vt, /virtualtool"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_HELP)
                    .description(Text.of("Allows the use of /vt help"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_ANVIL)
                    .description(Text.of("Allows the use of /anvil, /av"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK)
                    .description(Text.of("Allows the use of /backpack, /bp"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_ENCHANTINGTABLE)
                    .description(Text.of("Allows the use of /enchantingtable or /et"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_ENDERCHEST)
                    .description(Text.of("Allows the use of /enderchest, /ec"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_WORKBENCH)
                    .description(Text.of("Allows the use of /workbench or /wb"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_ADMIN_READ)
                    .description(Text.of("Allows to read content of other backpacks"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_ADMIN_MODIFY)
                    .description(Text.of("Allows to modify other backpacks"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_SIZE_ONE)
                    .description(Text.of("Sets users backpack size to 1 row"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_SIZE_TWO)
                    .description(Text.of("Sets users backpack size to 2 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_SIZE_THREE)
                    .description(Text.of("Sets users backpack size to 3 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_SIZE_FOUR)
                    .description(Text.of("Sets users backpack size to 4 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_SIZE_FIVE)
                    .description(Text.of("Sets users backpack size to 5 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            this.permdescbuilder
                    .id(VTPermissions.COMMAND_BACKPACK_SIZE_SIX)
                    .description(Text.of("Sets users backpack size to 6 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
        }

        try {
            if (!Files.exists(this.configdirpath)) {
                Files.createDirectories(this.configdirpath);
            }
        } catch (final IOException ex) {
            this.logger.error("Error on creating root plugin directory: {}", ex);
        }

        try {
            if (!Files.exists(this.backpackDir)) {
                Files.createDirectories(this.backpackDir);
            }
        } catch (final IOException ex) {
            this.logger.error("Error on creating backpack directory: {}", ex);
        }

        this.settings.Load(this.configfullpath);
        this.logger.info("Plugin Initialized successfully!");
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        this.logger.info("Loading...");
        this.cmdManager = new CommandManager(game, this);
        this.logger.info("Loaded!");
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        //this.logger.info("Game Server  Started...");
    }

    @Listener
    public void onGameReload(@Nullable final GameReloadEvent event) {
        //this.logger.info("Reloading...");
        //this.logger.info("Reloaded!");
    }
}
