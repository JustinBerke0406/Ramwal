package com.justinb.ramwal.items;

import com.justinb.ramwal.init.ItemInit;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class BatteryItem extends Item {
    private int maxCharge;

    public BatteryItem(int max) {
        super(createProperties(2*max));

        maxCharge = 2*max;
    }

    private static Properties createProperties(int max) {
        return new Item.Properties().maxStackSize(1).group(ItemInit.ModItemGroup.instance).defaultMaxDamage(max);
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public void damageBy(ItemStack stack, int amount) {
        int toSet = stack.getDamage() + (amount*2);

        stack.setDamage(Math.max(toSet, 0));
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flags) {
        tooltip.add(new StringTextComponent(""));
        tooltip.add(new StringTextComponent("Power:").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new StringTextComponent(((maxCharge - stack.getDamage())/2) + "/" + (maxCharge/2)).mergeStyle(TextFormatting.GREEN));
    }
}
