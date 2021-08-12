package com.justinb.ramwal.blocks;

import com.justinb.ramwal.init.BlockInit;
import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.network.SpreadPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class Glitch extends Block {
    public final static int SPREAD_RATE = 20;
    public final static double PORTAL_CHANCE = 0.2;
    public final static int LIFE = 20;

    public static final IntegerProperty AGE = IntegerProperty.create("g_age", 0, LIFE);

    public Glitch(Properties properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(AGE, 0));
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 0);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isRemote) {
            ArrayList<BlockPos> poss = new ArrayList<>();
            Random rands = new Random();

            for (int i = 0; i < 6; i++) {
                BlockPos posss = pos.offset(Direction.byIndex(i));

                if (canSpread(worldIn.getBlockState(posss)))
                    poss.add(posss);
            }

            if (poss.size() == 0 || state.get(AGE).compareTo(LIFE) == 0) {
                if (rands.nextInt((int) (10000 / (100 * PORTAL_CHANCE))) == 0)
                    worldIn.setBlockState(pos, BlockInit.LEMONPORTAL.get().getDefaultState(), 3);
                else
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

                return;
            }

            for (BlockPos blockPos : poss) {
                if (rands.nextInt(SPREAD_RATE) == 0)
                    worldIn.setBlockState(blockPos, state.cycleValue(AGE), 3);
            }

            worldIn.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), (rand.nextInt(3) + 1) * 3);
        }
    }

    public static boolean canSpread(BlockState s) {
        return (s.isSolid() &&
                !s.getMaterial().equals(Material.WOOL) &&
                !s.getBlock().equals(Blocks.BEDROCK) &&
                !s.getBlock().equals(Blocks.END_PORTAL_FRAME) &&
                !s.getBlock().equals(Blocks.OBSIDIAN) &&
                !(s.getBlock() instanceof Glitch) ||
                s.getMaterial().equals(Material.GLASS) ||
                s.getMaterial().equals(Material.LEAVES));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(AGE, 0);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
