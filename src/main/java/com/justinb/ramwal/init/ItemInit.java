package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.builders.CraftableLemonBuilder;
import com.justinb.ramwal.inherited.misc.ModSpawnEggItem;
import com.justinb.ramwal.builders.LemonBuilder;
import com.justinb.ramwal.items.*;
import com.justinb.ramwal.recipes.Recipe;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.rmi.registry.Registry;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

    public static final RegistryObject<Item> LEMONADE = ITEMS.register("lemonade",
        () -> new LemonadeItem(new Item.Properties().maxStackSize(1).group(ModItemGroup.instance).food(new Food.Builder()
            .hunger(0)
            .saturation(0)
            .setAlwaysEdible()
            .effect(() -> new EffectInstance(Effects.HASTE, 600, 1), 1.0f)
            .effect(() -> new EffectInstance(Effects.SPEED, 600, 1), 1.0f)
            .effect(() -> new EffectInstance(Effects.NAUSEA, 200, 1), 0.1f)
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

    //Tech
    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery",
            () -> new BatteryItem(10));

    public static final RegistryObject<Item> CREATIVEBATTERY = ITEMS.register("creativebattery",
            () -> new CreativeBatteryItem());

    public static final RegistryObject<LemonPouchItem> LEMONPOUCH = ITEMS.register("lemonpouch",
            () -> new LemonPouchItem(new Item.Properties().maxStackSize(1).group(ModItemGroup.instance)));

    //Disc
    public static final RegistryObject<MusicDiscItem> RAMWAL = ITEMS.register("music_disc_owner",
            () -> new MusicDiscItem(14, SoundInit.RAMWAL, (new Item.Properties())
                    .maxStackSize(1)
                    .group(ItemGroup.MISC)
                    .rarity(Rarity.RARE)));

    //Spawn Eggs
    public static final RegistryObject<SpawnEggItem> DISCIPLE_EGG = ITEMS.register("disciple_egg",
            () -> new ModSpawnEggItem(EntityInit.DISCIPLE::get, 0x363636, 0x1f1f1f, new Item.Properties().group(ModItemGroup.instance)));

    public static final RegistryObject<SpawnEggItem> MIMIC_EGG = ITEMS.register("mimic_egg",
            () -> new ModSpawnEggItem(EntityInit.MIMIC::get, 0x0af047, 0x9c6d00, new Item.Properties().group(ModItemGroup.instance)));

    //Block items ---------------------------------------------------------------------------

    public static final RegistryObject<BlockItem> GLITCH = ITEMS.register("glitch",
            () -> new BlockItem(BlockInit.GLITCH.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static final RegistryObject<BlockItem> LEMONSPAWNERI = ITEMS.register("lemonspawneri",
            () -> new BlockItem(BlockInit.LEMONSPAWNERI.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static final RegistryObject<BlockItem> LEMONSPAWNERII = ITEMS.register("lemonspawnerii",
            () -> new BlockItem(BlockInit.LEMONSPAWNERII.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static final RegistryObject<BlockItem> INTEGRATOR = ITEMS.register("integrator",
            () -> new BlockItem(BlockInit.INTEGRATOR.get(), new Item.Properties().group(ModItemGroup.instance)));

    public static class ModItemGroup extends ItemGroup {
        public static final ModItemGroup instance = new ModItemGroup(ItemGroup.GROUPS.length, "ramwal");

        private ModItemGroup(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack createIcon() {
            return new ItemStack(LemonInit.LEMON.get());
        }
    }
}
