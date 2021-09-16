package com.justinb.ramwal.capabilities;

import com.justinb.ramwal.items.AbstractLemonItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemStackHandlerLemonPouch extends ItemStackHandler {
    public static final int SLOT_AMOUNT = 5;
    private boolean isDirty = true;

    public ItemStackHandlerLemonPouch() {
        super(SLOT_AMOUNT);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= SLOT_AMOUNT) {
            throw new IllegalArgumentException("Invalid slot number:"+slot);
        }
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (item instanceof AbstractLemonItem) return true;
        return false;
    }

    public int getNumberOfEmptySlots() {
        final int NUMBER_OF_SLOTS = getSlots();

        int emptySlotCount = 0;
        for (int i = 0; i < NUMBER_OF_SLOTS; ++i) {
            if (getStackInSlot(i) == ItemStack.EMPTY) {
                ++emptySlotCount;
            }
        }
        return emptySlotCount;
    }

    public boolean isDirty() {
        boolean currentState = isDirty;
        isDirty = false;
        return currentState;
    }

    protected void onContentsChanged(int slot) {
        isDirty = true;
    }
}
