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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = PluginData.id, name = PluginData.name, version = PluginData.version, description = PluginData.description, url = PluginData.url, authors = {PluginData.author1})
public class VirtualTool {
    private VirtualTool virtualtool;
    private Logger logger;
    private Path configpath;
    private Path dataDir;
    private PluginContainer pluginContainer;
    private Game game;
    //private File defaultConfig;
    private CommandManager cmdManager;

    //@DefaultConfig(sharedRoot = true)
    //private ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    public VirtualTool(@ConfigDir(sharedRoot = true) Path path, Logger logger, PluginContainer container) {
        this.dataDir = Sponge.getGame().getSavesDirectory().resolve(PluginData.id);
        this.pluginContainer = container;
        this.logger = LoggerFactory.getLogger(PluginData.name);
        this.configpath = path.resolve(PluginData.id);
        this.setVirtualTool(this);
    }

    public void setVirtualTool(VirtualTool virtualtool) {
        if (this.virtualtool == null) {
            this.virtualtool = virtualtool;
        }
    }

    public VirtualTool getNucleus() {
        return this.virtualtool;
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
        // this.logger.info("Plugin PreInitialization...");
       /* TODO Implement it XD */
        //this.logger.info("Plugin PreInitialized!");
    }

    @Listener
    public void onGameInit(@Nullable final GameInitializationEvent event) {
        this.logger.info("Plugin Initializing...");

        try {
            if (!Files.exists(this.configpath)) {
                Files.createDirectories(this.configpath);
            }
        } catch (final IOException ex) {
            this.logger.error("Error on creating root plugin directory: {}", ex);
        }

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
