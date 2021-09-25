package com.justinb.ramwal.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.justinb.ramwal.Main;
import com.justinb.ramwal.structures.templates.LemonTreasureStructure;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StructureInit {
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Main.MODID);

    public static final RegistryObject<Structure<?>> LEMONTREASURE = STRUCTURES.register("lemontreasure",
            () -> new LemonTreasureStructure(NoFeatureConfig.CODEC));

    public static void setupStructures() {
        setupMapSpacingAndLand(
                LEMONTREASURE.get(), /* The instance of the structure */
                new StructureSeparationSettings(10 /* average distance apart in chunks between spawn attempts */,
                        6 /* minimum distance apart in chunks between spawn attempts. MUST BE LESS THAN ABOVE VALUE*/,
                        186170952 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                true);


        // Add more structures here and so on
    }

    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand)
    {
        /*
         * We need to add our structures into the map in Structure class
         * alongside vanilla structures or else it will cause errors.
         *
         * If the registration is setup properly for the structure,
         * getRegistryName() should never return null.
         */
        Structure.NAME_STRUCTURE_BIMAP.put(Objects.requireNonNull(structure.getRegistryName()).toString(), structure);

        /*
         * Whether surrounding land will be modified automatically to conform to the bottom of the structure.
         * Basically, it adds land at the base of the structure like it does for Villages and Outposts.
         * Doesn't work well on structure that have pieces stacked vertically or change in heights.
         *
         * Note: The air space this method will create will be filled with water if the structure is below sealevel.
         * This means this is best for structure above sealevel so keep that in mind.
         *
         * NOISE_AFFECTING_FEATURES requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
         */

        if(transformSurroundingLand){
            ObfuscationReflectionHelper.setPrivateValue(Structure.class, null, ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.field_236384_t_)
                    .add(structure)
                    .build(), "field_236384_t_");
        }

        /*
         * This is the map that holds the default spacing of all structures.
         * Always add your structure to here so that other mods can utilize it if needed.
         *
         * However, while it does propagate the spacing to some correct dimensions from this map,
         * it seems it doesn't always work for code made dimensions as they read from this list beforehand.
         *
         * Instead, we will use the WorldEvent.Load event in StructureTutorialMain to add the structure
         * spacing from this list into that dimension or to do dimension blacklisting properly.
         * We also use our entry in DimensionStructuresSettings.DEFAULTS in WorldEvent.Load as well.
         *
         * DEFAULTS requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
         */

        ObfuscationReflectionHelper.setPrivateValue(DimensionStructuresSettings.class, null, ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .putAll(DimensionStructuresSettings.field_236191_b_)
                .put(structure, structureSeparationSettings)
                .build(), "field_236191_b_");

        /*
         * There are very few mods that relies on seeing your structure in the noise settings registry before the world is made.
         *
         * You may see some mods add their spacings to DimensionSettings.BUILTIN_OVERWORLD instead of the NOISE_GENERATOR_SETTINGS loop below but
         * that field only applies for the default overworld and won't add to other worldtypes or dimensions (like amplified or Nether).
         * So yeah, don't do DimensionSettings.BUILTIN_OVERWORLD. Use the NOISE_GENERATOR_SETTINGS loop below instead if you must.
         */
        WorldGenRegistries.NOISE_SETTINGS.getEntries().forEach(settings -> {
            DimensionStructuresSettings d = settings.getValue().getStructures();


            Map<Structure<?>, StructureSeparationSettings> structureMap = ObfuscationReflectionHelper.getPrivateValue(DimensionStructuresSettings.class, d, "field_236193_d_");

            /*
             * Pre-caution in case a mod makes the structure map immutable like datapacks do.
             * I take no chances myself. You never know what another mods does...
             *
             * structureConfig requires AccessTransformer  (See resources/META-INF/accesstransformer.cfg)
             */
            if(structureMap instanceof ImmutableMap){
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, structureSeparationSettings);
                ObfuscationReflectionHelper.setPrivateValue(DimensionStructuresSettings.class, d, tempMap, "field_236193_d_");
            }
            else{
                structureMap.put(structure, structureSeparationSettings);
            }
        });
    }
}
