package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.blocks.Glitch;
import com.justinb.ramwal.blocks.LemonSpawnerIBlock;
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
}
