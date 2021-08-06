package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.effects.SugarRushEffect;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class EffectInit {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Main.MODID);

    public static final RegistryObject<Effect> SUGARRUSH = EFFECTS.register("sugarrush",
            () -> new SugarRushEffect(EffectType.BENEFICIAL, 0xff97d3)
                    .addAttributesModifier(Attributes.MOVEMENT_SPEED, "CC301E38-207E-400D-A7EA-8C4B198EE36C", 1.5, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributesModifier(Attributes.ATTACK_SPEED, "9E1FEE34-EB9F-4CAB-9590-32A8149D15AD", 2, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributesModifier(Attributes.ATTACK_KNOCKBACK, "158A29BD-1648-451A-BC65-E0954BCF0FDB", 2.5, AttributeModifier.Operation.MULTIPLY_TOTAL));
}
