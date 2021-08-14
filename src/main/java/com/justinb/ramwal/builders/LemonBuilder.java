package com.justinb.ramwal.builders;

import com.justinb.ramwal.blocks.Glitch;
import com.justinb.ramwal.init.ItemInit;
import com.justinb.ramwal.items.AbstractLemonItem;
import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.network.SpreadPacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Supplier;

public class LemonBuilder {
    private static ArrayList<AbstractLemonItem> items = new ArrayList<>();

    public static AbstractLemonItem build(int color, float saturation, int hunger, ILemonBehavior behavior, float[] probs, Supplier<EffectInstance>... effects) {
        Food.Builder builder = new Food.Builder();

        builder = builder
                .saturation(saturation)
                .hunger(hunger)
                .setAlwaysEdible();

        for (int i = 0; i < effects.length; i++)
            builder = builder.effect(effects[i], probs[i]);

        AbstractLemonItem lemon = new AbstractLemonItem(new Item.Properties().group(ItemInit.ModItemGroup.instance).food(builder.build()), color) {
            @Override
            public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
                return behavior.run(stack, worldIn, entityLiving);
            }
        };

        items.add(lemon);

        return lemon;
    }

    private static ItemStack superItemFinish(Item item, ItemStack stack, World worldIn, LivingEntity entityLiving) {
        return item.isFood() ? entityLiving.onFoodEaten(worldIn, stack) : stack;
    }

    public static ArrayList<AbstractLemonItem> getItems() {
        return items;
    }

    public interface ILemonBehavior {
        ItemStack run(ItemStack stack, World worldIn, LivingEntity entityLiving);
    }

    public static final ILemonBehavior LEMON_BEHAVIOR = (ItemStack stack, World worldIn, LivingEntity entityLiving) -> {
        BlockPos pos = entityLiving.getPosition();

        if (worldIn.isRemote() && Glitch.canSpread(worldIn.getBlockState(pos.down())))
            if (new Random().nextInt(100/10) == 0)
                NetworkHandler.sendToServer(new SpreadPacket("ramwal:glitch", pos.down()));

        return superItemFinish(stack.getItem(), stack, worldIn, entityLiving);};

    public static final ILemonBehavior PINK_LEMON_BEHAVIOR = (stack, worldIn, entityLiving) -> {
        if (!worldIn.isRemote) {
            Random rand = new Random();

            int CHANCE_BAD = 20;
            int AMOUNT = 7;
            int CHANCE_GOOD = 2;

            if (rand.nextInt(100/CHANCE_BAD) == 0)
                for (int i = 0; i < AMOUNT; i++) {
                    CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, worldIn);
                    creeper.setPositionAndRotation(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), entityLiving.rotationYaw, entityLiving.rotationPitch);
                    creeper.setAbsorptionAmount(1000f);
                    worldIn.addEntity(creeper);
                }
            else if (rand.nextInt(100/CHANCE_GOOD) == 0) {
                BlockState state = Blocks.DIAMOND_ORE.getDefaultState();

                if (worldIn.getGameRules().get(GameRules.KEEP_INVENTORY).get()) {
                    state = Blocks.OAK_SIGN.getDefaultState();

                    if (!worldIn.getBlockState(entityLiving.getPosition().down()).isSolid())
                        worldIn.setBlockState(entityLiving.getPosition().down(), Blocks.DIRT.getDefaultState());

                    worldIn.setBlockState(entityLiving.getPosition(), state);

                    TileEntity te = worldIn.getTileEntity(entityLiving.getPosition());

                    if (te instanceof SignTileEntity) {
                        SignTileEntity ste = (SignTileEntity) te;

                        ste.setText(0, TextComponentUtils.toTextComponent(() -> "turn off"));
                        ste.setText(1, TextComponentUtils.toTextComponent(() -> "keepInventory"));
                        ste.setText(2, TextComponentUtils.toTextComponent(() -> "noob"));
                    }
                }
                else
                    worldIn.setBlockState(entityLiving.getPosition().down(), state);
            }
        }

        return superItemFinish(stack.getItem(), stack, worldIn, entityLiving);
    };

    public static final ILemonBehavior LIME_BEHAVIOR = (stack, worldIn, entityLiving) -> {
        //TODO

        return superItemFinish(stack.getItem(), stack, worldIn, entityLiving);
    };
}
