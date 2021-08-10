package com.justinb.ramwal.items;

import com.justinb.ramwal.blocks.Glitch;
import com.justinb.ramwal.init.SoundInit;
import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.network.SpreadPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.logging.Logger;

public class LemonItem extends Item {
    public static final int SPAWN_CHANCE = 10;

    public LemonItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        BlockPos pos = entityLiving.getPosition();

        if (worldIn.isRemote() && Glitch.canSpread(worldIn.getBlockState(pos.down())))
            if (new Random().nextInt(100/SPAWN_CHANCE) == 0)
                NetworkHandler.sendToServer(new SpreadPacket("ramwal:glitch", pos.down()));

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
