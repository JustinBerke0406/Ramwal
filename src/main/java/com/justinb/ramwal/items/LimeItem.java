package com.justinb.ramwal.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LimeItem extends Item {
    public LimeItem(Properties properties) {
        super(properties);
    }
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            //TODO come up with effect
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
