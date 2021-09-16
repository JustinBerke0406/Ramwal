package com.justinb.ramwal.containers;

import com.justinb.ramwal.inherited.containers.ZoneContents;
import com.justinb.ramwal.inherited.containers.slots.OutputSlot;
import com.justinb.ramwal.init.ContainerInit;
import com.justinb.ramwal.init.ItemInit;
import com.justinb.ramwal.items.BatteryItem;
import com.justinb.ramwal.recipes.Recipe;
import com.justinb.ramwal.recipes.Recipes;
import com.justinb.ramwal.tileentities.IntegratorTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class IntegratorContainer extends Container {
    public static IntegratorContainer createContainerServerSide(int windowID, PlayerInventory playerInventory,
                                                                ZoneContents inputs, ZoneContents outputs, ZoneContents fuel) {
        return new IntegratorContainer(windowID, playerInventory, inputs, outputs, fuel);
    }

    public static IntegratorContainer createContainerClientSide(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        //  don't need extraData for this example; if you want you can use it to provide extra information from the server, that you can use
        //  when creating the client container
        //  eg String detailedDescription = extraData.readString(128);
        ZoneContents inputs = ZoneContents.createForClientSideContainer(INPUT_SLOTS_COUNT);
        ZoneContents outputs = ZoneContents.createForClientSideContainer(OUTPUT_SLOTS_COUNT);
        ZoneContents fuel = ZoneContents.createForClientSideContainer(FUEL_SLOTS_COUNT);

        // on the client side there is no parent TileEntity to communicate with, so we:
        // 1) use dummy inventories and furnace state data (tracked ints)
        // 2) use "do nothing" lambda functions for canPlayerAccessInventory and markDirty
        return new IntegratorContainer(windowID, playerInventory,
                inputs, outputs, fuel);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

    public static final int INPUT_SLOTS_COUNT = IntegratorTileEntity.INPUT_SLOTS_COUNT;
    public static final int OUTPUT_SLOTS_COUNT = IntegratorTileEntity.OUTPUT_SLOTS_COUNT;
    public static final int FUEL_SLOTS_COUNT = IntegratorTileEntity.FUEL_SLOTS_COUNT;
    public static final int INTEGRATOR_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT + FUEL_SLOTS_COUNT;

    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int HOTBAR_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX;
    private static final int PLAYER_INVENTORY_FIRST_SLOT_INDEX = HOTBAR_FIRST_SLOT_INDEX + HOTBAR_SLOT_COUNT;
    private static final int INPUT_SLOT_INDEX = PLAYER_INVENTORY_FIRST_SLOT_INDEX + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int OUTPUT_SLOT_INDEX = INPUT_SLOT_INDEX + INPUT_SLOTS_COUNT;
    private static final int FUEL_SLOT_INDEX = OUTPUT_SLOT_INDEX + OUTPUT_SLOTS_COUNT;

    public static final int PLAYER_INVENTORY_XPOS = 8;
    public static final int PLAYER_INVENTORY_YPOS = 125;

    private ZoneContents inputs;
    private ZoneContents outputs;
    private ZoneContents fuel;

    private PlayerInventory playerInv;

    private World world; //needed for some helper methods
    private static final Logger LOGGER = LogManager.getLogger();

    public IntegratorContainer(int windowID, PlayerInventory invPlayer,
                               ZoneContents inputs,
                               ZoneContents outputs,
                               ZoneContents fuel) {
        super(ContainerInit.INTEGRATOR.get(), windowID);
        if (ContainerInit.INTEGRATOR.get() == null)
            throw new IllegalStateException("Must initialise containerTypeContainerFurnace before constructing a ContainerFurnace!");
        this.inputs = inputs;

        this.inputs.setRunnable(fuel::markDirty);

        this.outputs = outputs;
        this.fuel = fuel;

        this.fuel.setRunnable(() -> {
            invalidateOutput();

            if (getFuel() != null) {
                Recipe r = getRecipe();

                if (r != null && canFuelProvidePower(r)) {
                    setOutput(new ItemStack(r.getResult()));
                    outputs.setRunnable(() -> onFuelUsed(r));
                }
                else invalidateOutput();
            }
        });

        this.world = invPlayer.player.world;

        this.playerInv = invPlayer;

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;
        final int HOTBAR_XPOS = 8;
        final int HOTBAR_YPOS = 183;
        // Add the players hotbar to the gui - the [xpos, ypos] location of each item
        for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
            addSlot(new Slot(invPlayer, x, HOTBAR_XPOS + SLOT_X_SPACING * x, HOTBAR_YPOS));
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

        final int INPUT_SLOTS_XPOS = 35;
        final int INPUT_SLOTS_YPOS = 20;
        // Add the tile input slots
        for (int y = 0; y < INPUT_SLOTS_COUNT; y++) {
            addSlot(new Slot(inputs, y, INPUT_SLOTS_XPOS + (45 * y), INPUT_SLOTS_YPOS));
        }

        final int OUTPUT_SLOTS_XPOS = 80;
        final int OUTPUT_SLOTS_YPOS = 87;
        // Add the tile output slots
        for (int y = 0; y < OUTPUT_SLOTS_COUNT; y++) {
            addSlot(new OutputSlot(outputs, inputs, y, OUTPUT_SLOTS_XPOS, OUTPUT_SLOTS_YPOS));
        }

        final int FUEL_SLOTS_XPOS = 152;
        final int FUEL_SLOTS_YPOS = 104;

        for (int y = 0; y < FUEL_SLOTS_COUNT; y++) {
            addSlot(new Slot(fuel, y, FUEL_SLOTS_XPOS, FUEL_SLOTS_YPOS));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity player)
    {
        return inputs.isUsableByPlayer(player) && outputs.isUsableByPlayer(player) && fuel.isUsableByPlayer(player);
    }

    private Recipe getRecipe() {
        ArrayList<Item> recipe = new ArrayList<>();

        for (int i = 0; i < inputs.getSizeInventory(); i++)
            if (inputs.getStackInSlot(i).getItem() != Items.AIR)
                recipe.add(inputs.getStackInSlot(i).getItem());

        return Recipe.getRecipe(recipe);
    }

    private boolean isFuelValid() {
        return fuel.getStackInSlot(0).getItem() instanceof BatteryItem;
    }

    private boolean canFuelProvidePower(Recipe r) {
        return isFuelValid() && (fuel.getStackInSlot(0).getMaxDamage() - fuel.getStackInSlot(0).getDamage()) >= 2 * r.getCost();
    }

    private ItemStack getFuel() {
        return fuel.getStackInSlot(0);
    }

    private void setOutput(ItemStack stack) {
        outputs.setInventorySlotContents(0, stack);
    }

    private void invalidateOutput() {
        setOutput(ItemStack.EMPTY);

        outputs.setRunnable(null);
    }

    public void onFuelUsed(Recipe recipe) {
        ((BatteryItem) getFuel().getItem()).damageBy(getFuel(), recipe.getCost());
        fuel.markDirty();
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
            case FUEL_ZONE:
            case INPUT_ZONE:
                successfulTransfer = mergeInto(SlotZone.PLAYER_HOTBAR, sourceItemStack, true);
                if (!successfulTransfer) {
                    successfulTransfer = mergeInto(SlotZone.PLAYER_MAIN_INVENTORY, sourceItemStack, true);
                }
                if (successfulTransfer) {  // removing from output means we have just crafted an item -> need to inform
                    sourceSlot.onSlotChange(sourceItemStack, sourceStackBeforeMerge);
                }
                break;
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
        INPUT_ZONE(INPUT_SLOT_INDEX, INPUT_SLOTS_COUNT),
        OUTPUT_ZONE(OUTPUT_SLOT_INDEX, OUTPUT_SLOTS_COUNT),
        FUEL_ZONE(FUEL_SLOT_INDEX, FUEL_SLOTS_COUNT),
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
