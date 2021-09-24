package com.justinb.ramwal.inherited.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ZoneContents implements IInventory {
    public static ZoneContents createForTileEntity(int size,
                                                   Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
                                                   Notify markDirtyNotificationLambda) {
        return new ZoneContents(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
    }

    public static ZoneContents createForClientSideContainer(int size) {
        return new ZoneContents(size);
    }

    public ZoneContents(ZoneContents work) {
        this.canPlayerAccessInventoryLambda = work.canPlayerAccessInventoryLambda;
        this.closeInventoryNotificationLambda = work.closeInventoryNotificationLambda;
        this.markDirtyNotificationLambda = work.markDirtyNotificationLambda;
        this.zoneComponentContents = work.zoneComponentContents;
        this.openInventoryNotificationLambda = work.openInventoryNotificationLambda;
    }

    @Override
    public int getSizeInventory() {
        return zoneComponentContents.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < zoneComponentContents.getSlots(); ++i) {
            if (!zoneComponentContents.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        return zoneComponentContents.getStackInSlot(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (count < 0) throw new IllegalArgumentException("count should be >= 0:" + count);
        return zoneComponentContents.extractItem(index, count, false);
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        int maxPossibleItemStackSize = zoneComponentContents.getSlotLimit(index);
        return zoneComponentContents.extractItem(index, maxPossibleItemStackSize, false);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        zoneComponentContents.setStackInSlot(index, stack);
    }

    @Override
    public void markDirty() {
        markDirtyNotificationLambda.invoke();

        if (runnable != null) runnable.run();
    }

    @Override
    public void openInventory(@Nonnull PlayerEntity player) {
        openInventoryNotificationLambda.invoke();
    }

    @Override
    public void closeInventory(@Nonnull PlayerEntity player) {
        closeInventoryNotificationLambda.invoke();
    }

    /**
     * Writes the chest contents to a CompoundNBT tag (used to save the contents to disk)
     * @return the tag containing the contents
     */
    public CompoundNBT serializeNBT()  {
        return zoneComponentContents.serializeNBT();
    }

    /**
     * Fills the chest contents from the nbt; resizes automatically to fit.  (used to load the contents from disk)
     * @param nbt
     */
    public void deserializeNBT(CompoundNBT nbt)   {
        zoneComponentContents.deserializeNBT(nbt);
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return canPlayerAccessInventoryLambda.test(player);
    }

    @Override
    public void clear() {
        for (int i = 0; i < zoneComponentContents.getSlots(); ++i) {
            zoneComponentContents.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public interface Notify {   // Some folks use Runnable, but I prefer not to use it for non-thread-related tasks
        void invoke();
    }

    private final ItemStackHandler zoneComponentContents;

    // the function that the container should call in order to decide if the
    // given player can access the container's Inventory or not.  Only valid server side
    //  default is "true".
    private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x-> true;

    // the function that the container should call in order to tell the parent TileEntity that the
    // contents of its inventory have been changed.
    // default is "do nothing"
    private Notify markDirtyNotificationLambda = ()->{};

    // the function that the container should call in order to tell the parent TileEntity that the
    // container has been opened by a player (eg so that the chest can animate its lid being opened)
    // default is "do nothing"
    private Notify openInventoryNotificationLambda = ()->{};

    // the function that the container should call in order to tell the parent TileEntity that the
    // container has been closed by a player
    // default is "do nothing"
    private Notify closeInventoryNotificationLambda = ()->{};


    private ZoneContents(int size) {
        this.zoneComponentContents = new ItemStackHandler(size);
    }

    private ZoneContents(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
        this.zoneComponentContents = new ItemStackHandler(size);
        this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
        this.markDirtyNotificationLambda = markDirtyNotificationLambda;
    }

    /**
     *  Tries to insert the given ItemStack into the given slot.
     * @param index the slot to insert into
     * @param itemStackToInsert the itemStack to insert.  Is not mutated by the function.
     * @return if successful insertion: ItemStack.EMPTY.  Otherwise, the leftover itemstack
     *         (eg if ItemStack has a size of 23, and only 12 will fit, then ItemStack with a size of 11 is returned
     */
    public ItemStack increaseStackSize(int index, ItemStack itemStackToInsert) {
        return zoneComponentContents.insertItem(index, itemStackToInsert, false);
    }

    /**
     *  Checks if the given slot will accept all of the given itemStack
     * @param index the slot to insert into
     * @param itemStackToInsert the itemStack to insert
     * @return if successful insertion: ItemStack.EMPTY.  Otherwise, the leftover itemstack
     *         (eg if ItemStack has a size of 23, and only 12 will fit, then ItemStack with a size of 11 is returned
     */
    public boolean doesItemStackFit(int index, ItemStack itemStackToInsert) {
        ItemStack leftoverItemStack = zoneComponentContents.insertItem(index, itemStackToInsert, true);
        return leftoverItemStack.isEmpty();
    }

    private DirtyRunnable runnable = null;

    public void setRunnable(DirtyRunnable run) {
        this.runnable = run;
    }

    public DirtyRunnable getRunnable() {
        return this.runnable;
    }

    public interface DirtyRunnable {
        void run();
    }
}
