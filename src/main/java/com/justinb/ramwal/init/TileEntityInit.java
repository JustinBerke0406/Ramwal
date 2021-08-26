package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.tileentities.ProgrammerTileEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);

    public static RegistryObject<TileEntityType<ProgrammerTileEntity>> PROGRAMMER = TILE_ENTITIES.register("programmer",
            () -> TileEntityType.Builder.create(ProgrammerTileEntity::new, BlockInit.PROGRAMMER.get()).build(null));

}
