package com.justinb.ramwal.containers;

import com.justinb.ramwal.capabilities.ItemStackHandlerLemonPouch;
import com.justinb.ramwal.init.ContainerInit;
import com.justinb.ramwal.items.AbstractLemonItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class LemonPouchContainer extends Container {
    public static LemonPouchContainer createContainerServerSide(int windowID, PlayerInventory playerInv, ItemStackHandlerLemonPouch invv, ItemStack stack) {
        return new LemonPouchContainer(windowID, playerInv, stack, invv);
    }

    public static LemonPouchContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        ItemStackHandlerLemonPouch invv = new ItemStackHandlerLemonPouch();

        return new LemonPouchContainer(windowID, playerInventory, ItemStack.EMPTY, invv);
    }

    private ItemStackHandlerLemonPouch inv;
    private ItemStack openedFrom;

    public static final int CUSTOM_START = 0, CUSTOM_END = CUSTOM_START + 4, INV_START = CUSTOM_END + 1,
    INV_END = INV_START + 26,
    HOTBAR_START = INV_END + 1,
    HOTBAR_END = HOTBAR_START + 8;

    public LemonPouchContainer(int windowId, PlayerInventory playerInv, ItemStack stack, ItemStackHandlerLemonPouch inv) {
        super(ContainerInit.LEMONPOUCH.get(), windowId);

        this.inv = inv;
        this.openedFrom = stack;

        int x = 44;
        int y = 76;
        for (int i = 0; i <= CUSTOM_END; i++) addSlot(new SlotItemHandler(inv, i, x + (18*i), y) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() instanceof AbstractLemonItem;
            }
        });

        for (int i = 0; i < 9; i++) addSlot(new Slot(playerInv, i, 8 + i * 18, 183));

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 125 + i * 18));
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity player) {
        ItemStack main = player.getHeldItemMainhand();
        ItemStack off = player.getHeldItemOffhand();
        return (!main.isEmpty() && main == openedFrom) ||
                (!off.isEmpty() && off == openedFrom);
    }

    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack() && slot.getStack() != ItemStack.EMPTY)
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            // If item is in our custom Inventory
            if (index < INV_START)
            {
                // try to place in player inventory / action bar
                if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, true)) {
                    boolean space = false;

                    for (int c = 5; c <= 40; c++)
                        if (!this.inventorySlots.get(c).getHasStack()) space = true;

                    if (space) return itemstack;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            // Item is in inventory / hotbar, try to place in custom inventory
            else
            {
				// Check that the item is the right type
				if (itemstack1.getItem() instanceof AbstractLemonItem) {
                    // Try to merge into your custom inventory slots
                    boolean space = false;

                    for (int c = 0; c < 5; c++)
                        if (!this.inventorySlots.get(c).getHasStack()) space = true;

                    if (!this.mergeItemStack(itemstack1, 0, 5, false))
                        if (space) return itemstack;
                }
            }

            if (itemstack1.isEmpty()) return ItemStack.EMPTY;
            else slot.onSlotChanged();

            if (itemstack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;

            slot.onTake(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull PlayerEntity player) {
        if (slotId >= 0 && getSlot(slotId) != null && getSlot(slotId).getStack() == player.getHeldItemMainhand()) {
            return ItemStack.EMPTY;
        }

        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public void detectAndSendChanges() {
        if (inv.isDirty()) {
            CompoundNBT nbt = openedFrom.getOrCreateTag();
            int dirtyCounter = nbt.getInt("dirtyCounter");
            nbt.putInt("dirtyCounter", dirtyCounter + 1);
            openedFrom.setTag(nbt);
        }

        super.detectAndSendChanges();
    }
}
