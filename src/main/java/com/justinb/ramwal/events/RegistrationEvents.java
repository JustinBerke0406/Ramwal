package com.justinb.ramwal.events;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.init.EntityInit;
import com.justinb.ramwal.mobs.entities.DiscipleEntity;
import com.justinb.ramwal.mobs.entities.MimicEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistrationEvents {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(EntityInit.DISCIPLE.get(), DiscipleEntity.setCustomAttributes().create());
        event.put(EntityInit.MIMIC.get(), MimicEntity.setCustomAttributes().create());
    }
}
