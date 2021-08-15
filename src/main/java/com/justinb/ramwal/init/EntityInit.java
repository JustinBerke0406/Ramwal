package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.mobs.entities.DiscipleEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Main.MODID);

    public static final RegistryObject<EntityType<DiscipleEntity>> DISCIPLE = ENTITIES.register("disciple",
            () -> EntityType.Builder.create(DiscipleEntity::new, EntityClassification.MONSTER).immuneToFire().size(0.6f, 3.7f).build("disciple"));
}
