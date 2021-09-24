package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.tileentities.DeriverTileEntity;
import com.justinb.ramwal.tileentities.IntegratorTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Main.MODID);

    public static RegistryObject<TileEntityType<IntegratorTileEntity>> INTEGRATOR = TILE_ENTITIES.register("integrator",
            () -> TileEntityType.Builder.create(IntegratorTileEntity::new, BlockInit.INTEGRATOR.get()).build(null));

    public static RegistryObject<TileEntityType<DeriverTileEntity>> DERIVER = TILE_ENTITIES.register("deriver",
            () -> TileEntityType.Builder.create(DeriverTileEntity::new, BlockInit.DERIVER.get()).build(null));
}
