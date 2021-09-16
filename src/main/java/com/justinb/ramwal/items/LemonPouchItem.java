package com.justinb.ramwal.items;

import com.justinb.ramwal.capabilities.CapabilityLemonPouch;
import com.justinb.ramwal.capabilities.ItemStackHandlerLemonPouch;
import com.justinb.ramwal.containers.LemonPouchContainer;
import com.justinb.ramwal.inherited.containers.ZoneContents;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class LemonPouchItem extends Item {
    private final String BASE_NBT_TAG = "base";
    private final String CAPABILITY_NBT_TAG = "cap";

    public LemonPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {  // server only!
            INamedContainerProvider containerProviderLemon = new ContainerProviderLemonPouch(this, stack);
            final int NUMBER_OF_SLOTS = 5;
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    containerProviderLemon,
                    (packetBuffer)-> packetBuffer.writeInt(NUMBER_OF_SLOTS));
            // We use the packetBuffer to send the bag size; not necessary since it's always 16, but just for illustration purposes
        }
        return ActionResult.resultSuccess(stack);
    }

    private static ItemStackHandlerLemonPouch getItemStackHandlerLemonPouch(ItemStack itemStack) {
        IItemHandler pouch = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (pouch == null || !(pouch instanceof ItemStackHandlerLemonPouch)) {
            return new ItemStackHandlerLemonPouch();
        }
        return (ItemStackHandlerLemonPouch) pouch;
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
        CapabilityLemonPouch cp = new CapabilityLemonPouch();

        return cp;
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT baseTag = stack.getTag();
        ItemStackHandlerLemonPouch itemStackHandlerFlowerBag = getItemStackHandlerLemonPouch(stack);
        CompoundNBT capabilityTag = itemStackHandlerFlowerBag.serializeNBT();
        CompoundNBT combinedTag = new CompoundNBT();
        if (baseTag != null) {
            combinedTag.put(BASE_NBT_TAG, baseTag);
        }
        if (capabilityTag != null) {
            combinedTag.put(CAPABILITY_NBT_TAG, capabilityTag);
        }
        return combinedTag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt == null) {
            stack.setTag(null);
            return;
        }
        CompoundNBT baseTag = nbt.getCompound(BASE_NBT_TAG);              // empty if not found
        CompoundNBT capabilityTag = nbt.getCompound(CAPABILITY_NBT_TAG); // empty if not found
        stack.setTag(baseTag);
        ItemStackHandlerLemonPouch itemStackHandlerFlowerBag = getItemStackHandlerLemonPouch(stack);
        itemStackHandlerFlowerBag.deserializeNBT(capabilityTag);
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flags) {
        ArrayList<ITextComponent> t = new ArrayList<>(tooltip);

        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == null) return;

        IItemHandler items = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();

        for (int i = 0; i < 5; i++) {
            if (items.getStackInSlot(i).getItem() != Items.AIR)
                tooltip.add(new StringTextComponent(items.getStackInSlot(i).getItem().getName().getString()).mergeStyle(TextFormatting.BLUE));
        }
    }

    private static class ContainerProviderLemonPouch implements INamedContainerProvider {
        private LemonPouchItem item;
        private ItemStack stack;

        public ContainerProviderLemonPouch(LemonPouchItem item, ItemStack stack) {
            this.item = item;
            this.stack = stack;
        }

        @Override
        public ITextComponent getDisplayName() {
            return stack.getDisplayName();
        }

        @Nullable
        @Override
        public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
            LemonPouchContainer newContainerServerSide =
                    LemonPouchContainer.createContainerServerSide(p_createMenu_1_, p_createMenu_2_,
                            item.getItemStackHandlerLemonPouch(stack),
                            stack);
            return newContainerServerSide;
        }
    }
}
