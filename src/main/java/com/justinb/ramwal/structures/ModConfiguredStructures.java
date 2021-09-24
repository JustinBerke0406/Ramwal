package com.justinb.ramwal.structures;

import com.justinb.ramwal.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ModConfiguredStructures {
    public static final StructureFeature<?, ?> LEMONTREASURE = ModStructures.LEMONTREASURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(registry, new ResourceLocation(Main.MODID, "conf_lemon_treasure"), LEMONTREASURE);

        FlatGenerationSettings.STRUCTURES.put(ModStructures.LEMONTREASURE, LEMONTREASURE);
    }
}

