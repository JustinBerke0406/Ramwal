package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.loot.LemonLeafModifer;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LootModifierInit {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Main.MODID);

    public static final RegistryObject<LemonLeafModifer.Serializer> LEMON_LEAF = SERIALIZERS.register("lemon_leaf", LemonLeafModifer.Serializer::new);
}
