package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Main.MODID);

    public static final RegistryObject<SoundEvent> RAMWAL = SOUNDS.register("music_disc.owner",
            () -> new SoundEvent(new ResourceLocation("ramwal:music_disc_owner")));
}
