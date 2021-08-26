package com.justinb.ramwal.containers;

import com.justinb.ramwal.inherited.ProgrammerZoneContents;
import com.justinb.ramwal.init.ContainerInit;
import com.justinb.ramwal.tileentities.ProgrammerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class ProgrammerContainer extends Container {
    public static ProgrammerContainer createContainerServerSide(int windowID, PlayerInventory playerInventory,
                                   ProgrammerZoneContents diskIn, ProgrammerZoneContents upgrade) {
        return new ProgrammerContainer(windowID, playerInventory, diskIn, upgrade);
    }

    public static ProgrammerContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        //  don't need extraData for this example; if you want you can use it to provide extra information from the server, that you can use
        //  when creating the client container
        //  eg String detailedDescription = extraData.readString(128);
        ProgrammerZoneContents disk = ProgrammerZoneContents.createForClientSideContainer(DISK_SLOTS_COUNT);
        ProgrammerZoneContents up = ProgrammerZoneContents.createForClientSideContainer(UPGRADE_SLOTS_COUNT);

        // on the client side there is no parent TileEntity to communicate with, so we:
        // 1) use dummy inventories and furnace state data (tracked ints)
        // 2) use "do nothing" lambda functions for canPlayerAccessInventory and markDirty
        return new ProgrammerContainer(windowID, playerInventory,
                disk, up);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    public static final int DISK_SLOTS_COUNT = ProgrammerTileEntity.DISK_SLOTS_COUNT;
    public static final int UPGRADE_SLOTS_COUNT = ProgrammerTileEntity.UPGRADE_SLOTS_COUNT;
    public static final int PROGRAMMER_SLOTS_COUNT = DISK_SLOTS_COUNT + UPGRADE_SLOTS_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
    private static final int FIRST_FUEL_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int DISK_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT + PROGRAMMER_SLOTS_COUNT;
    private static final int UPGRADE_SLOT_INDEX = DISK_SLOT_INDEX + DISK_SLOTS_COUNT;

    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 125;

    private ProgrammerZoneContents disk;
    private ProgrammerZoneContents upgrade;

    private World world; //needed for some helper methods
    private static final Logger LOGGER = LogManager.getLogger();

    public ProgrammerContainer(int windowID, PlayerInventory invPlayer,
                            ProgrammerZoneContents diskIn,
                            ProgrammerZoneContents upgrade) {
        super(ContainerInit.PROGRAMMER.get(), windowID);
        if (ContainerInit.PROGRAMMER.get() == null)
            throw new IllegalStateException("Must initialise containerTypeContainerFurnace before constructing a ContainerFurnace!");
        this.disk = diskIn;
        this.upgrade = upgrade;
        this.world = invPlayer.player.world;

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 183;
        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            int slotNumber = x;
            addSlot(new Slot(invPlayer, slotNumber, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
        }

        // Add the rest of the players inventory to the gui
        for (int y = 0; y < PLAYER_INVENTORY_ROW_COUNT; y++) {
            for (int x = 0; x < PLAYER_INVENTORY_COLUMN_COUNT; x++) {
                int slotNumber = HOTBAR_SLOT_COUNT + y * PLAYER_INVENTORY_COLUMN_COUNT + x;
                int xpos = PLAYER_INVENTORY_XPOS + x * SLOT_X_SPACING;
                int ypos = PLAYER_INVENTORY_YPOS + y * SLOT_Y_SPACING;
                addSlot(new Slot(invPlayer, slotNumber,  xpos, ypos));
            }
        }

        final int INPUT_SLOTS_XPOS = 8;
        final int INPUT_SLOTS_YPOS = 20;
        // Add the tile input slots
        for (int y = 0; y < DISK_SLOTS_COUNT; y++) {
            int slotNumber = y;
            addSlot(new Slot(disk, slotNumber, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS+ SLOT_Y_SPACING * y));
        }

        final int OUTPUT_SLOTS_XPOS = 8;
        final int OUTPUT_SLOTS_YPOS = 95;
        // Add the tile output slots
        for (int y = 0; y < UPGRADE_SLOTS_COUNT; y++) {
            int slotNumber = y;
            addSlot(new Slot(upgrade, slotNumber, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS + SLOT_Y_SPACING * y));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return disk.isUsableByPlayer(player) && upgrade.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int sourceSlotIndex)
    {
        Slot sourceSlot = inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack()) return ItemStack.EMPTY;
        ItemStack sourceItemStack = sourceSlot.getStack();
        ItemStack sourceStackBeforeMerge = sourceItemStack.copy();
        boolean successfulTransfer = false;

        SlotZone sourceZone = SlotZone.getZoneFromIndex(sourceSlotIndex);

        switch (sourceZone) {
            case OUTPUT_ZONE: // taking out of the output zone - try the hotbar first, then main inventory.  fill from the end.
                successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
                if (!successfulTransfer) {
                    successfulTransfer = mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, true);
                }
                if (successfulTransfer) {  // removing from output means we have just crafted an item -> need to inform
                    sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
                }
                break;

            case INPUT_ZONE:
            case PLAYER_HOTBAR:
            case PLAYER_MAIN_INVENTORY: // taking out of inventory - find the appropriate furnace zone
                if (!successfulTransfer) {  // didn't fit into furnace; try player main inventory or hotbar
                    if (sourceZone == SlotZone.PLAYER_HOTBAR) { // main inventory
                        successfulTransfer = mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, false);
                    } else {
                        successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, false);
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("unexpected sourceZone:" + sourceZone);
        }
        if (!successfulTransfer) return ItemStack.EMPTY;

        // If source stack is empty (the entire stack was moved) set slot contents to empty
        if (sourceItemStack.isEmpty()) {
            sourceSlot.putStack(ItemStack.EMPTY);
        } else {
            sourceSlot.onSlotChanged();
        }

        // if source stack is still the same as before the merge, the transfer failed somehow?  not expected.
        if (sourceItemStack.getCount() == sourceStackBeforeMerge.getCount()) {
            return ItemStack.EMPTY;
        }
        sourceSlot.onTake(player, sourceItemStack);
        return sourceStackBeforeMerge;
    }

    /**
     * Try to merge from the given source ItemStack into the given SlotZone.
     * @param destinationZone the zone to merge into
     * @param sourceItemStack the itemstack to merge from
     * @param fillFromEnd if true: try to merge from the end of the zone instead of from the start
     * @return true if a successful transfer occurred
     */
    private boolean mergeInto(SlotZone destinationZone, ItemStack sourceItemStack, boolean fillFromEnd) {
        return mergeItemStack(sourceItemStack, destinationZone.firstIndex, destinationZone.lastIndexPlus1, fillFromEnd);
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn)
    {
        super.onContainerClosed(playerIn);
    }

    private enum SlotZone {
        INPUT_ZONE(DISK_SLOT_INDEX, DISK_SLOTS_COUNT),
        OUTPUT_ZONE(UPGRADE_SLOT_INDEX, UPGRADE_SLOTS_COUNT),
        PLAYER_MAIN_INVENTORY(PLAYER_INVENTORY_FIRST_SLOT_INDEX, PLAYER_INVENTORY_SLOT_COUNT),
        PLAYER_HOTBAR(HOTBAR_FIRST_SLOT_INDEX, HOTBAR_SLOT_COUNT);

        SlotZone(int firstIndex, int numberOfSlots) {
            this.firstIndex = firstIndex;
            this.slotCount = numberOfSlots;
            this.lastIndexPlus1 = firstIndex + numberOfSlots;
        }

        public final int firstIndex;
        public final int slotCount;
        public final int lastIndexPlus1;

        public static SlotZone getZoneFromIndex(int slotIndex) {
            for (SlotZone slotZone : SlotZone.values()) {
                if (slotIndex >= slotZone.firstIndex && slotIndex < slotZone.lastIndexPlus1) return slotZone;
            }
            throw new IndexOutOfBoundsException("Unexpected slotIndex");
        }
    }

}
