/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.proxy.event;

import com.buuz135.industrial.item.MeatFeederItem;
import com.buuz135.industrial.proxy.ItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MeatFeederTickHandler {

    public static boolean meatTick(ItemStack stack, EntityPlayer player) {
        int filledAmount = ((MeatFeederItem) stack.getItem()).getFilledAmount(stack);
        if (filledAmount >= 400 && (player.getFoodStats().getSaturationLevel() < 20 || player.getFoodStats().getFoodLevel() < 20)) {
            ((MeatFeederItem) stack.getItem()).drain(stack, 400);
            player.getFoodStats().addStats(1, 1);
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent event) {
        if (!event.getEntityLiving().getEntityWorld().isRemote && event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (player.getFoodStats().needFood() || player.getFoodStats().getSaturationLevel() < 10) {
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (stack.getItem().equals(ItemRegistry.meatFeederItem)) {
                        meatTick(stack, (EntityPlayer) event.getEntityLiving());
                    }
                }
            }
        }
    }
}
