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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * Created by David on 3/14/2017.
 */
public class Settings implements Serializable {

    private final static long serialVersionUID = 4280887333225310204L;
    @SerializedName("commands")
    @Expose
    @Valid
    private Commands commands;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
        this.setCommands(new Commands(true, true, true, true, true));
    }

    /**
     * @param commands
     */
    public Settings(Commands commands) {
        super();
        this.commands = commands;
    }


    public Settings(Path file) {
        this.Load(file);
    }

    public void Load(Path file) {
        try {
            Settings sets = Plugin.loadfromjson(file, new Settings());
            this.setCommands(sets.getCommands());
        } catch (Exception e) {
            this.Save(file);
            this.Load(file);
        }
    }

    public void Save(Path file) {
        Plugin.savetojson(file, this);
    }

    public Commands getCommands() {
        return commands;
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    public Settings withCommands(Commands commands) {
        this.commands = commands;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(commands).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Settings) == false) {
            return false;
        }
        Settings rhs = ((Settings) other);
        return new EqualsBuilder().append(commands, rhs.commands).isEquals();
    }

    public class Commands implements Serializable {

        private final static long serialVersionUID = 6630490888395483134L;
        @SerializedName("enable_enderchest")
        @Expose
        private boolean enableEnderchest;
        @SerializedName("enable_anvil")
        @Expose
        private boolean enableAnvil;
        @SerializedName("enable_workbench")
        @Expose
        private boolean enableWorkbench;
        @SerializedName("enable_enchantingtable")
        @Expose
        private boolean enableEnchantingtable;
        @SerializedName("enable_backpack")
        @Expose
        private boolean enableBackpack;

        /**
         * No args constructor for use in serialization
         */
        public Commands() {
        }

        /**
         * @param enableWorkbench
         * @param enableEnchantingtable
         * @param enableBackpack
         * @param enableAnvil
         * @param enableEnderchest
         */
        public Commands(boolean enableEnderchest, boolean enableAnvil, boolean enableWorkbench, boolean enableEnchantingtable, boolean enableBackpack) {
            super();
            this.enableEnderchest = enableEnderchest;
            this.enableAnvil = enableAnvil;
            this.enableWorkbench = enableWorkbench;
            this.enableEnchantingtable = enableEnchantingtable;
            this.enableBackpack = enableBackpack;
        }

        public boolean isEnderchestEnabled() {
            return enableEnderchest;
        }

        public void setEnderchest(boolean enableEnderchest) {
            this.enableEnderchest = enableEnderchest;
        }

        public Commands withEnderchest(boolean enableEnderchest) {
            this.enableEnderchest = enableEnderchest;
            return this;
        }

        public boolean isAnvilEnabled() {
            return enableAnvil;
        }

        public void setAnvil(boolean enableAnvil) {
            this.enableAnvil = enableAnvil;
        }

        public Commands withAnvil(boolean enableAnvil) {
            this.enableAnvil = enableAnvil;
            return this;
        }

        public boolean isWorkbenchEnabled() {
            return enableWorkbench;
        }

        public void setWorkbench(boolean enableWorkbench) {
            this.enableWorkbench = enableWorkbench;
        }

        public Commands withWorkbench(boolean enableWorkbench) {
            this.enableWorkbench = enableWorkbench;
            return this;
        }

        public boolean isEnchantingtableEnabled() {
            return enableEnchantingtable;
        }

        public void setEnchantingtable(boolean enableEnchantingtable) {
            this.enableEnchantingtable = enableEnchantingtable;
        }

        public Commands withEnchantingtable(boolean enableEnchantingtable) {
            this.enableEnchantingtable = enableEnchantingtable;
            return this;
        }

        public boolean isBackpackEnabled() {
            return enableBackpack;
        }

        public void setBackpack(boolean enableBackpack) {
            this.enableBackpack = enableBackpack;
        }

        public Commands withBackpack(boolean enableBackpack) {
            this.enableBackpack = enableBackpack;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(enableEnderchest).append(enableAnvil).append(enableWorkbench).append(enableEnchantingtable).append(enableBackpack).toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if ((other instanceof Commands) == false) {
                return false;
            }
            Commands rhs = ((Commands) other);
            return new EqualsBuilder().append(enableEnderchest, rhs.enableEnderchest).append(enableAnvil, rhs.enableAnvil).append(enableWorkbench, rhs.enableWorkbench).append(enableEnchantingtable, rhs.enableEnchantingtable).append(enableBackpack, rhs.enableBackpack).isEquals();
        }

    }
}