package com.justinb.ramwal.blocks;

import com.justinb.ramwal.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class LemonPortalBlock extends Block {
    public LemonPortalBlock(Properties properties) {
        super(properties);
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !isValidSpawn(currentPos, (World) worldIn) ? BlockInit.LEMONPORTAL.get().getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (worldIn instanceof ServerWorld && !entityIn.isPassenger() && !entityIn.isBeingRidden() && entityIn.canChangeDimension()) {
            RegistryKey<World> dim = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("ramwal:lemondimension"));
            RegistryKey<World> registrykey = worldIn.getDimensionKey() == dim ? World.OVERWORLD : dim;
            ServerWorld serverworld = ((ServerWorld)worldIn).getServer().getWorld(registrykey);

            if (serverworld == null) {
                return;
            }

            entityIn.changeDimension(serverworld);
        }
    }

    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    public static boolean isValidSpawn(BlockPos pos, World worldIn) {
        for (int i = 2; i <= 5; i++)
            if (!(worldIn.getBlockState(pos.offset(Direction.byIndex(i))).getBlock() == BlockInit.GLITCH.get())) return false;
        return true;
    }
}
