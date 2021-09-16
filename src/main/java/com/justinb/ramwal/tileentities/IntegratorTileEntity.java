package com.justinb.ramwal.tileentities;

import com.justinb.ramwal.containers.IntegratorContainer;
import com.justinb.ramwal.inherited.containers.ZoneContents;
import com.justinb.ramwal.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IntegratorTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity {
    public static final int INPUT_SLOTS_COUNT = 3;
    public static final int OUTPUT_SLOTS_COUNT = 1;
    public static final int FUEL_SLOTS_COUNT = 1;
    public static final int TOTAL_SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT + FUEL_SLOTS_COUNT;

    private ZoneContents inputs;
    private ZoneContents output;
    private ZoneContents fuel;

    public IntegratorTileEntity() {
        super(TileEntityInit.INTEGRATOR.get());

        inputs = ZoneContents.createForTileEntity(INPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        output = ZoneContents.createForTileEntity(OUTPUT_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
        fuel = ZoneContents.createForTileEntity(FUEL_SLOTS_COUNT, this::canPlayerAccessInventory, this::markDirty);
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return IntegratorContainer.createContainerServerSide(p_createMenu_1_, p_createMenu_2_, inputs, output, fuel);
    }

    @Override
    public void tick() {
        if (world.isRemote) return; // do nothing on client.
    }

    /**
     * Will the given ItemStack fully fit into the target slot?
     * @param furnaceZoneContents
     * @param slotIndex
     * @param itemStackOrigin
     * @return true if the given ItemStack will fit completely; false otherwise
     */
    public boolean willItemStackFit(ZoneContents furnaceZoneContents, int slotIndex, ItemStack itemStackOrigin) {
        ItemStack itemStackDestination = furnaceZoneContents.getStackInSlot(slotIndex);

        if (itemStackDestination.isEmpty() || itemStackOrigin.isEmpty()) {
            return true;
        }

        if (!itemStackOrigin.isItemEqual(itemStackDestination)) {
            return false;
        }

        int sizeAfterMerge = itemStackDestination.getCount() + itemStackOrigin.getCount();
        return sizeAfterMerge <= furnaceZoneContents.getInventoryStackLimit() && sizeAfterMerge <= itemStackDestination.getMaxStackSize();
    }

    public boolean canPlayerAccessInventory(PlayerEntity player) {
        assert this.world != null;
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
    }

    private final String INPUT_SLOTS_NBT = "inputSlots";
    private final String OUTPUT_SLOTS_NBT = "outputSlots";
    private final String FUEL_SLOTS_NBT = "fuelSlots";

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tile's location

        parentNBTTagCompound.put(INPUT_SLOTS_NBT, inputs.serializeNBT());
        parentNBTTagCompound.put(OUTPUT_SLOTS_NBT, output.serializeNBT());
        parentNBTTagCompound.put(FUEL_SLOTS_NBT, fuel.serializeNBT());
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in writeToNBT
    @Override
    public void read(BlockState blockState, CompoundNBT nbtTagCompound)
    {
        super.read(blockState, nbtTagCompound); // The super call is required to save and load the tile's location

        CompoundNBT inventoryNBT = nbtTagCompound.getCompound(INPUT_SLOTS_NBT);
        inputs.deserializeNBT(inventoryNBT);

        inventoryNBT = nbtTagCompound.getCompound(OUTPUT_SLOTS_NBT);
        output.deserializeNBT(inventoryNBT);

        inventoryNBT = nbtTagCompound.getCompound(FUEL_SLOTS_NBT);
        fuel.deserializeNBT(inventoryNBT);

        if (inputs.getSizeInventory() != INPUT_SLOTS_COUNT
                || output.getSizeInventory() != OUTPUT_SLOTS_COUNT
                || fuel.getSizeInventory() != FUEL_SLOTS_COUNT
        )
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT updateTagDescribingTileEntityState = getUpdateTag();
        final int METADATA = 42; // arbitrary.
        return new SUpdateTileEntityPacket(this.pos, METADATA, updateTagDescribingTileEntityState);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT updateTagDescribingTileEntityState = pkt.getNbtCompound();
        assert world != null;
        BlockState blockState = world.getBlockState(pos);
        handleUpdateTag(blockState, updateTagDescribingTileEntityState);
    }

    /* Creates a tag containing the TileEntity information, used by vanilla to transmit from server to client
       Warning - although our getUpdatePacket() uses this method, vanilla also calls it directly, so don't remove it.
     */
    @Override
    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
     *  The vanilla default is suitable for this example but I've included an explicit definition anyway.
     */
    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT tag) { read(blockState, tag); }

    /**
     * When this tile entity is destroyed, drop all of its contents into the world
     * @param world
     * @param blockPos
     */
    public void dropAllContents(World world, BlockPos blockPos) {
        InventoryHelper.dropInventoryItems(world, blockPos, inputs);
        InventoryHelper.dropInventoryItems(world, blockPos, fuel);
    }

    // -------------  The following two methods are used to make the TileEntity perform as a NamedContainerProvider, i.e.
    //  1) Provide a name used when displaying the container, and
    //  2) Creating an instance of container on the server, and linking it to the inventory items stored within the TileEntity

    /**
     *  standard code to look up what the human-readable name is.
     *  Can be useful when the tileentity has a customised name (eg "David's footlocker")
     */
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.ramwal.integrator");
    }
}
