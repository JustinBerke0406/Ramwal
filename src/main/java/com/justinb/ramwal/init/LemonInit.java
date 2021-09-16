package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.builders.CraftableLemonBuilder;
import com.justinb.ramwal.builders.LemonBuilder;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class LemonInit {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

    public static final RegistryObject<Item> LEMON = ITEMS.register("lemon",
            () -> LemonBuilder.build(0xffff00, 0.4f, 3, LemonBuilder.LEMON_BEHAVIOR, new float[] {1.0f}, () -> new EffectInstance(Effects.NAUSEA, 200, 3)));

    public static final RegistryObject<Item> PINK_LEMON = ITEMS.register("pinklemon",
            () -> LemonBuilder.build(0xff00ff, 0.5f, 3, LemonBuilder.PINK_LEMON_BEHAVIOR, new float[] {1.0f}, () -> new EffectInstance(Effects.WEAKNESS, 200, 1)));

    public static final RegistryObject<Item> LIME = ITEMS.register("lime",
            () -> LemonBuilder.build(0x00c200, 0.6f, 3, LemonBuilder.LIME_BEHAVIOR, new float[] {1.0f}, () -> new EffectInstance(Effects.MINING_FATIGUE, 200, 1)));

    public static final RegistryObject<Item> DEFAULTLEMON = ITEMS.register("defaultlemon",
            () -> CraftableLemonBuilder.build(0xcccccc, 0, 0, CraftableLemonBuilder.BASE_BEHAVIOR));

    public static final RegistryObject<Item> CONDENSEDLEMON = ITEMS.register("condensedlemon",
            () -> CraftableLemonBuilder.build(0xffff00, 0.1f, 4, CraftableLemonBuilder.LEMON_BEHAVIOR,
                    new float[] {1},
                    () -> new EffectInstance(Effects.NAUSEA, 300, 3)));

    public static final RegistryObject<Item> ANTILEMON = ITEMS.register("antilemon",
            () -> CraftableLemonBuilder.build(0x0000FF, 0.0f, -1, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {1},
                    () -> new EffectInstance(Effects.NAUSEA, 250, 2)));

    public static final RegistryObject<Item> ACIDLEMON = ITEMS.register("acidlemon",
            () -> CraftableLemonBuilder.build(0xfff185, 0.0f, 0, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {1},
                    () -> new EffectInstance(Effects.INSTANT_DAMAGE, 1, 0)));

    public static final RegistryObject<Item> BASICLEMON = ITEMS.register("basiclemon",
            () -> CraftableLemonBuilder.build(0xc2ffff, 0.0f, 0, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {1, 1},
                    () -> new EffectInstance(Effects.NAUSEA, 250, 1),
                    () -> new EffectInstance(Effects.MINING_FATIGUE, 250, 1)));

    public static final RegistryObject<Item> ORANGELEMON = ITEMS.register("orangelemon",
            () -> CraftableLemonBuilder.build(0xffae00, 0.4f, 2, CraftableLemonBuilder.BASE_BEHAVIOR));

    public static final RegistryObject<Item> NEUTRALLEMON = ITEMS.register("neutrallemon",
            () -> CraftableLemonBuilder.build(0xe3ffe3, 0.1f, 0, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {0.5f},
                    () -> new EffectInstance(Effects.NAUSEA, 350, 3)));

    public static final RegistryObject<Item> LIGHTLEMON = ITEMS.register("lightlemon",
            () -> CraftableLemonBuilder.build(0xfcfce1, 0.0f, 1, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {1},
                    () -> new EffectInstance(Effects.GLOWING, 400, 1)));

    public static final RegistryObject<Item> DARKLEMON = ITEMS.register("darklemon",
            () -> CraftableLemonBuilder.build(0x525244, -0.2f, 0, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {1},
                    () -> new EffectInstance(Effects.BLINDNESS, 150, 3)));

    public static final RegistryObject<Item> COMPRESSORLEMON = ITEMS.register("compressorlemon",
            () -> CraftableLemonBuilder.build(0x780e80, 0.0f, 1, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {1},
                    () -> new EffectInstance(Effects.HUNGER, 150, 0)));

    public static final RegistryObject<Item> UNSTABLELEMON = ITEMS.register("unstablelemon",
            () -> CraftableLemonBuilder.build(0xd44700, -0.3f, -3, CraftableLemonBuilder.BASE_BEHAVIOR,
                    new float[] {0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f},
                    () -> new EffectInstance(Effects.INSTANT_DAMAGE, 1, 0),
                    () -> new EffectInstance(Effects.NAUSEA, 200, 1),
                    () -> new EffectInstance(Effects.HUNGER, 200, 1),
                    () -> new EffectInstance(Effects.MINING_FATIGUE, 200, 1),
                    () -> new EffectInstance(Effects.BLINDNESS, 200, 1),
                    () -> new EffectInstance(Effects.POISON, 200, 1),
                    () -> new EffectInstance(Effects.WEAKNESS, 200, 1)));
}
