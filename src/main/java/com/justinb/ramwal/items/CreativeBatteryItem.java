package com.justinb.ramwal.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class CreativeBatteryItem extends BatteryItem {
    public CreativeBatteryItem() {
        super(10000000);
    }

    @Override
    public void damageBy(ItemStack stack, int amount) {
        stack.setDamage(0);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flags) {
        tooltip.add(new StringTextComponent(""));
        tooltip.add(new StringTextComponent("Power:").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent("\u221E/\u221E").mergeStyle(TextFormatting.GREEN));
    }
}
