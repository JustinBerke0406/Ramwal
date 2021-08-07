package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.items.*;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

    public static final RegistryObject<Item> LEMON = ITEMS.register("lemon",
            () -> new LemonItem(new Item.Properties().group(ModItemGroup.instance).food(new Food.Builder()
                    .effect(() -> new EffectInstance(Effects.NAUSEA, 200, 3), 1.0f)
                    .saturation(0.4f)
                    .hunger(3)
                    .setAlwaysEdible()
                    .build())));

    public static final RegistryObject<Item> LEMONADE = ITEMS.register("lemonade",
        () -> new LemonadeItem(new Item.Properties().maxStackSize(1).group(ModItemGroup.instance).food(new Food.Builder()
            .hunger(0)
            .saturation(0)
            .setAlwaysEdible()
            .effect(() -> new EffectInstance(Effects.HASTE, 600, 1), 1.0f)
            .effect(() -> new EffectInstance(Effects.SPEED, 600, 1), 1.0f)
            .effect(() -> new EffectInstance(Effects.NAUSEA, 200, 1), 0.1f)
            .build())));

    public static final RegistryObject<Item> PINK_LEMON = ITEMS.register("pinklemon",
            () -> new PinkLemonItem(new Item.Properties().group(ModItemGroup.instance).food(new Food.Builder()
                    .effect(() -> new EffectInstance(Effects.WEAKNESS, 200, 1), 1.0f)
                    .saturation(0.5f)
                    .hunger(3)
                    .setAlwaysEdible()
                    .build())));

    public static final RegistryObject<Item> PINK_LEMONADE = ITEMS.register("pinklemonade",
            () -> new PinkLemonadeItem(new Item.Properties().maxStackSize(1).group(ModItemGroup.instance).food(new Food.Builder()
                    .hunger(0)
                    .saturation(0)
                    .setAlwaysEdible()
                    .effect(() -> new EffectInstance(Effects.HASTE, 600, 2), 1.0f)
                    .effect(() -> new EffectInstance(Effects.SPEED, 600, 2), 1.0f)
                    .effect(() -> new EffectInstance(Effects.WEAKNESS, 200, 1), 0.1f)
                    .effect(() -> new EffectInstance(EffectInit.SUGARRUSH.get(), 200, 1), 0.3f)
                    .build())));

    public static final RegistryObject<Item> LIME = ITEMS.register("lime",
            () -> new LimeItem(new Item.Properties().group(ModItemGroup.instance).food(new Food.Builder()
                    .effect(() -> new EffectInstance(Effects.MINING_FATIGUE, 200, 2), 1.0f)
                    .saturation(0.6f)
                    .hunger(3)
                    .setAlwaysEdible()
                    .build())));

    public static final RegistryObject<Item> LIMEADE = ITEMS.register("limeade",
            () -> new LimeadeItem(new Item.Properties().maxStackSize(1).group(ModItemGroup.instance).food(new Food.Builder()
                    .hunger(0)
                    .saturation(0)
                    .setAlwaysEdible()
                    .effect(() -> new EffectInstance(Effects.HASTE, 600, 2), 1.0f)
                    .effect(() -> new EffectInstance(Effects.SPEED, 600, 2), 1.0f)
                    .effect(() -> new EffectInstance(Effects.MINING_FATIGUE, 200, 1), 0.1f)
                    .effect(() -> new EffectInstance(EffectInit.SUGARRUSH.get(), 200, 2), 0.3f)
                    .build())));

    //Disc
    public static final RegistryObject<MusicDiscItem> RAMWAL = ITEMS.register("music_disc_owner",
            () -> new MusicDiscItem(14, SoundInit.RAMWAL, (new Item.Properties())
                    .maxStackSize(1)
                    .group(ItemGroup.MISC)
                    .rarity(Rarity.RARE)));

    //Block items ---------------------------------------------------------------------------

    public static final RegistryObject<BlockItem> GLITCH = ITEMS.register("glitch",
            () -> new BlockItem(BlockInit.GLITCH.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static final RegistryObject<BlockItem> LEMONSPAWNERI = ITEMS.register("lemonspawneri",
            () -> new BlockItem(BlockInit.LEMONSPAWNERI.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static final RegistryObject<BlockItem> LEMONSPAWNERII = ITEMS.register("lemonspawnerii",
            () -> new BlockItem(BlockInit.LEMONSPAWNERII.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static class ModItemGroup extends ItemGroup {
        public static final ModItemGroup instance = new ModItemGroup(ItemGroup.GROUPS.length, "ramwal");

        private ModItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(LEMON.get());
        }
    }
}
