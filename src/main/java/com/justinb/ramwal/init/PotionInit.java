package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PotionInit {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Main.MODID);

    public static final RegistryObject<Potion> SUGARRUSH = POTIONS.register("sugarrush",
            () -> new Potion(new EffectInstance(EffectInit.SUGARRUSH.get(), 600)));
}
