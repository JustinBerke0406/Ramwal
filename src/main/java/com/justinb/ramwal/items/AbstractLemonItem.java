package com.justinb.ramwal.items;

import com.justinb.ramwal.blocks.Glitch;
import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.network.SpreadPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public abstract class AbstractLemonItem extends Item {
    private int color;

    public AbstractLemonItem(Properties properties, int color) {
        super(properties);

        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        return (tintIndex == 0) ? ((AbstractLemonItem) stack.getItem()).getColor() : 0xFFFFFF;
    }

    @Override
    public abstract ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving);
}
