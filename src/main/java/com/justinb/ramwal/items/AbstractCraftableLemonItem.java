package com.justinb.ramwal.items;

import com.justinb.ramwal.recipes.Recipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class AbstractCraftableLemonItem extends AbstractLemonItem {
    public AbstractCraftableLemonItem(Properties properties, int color) {
        super(properties, color);
    }

    public static int getItemColor(ItemStack stack, int tintIndex) {
        return (tintIndex == 0) ? ((AbstractCraftableLemonItem) stack.getItem()).getColor() : 0xFFFFFF;
    }
}
