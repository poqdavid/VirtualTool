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
package io.github.poqdavid.virtualtool.Utils.Containers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by David on 3/26/2017.
 */
public class VirtualEnchantingTable extends ContainerEnchantment {
    private final Random rand;
    private final World worldPointer;
    private final BlockPos position;
    private final EntityPlayer entityHumans;
    private final float power;

    public VirtualEnchantingTable(EntityPlayer entityHuman, float power) {
        super(entityHuman.inventory, entityHuman.world, entityHuman.getPosition());
        this.worldPointer = entityHuman.world;
        this.position = entityHuman.getPosition();
        this.rand = new Random();
        this.entityHumans = entityHuman;
        this.power = power;
    }

    private List<EnchantmentData> getEnchantmentList(ItemStack stack, int p_178148_2_, int p_178148_3_) {
        this.rand.setSeed((long) (this.xpSeed + p_178148_2_));
        List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_, false);

        if (stack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(this.rand.nextInt(list.size()));
        }

        return list;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if (inventoryIn == this.tableInventory) {
            ItemStack itemstack = inventoryIn.getStackInSlot(0);

            if (itemstack != null && itemstack.isItemEnchantable()) {
                if (!this.worldPointer.isRemote) {
                    int l = 0;

                    this.rand.setSeed((long) this.xpSeed);

                    for (int i1 = 0; i1 < 3; ++i1) {
                        this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, (int) this.power, itemstack);
                        this.enchantClue[i1] = -1;
                        this.worldClue[i1] = -1;

                        if (this.enchantLevels[i1] < i1 + 1) {
                            this.enchantLevels[i1] = 0;
                        }
                    }

                    for (int j1 = 0; j1 < 3; ++j1) {
                        if (this.enchantLevels[j1] > 0) {
                            List<EnchantmentData> list = this.getEnchantmentList(itemstack, j1, this.enchantLevels[j1]);

                            if (list != null && !list.isEmpty()) {
                                EnchantmentData enchantmentdata = (EnchantmentData) list.get(this.rand.nextInt(list.size()));
                                this.enchantClue[j1] = Enchantment.getEnchantmentID(enchantmentdata.enchantmentobj);
                                this.worldClue[j1] = enchantmentdata.enchantmentLevel;
                            }
                        }
                    }

                    this.detectAndSendChanges();
                }
            } else {
                for (int i = 0; i < 3; ++i) {
                    this.enchantLevels[i] = 0;
                    this.enchantClue[i] = -1;
                    this.worldClue[i] = -1;
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}