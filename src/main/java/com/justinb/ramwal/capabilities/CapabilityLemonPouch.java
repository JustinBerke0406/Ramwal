package com.justinb.ramwal.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityLemonPouch implements ICapabilitySerializable<INBT> {
    private final Direction NO_SPECIFIC_SIDE = null;

    //  a supplier: when called, returns the result of getCachedInventory()
    private final LazyOptional<IItemHandler> lazyInitialisionSupplier = LazyOptional.of(this::getCachedInventory);

    private ItemStackHandlerLemonPouch itemStackHandlerLemonPouch;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, lazyInitialisionSupplier);
    }

    @Override
    public INBT serializeNBT() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(getCachedInventory(), NO_SPECIFIC_SIDE);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(getCachedInventory(), NO_SPECIFIC_SIDE, nbt);
    }

    private ItemStackHandlerLemonPouch getCachedInventory() {
        if (itemStackHandlerLemonPouch == null) {
            itemStackHandlerLemonPouch = new ItemStackHandlerLemonPouch();
        }
        return itemStackHandlerLemonPouch;
    }
}
