package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.blocks.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

    public static final RegistryObject<Block> GLITCH = BLOCKS.register("glitch",
            () -> new Glitch(AbstractBlock.Properties.create(Material.IRON)
            .hardnessAndResistance(10, 1200)
            .sound(SoundType.GLASS)
            .harvestLevel(1)
            .harvestTool(ToolType.PICKAXE)
            .setRequiresTool()));

    public static final RegistryObject<Block> LEMONSPAWNERI = BLOCKS.register("lemonspawneri",
            () -> new LemonSpawnerIBlock(AbstractBlock.Properties.create(Material.IRON)
                    .hardnessAndResistance(0.8f)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)
                    .setRequiresTool()));

    public static final RegistryObject<Block> LEMONSPAWNERII = BLOCKS.register("lemonspawnerii",
            () -> new LemonSpawnerIIBlock(AbstractBlock.Properties.create(Material.IRON)
                    .hardnessAndResistance(0.8f)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)
                    .setRequiresTool()));

    public static final RegistryObject<Block> LEMONPORTAL = BLOCKS.register("lemonportal",
            () -> new LemonPortalBlock(AbstractBlock.Properties.create(Material.PORTAL)
                    .hardnessAndResistance(-1f)
                    .sound(SoundType.GLASS)
                    .doesNotBlockMovement()
                    .setLightLevel((state) -> 2)));

    public static final RegistryObject<Block> INTEGRATOR = BLOCKS.register("integrator",
            () -> new IntegratorBlock(AbstractBlock.Properties.create(Material.IRON)
                .hardnessAndResistance(2.5f, 6.0f)
                .sound(SoundType.METAL)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(1)));
}
