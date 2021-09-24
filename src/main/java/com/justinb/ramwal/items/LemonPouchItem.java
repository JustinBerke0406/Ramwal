package com.justinb.ramwal.items;

import com.justinb.ramwal.capabilities.CapabilityLemonPouch;
import com.justinb.ramwal.capabilities.ItemStackHandlerLemonPouch;
import com.justinb.ramwal.containers.LemonPouchContainer;
import com.justinb.ramwal.init.ItemInit;
import com.justinb.ramwal.recipes.Recipe;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LemonPouchItem extends Item {
    private final String BASE_NBT_TAG = "base";
    private final String CAPABILITY_NBT_TAG = "cap";

    public LemonPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {  // server only!
            INamedContainerProvider containerProviderLemon = new ContainerProviderLemonPouch(stack);
            final int NUMBER_OF_SLOTS = 5;
            NetworkHooks.openGui((ServerPlayerEntity) player,
                    containerProviderLemon,
                    (packetBuffer)-> packetBuffer.writeInt(NUMBER_OF_SLOTS));
            // We use the packetBuffer to send the bag size; not necessary since it's always 16, but just for illustration purposes
        }
        return ActionResult.resultSuccess(stack);
    }

    private static ItemStackHandlerLemonPouch getItemStackHandlerLemonPouch(ItemStack itemStack) {
        Optional<IItemHandler> ih = itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve();

        if (!ih.isPresent()) return new ItemStackHandlerLemonPouch();

        IItemHandler pouch = ih.get();

        if (!(pouch instanceof ItemStackHandlerLemonPouch)) {
            return new ItemStackHandlerLemonPouch();
        }
        return (ItemStackHandlerLemonPouch) pouch;
    }

    @Nonnull
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {

        return new CapabilityLemonPouch();
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
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flags) {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == null) return;

        Optional<IItemHandler> ih = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve();

        if (!ih.isPresent()) return;

        IItemHandler items = ih.get();

        for (int i = 0; i < 5; i++) {
            if (items.getStackInSlot(i).getItem() != Items.AIR)
                tooltip.add(new StringTextComponent(items.getStackInSlot(i).getItem().getName().getString()).mergeStyle(TextFormatting.BLUE));
        }
    }

    private static class ContainerProviderLemonPouch implements INamedContainerProvider {
        private final ItemStack stack;

        public ContainerProviderLemonPouch(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        @Nonnull
        public ITextComponent getDisplayName() {
            return stack.getDisplayName();
        }

        @Nullable
        @Override
        public Container createMenu(int p_createMenu_1_, @Nullable PlayerInventory p_createMenu_2_, @Nonnull PlayerEntity p_createMenu_3_) {
            return LemonPouchContainer.createContainerServerSide(p_createMenu_1_, p_createMenu_2_,
                    getItemStackHandlerLemonPouch(stack),
                    stack);
        }
    }

    public static ItemStack createRecipeHolder(Recipe r) {
        ItemStack pouch = new ItemStack(ItemInit.LEMONPOUCH.get(), 1);
        IItemHandler items = pouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();

        int temp = 0;

        for (Item inputs : r.getInputs())
            items.insertItem(temp++, new ItemStack(inputs), false);

        return pouch;
    }

    public static Recipe getRecipe(ItemStack stack) {
        IItemHandler ih = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();

        ArrayList<Item> in = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Item temp = ih.getStackInSlot(i).getItem();

            if (temp != Items.AIR)
                in.add(temp);
        }

        return Recipe.getRecipe(in);
    }
}
