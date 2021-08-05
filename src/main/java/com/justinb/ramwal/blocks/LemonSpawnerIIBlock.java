package com.justinb.ramwal.blocks;

import com.justinb.ramwal.init.ItemInit;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.ProxyBlockSource;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class LemonSpawnerIIBlock extends Block {
    public static final int GREEN_CHANCE = 5;

    private static final Map<Item, IDispenseItemBehavior> DISPENSE_BEHAVIOR_REGISTRY = Util.make(new Object2ObjectOpenHashMap<>(), (behaviour) -> {
        behaviour.defaultReturnValue(new DefaultDispenseItemBehavior());
    });

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public LemonSpawnerIIBlock(Properties properties) {
        super(properties);

        setDefaultState(this.getDefaultState().with(FACING, Direction.UP).with(POWERED, false));
    }

    protected void dispense(ServerWorld worldIn, BlockPos pos) {
        ProxyBlockSource proxyblocksource = new ProxyBlockSource(worldIn, pos);

        Item ent = ItemInit.PINK_LEMON.get();

        if (new Random().nextInt(100/GREEN_CHANCE) == 0)
            ent = ItemInit.LIME.get();

        ItemStack itemstack = new ItemStack(ent);
        IDispenseItemBehavior idispenseitembehavior = this.getBehavior(itemstack);

        idispenseitembehavior.dispense(proxyblocksource, itemstack);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean flag = state.get(POWERED);;

            if (flag != worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, state.cycleValue(POWERED), 3);

                if (worldIn.isBlockPowered(pos)) {
                    dispense((ServerWorld) worldIn, pos);
                    worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(POWERED)) {
            dispense(worldIn, pos);
            worldIn.getPendingBlockTicks().scheduleTick(pos, this, 20);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getWorld().isBlockPowered(context.getPos()))
            context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 0);

        return this.getDefaultState().with(POWERED, context.getWorld().isBlockPowered(context.getPos())).with(FACING, Direction.UP);
    }

    protected IDispenseItemBehavior getBehavior(ItemStack stack) {
        return DISPENSE_BEHAVIOR_REGISTRY.get(stack.getItem());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }
}
