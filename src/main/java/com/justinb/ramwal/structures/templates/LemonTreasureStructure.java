package com.justinb.ramwal.structures.templates;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.structures.StructureHelper;
import com.justinb.ramwal.structures.pieces.LemonTreasurePiece;
import com.justinb.ramwal.structures.pieces.NBTPiece;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class LemonTreasureStructure extends Structure<NoFeatureConfig> {
    private static final ResourceLocation STRUCTURE_ID = new ResourceLocation(Main.MODID, "lemon_treasure");
    private static final Template STRUCTURE = StructureHelper.readStructure(STRUCTURE_ID);

    public LemonTreasureStructure(Codec<NoFeatureConfig> co) {
        super(co);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return LemonTreasureStructure.Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getStructureName()
    {
        return Main.MODID + ":lemon_treasure";
    }

    public class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> p_i225806_1_, int p_i225806_2_, int p_i225806_3_, MutableBoundingBox p_i225806_4_, int p_i225806_5_, long p_i225806_6_) {
            super(p_i225806_1_, p_i225806_2_, p_i225806_3_, p_i225806_4_, p_i225806_5_, p_i225806_6_);
        }

        public void func_230364_a_(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig noFeatureConfig) {
            int x = (chunkX << 4) | (int) ((Math.random() * 9) + 4);
            int z = (chunkZ << 4) | (int) ((Math.random() * 9) + 4);
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            if (y > 10)
                this.components.add(new LemonTreasurePiece(templateManager, STRUCTURE_ID, STRUCTURE, new BlockPos(x, y - 5, z), rand));

            this.recalculateStructureSize();
        }
    }
}
