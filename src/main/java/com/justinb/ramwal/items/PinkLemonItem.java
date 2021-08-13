package com.justinb.ramwal.items;

import com.mojang.brigadier.Message;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StandingSignBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.Random;

public class PinkLemonItem extends Item {
    public static final int AMOUNT = 80;
    public static final int CHANCE_BAD = 20;
    public static final int CHANCE_GOOD = 10;

    public PinkLemonItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            Random rand = new Random();

            if (rand.nextInt(100/CHANCE_BAD) == 0)
                for (int i = 0; i < AMOUNT; i++)
                    spawnCreeper(worldIn, entityLiving);
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

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    private void spawnCreeper(World worldIn, LivingEntity entityLiving) {
        CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, worldIn);
        creeper.setPositionAndRotation(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), entityLiving.rotationYaw, entityLiving.rotationPitch);

        Field field;
        try {
            field = creeper.getClass().getDeclaredField("POWERED");
            field.setAccessible(true);
            DataParameter<Boolean> powered = (DataParameter<Boolean>) field.get(creeper);
            creeper.getDataManager().set(powered, true);

            field = creeper.getClass().getDeclaredField("IGNITED");
            field.setAccessible(true);
            DataParameter<Boolean> ign = (DataParameter<Boolean>) field.get(creeper);
            creeper.getDataManager().set(ign, true);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        creeper.setAbsorptionAmount(1000f);
        worldIn.addEntity(creeper);
    }
}
