package com.justinb.ramwal.inherited.containers.slots;

import com.justinb.ramwal.inherited.containers.ZoneContents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class OutputSlot extends Slot {
    private ZoneContents inputs;

    public OutputSlot(IInventory inventoryIn, ZoneContents inputs, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);

        this.inputs = inputs;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack onTake(@Nonnull PlayerEntity thePlayer, ItemStack stack) {
        for (int i = 0; i < inputs.getSizeInventory(); i++)
            inputs.decrStackSize(i, 1);

        return super.onTake(thePlayer, stack);
    }
}
