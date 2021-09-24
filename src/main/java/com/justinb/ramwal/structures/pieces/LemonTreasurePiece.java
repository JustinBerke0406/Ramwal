package com.justinb.ramwal.structures.pieces;

import com.justinb.ramwal.Main;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class LemonTreasurePiece extends NBTPiece {
    public LemonTreasurePiece(TemplateManager templateManager, ResourceLocation structureID, Template structure, BlockPos pos, Random random) {
        super(templateManager, structureID, structure, pos, random);
    }

    public LemonTreasurePiece(TemplateManager p_i50677_1_, CompoundNBT nbt) {
        super(p_i50677_1_, nbt);
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
        if ("chesttop".equals(function)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            TileEntity tileentity = worldIn.getTileEntity(pos.down());
            if (tileentity instanceof ChestTileEntity) {
                ((ChestTileEntity)tileentity).setLootTable(new ResourceLocation(Main.MODID, "chests/lemontreasuretop"), rand.nextLong());
            }
        }

        if ("chestbottom".equals(function)) {
            worldIn.setBlockState(pos, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 3);
            TileEntity tileentity = worldIn.getTileEntity(pos.down());
            if (tileentity instanceof ChestTileEntity) {
                ((ChestTileEntity)tileentity).setLootTable(new ResourceLocation(Main.MODID, "chests/lemontreasurebottom"), rand.nextLong());
            }
        }
    }
}
