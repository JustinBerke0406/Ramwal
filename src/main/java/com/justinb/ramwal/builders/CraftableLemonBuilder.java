package com.justinb.ramwal.builders;

import com.justinb.ramwal.init.ItemInit;
import com.justinb.ramwal.items.AbstractCraftableLemonItem;
import com.justinb.ramwal.recipes.Recipe;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CraftableLemonBuilder extends LemonBuilder {
    private static ArrayList<AbstractCraftableLemonItem> items = new ArrayList<>();

    public static AbstractCraftableLemonItem build(int color, float saturation, int hunger, ILemonBehavior behavior, float[] probs, Supplier<EffectInstance>... effects) {
        Food.Builder builder = new Food.Builder();

        builder = builder
                .saturation(saturation)
                .hunger(hunger)
                .setAlwaysEdible();

        for (int i = 0; i < effects.length; i++)
            builder = builder.effect(effects[i], probs[i]);

        AbstractCraftableLemonItem lemon = new AbstractCraftableLemonItem(new Item.Properties().group(ItemInit.ModItemGroup.instance).food(builder.build()), color) {
            @Override
            public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
                return behavior.run(stack, worldIn, entityLiving);
            }
        };

        items.add(lemon);

        return lemon;
    }

    public static AbstractCraftableLemonItem build(int color, float saturation, int hunger, ILemonBehavior behavior) {
        return build(color, saturation, hunger, behavior, new float[]{});
    }

    public static ArrayList<AbstractCraftableLemonItem> getCraftableItems() {
        return items;
    }

    //BEHAVIORS
}
