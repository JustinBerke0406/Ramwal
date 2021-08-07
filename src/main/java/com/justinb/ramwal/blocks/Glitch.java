package com.justinb.ramwal.blocks;

import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.network.SpreadPacket;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class Glitch extends Block {
    public final static int SPREAD_RATE = 10;

    public Glitch(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        BlockPos[] poss = new BlockPos[6];

        for (int i = 0; i < 6; i++) {
            poss[i] = pos.offset(Direction.byIndex(i));
        }

        for (BlockPos blockPos : poss) {
            BlockState s = worldIn.getBlockState(blockPos);

            if (canSpread(s)) {
                Random rands = new Random();
                if (rands.nextInt(SPREAD_RATE) == 0) {
                    NetworkHandler.sendToServer(new SpreadPacket(blockPos.getX(), blockPos.getY(), blockPos.getZ(), "ramwal:glitch"));
                }
            }
        }
    }

    public static boolean canSpread(BlockState s) {
        return (s.isSolid() &&
                !s.getMaterial().equals(Material.WOOL) &&
                !s.getBlock().equals(Blocks.BEDROCK) &&
                !s.getBlock().equals(Blocks.END_PORTAL_FRAME) &&
                !(s.getBlock() instanceof Glitch));
    }
}
