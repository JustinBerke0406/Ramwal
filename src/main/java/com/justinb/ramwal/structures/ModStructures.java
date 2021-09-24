package com.justinb.ramwal.structures;

import com.google.common.collect.ImmutableMap;
import com.justinb.ramwal.Main;
import com.justinb.ramwal.structures.templates.LemonTreasureStructure;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;

public class ModStructures {
    public static final Structure<NoFeatureConfig> LEMONTREASURE = new LemonTreasureStructure(NoFeatureConfig.CODEC);

    public static void registerStructures(RegistryEvent.Register<Structure<?>> event) {
        Main.register(event.getRegistry(), LEMONTREASURE, "lemon_treasure");

        setupStructure(LEMONTREASURE, new StructureSeparationSettings(10, 6, 4629463));

        ModStructurePieces.registerAllPieces();
    }

    public static <F extends Structure<?>> void setupStructure(F structure, StructureSeparationSettings structureSeparationSettings) {
        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();
    }
}
