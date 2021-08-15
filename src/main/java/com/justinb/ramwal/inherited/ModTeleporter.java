package com.justinb.ramwal.inherited;

import net.minecraft.block.Blocks;
import net.minecraft.block.PortalInfo;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.logging.Logger;

public class ModTeleporter implements ITeleporter {
    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity)
    {
        return repositionEntity.apply(false);
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
        BlockPos pos = entity.getPosition();
        RegistryKey<World> dimen = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("ramwal:lemondimension"));

        double coeff = (destWorld.getDimensionKey() == dimen) ? (1/13.51) : (13.51);
        double x = pos.getX() * coeff, y = pos.getY(), z = pos.getZ() * coeff;

        BlockPos dim = findValidSpawn(new BlockPos(x, y, z), destWorld, 0);

        if (dim == null)
            dim = new BlockPos(x, y, z);

        return new PortalInfo(new Vector3d(dim.getX() + 0.5, dim.getY(), dim.getZ() + 0.5), Vector3d.ZERO, entity.rotationYaw, entity.rotationPitch);
    }

    private BlockPos findValidSpawn(BlockPos dim, ServerWorld destWorld, long count) {
        if (destWorld.getBlockState(dim.up()).getBlock().canSpawnInBlock()
                && destWorld.getBlockState(dim).getBlock().canSpawnInBlock()
                && destWorld.getBlockState(dim.down()).isSolid()
                && destWorld.canBlockSeeSky(dim)) {
            return dim;
        }
        else {
            if (count == 0) {
                BlockPos up = findValidSpawn(dim.up(), destWorld, 1);
                BlockPos down = findValidSpawn(dim.down(), destWorld, -1);

                if (up != null && down == null) return up;
                if (down != null && up == null) return down;
                if (up == null && down == null) return null;

                return (Math.abs(up.getY() - dim.getY()) > Math.abs(down.getY() - dim.getY())) ? down : up;
            }
            else if (count > 0) {
                if (dim.getY() > destWorld.getHeight()) return null;
                else return findValidSpawn(dim.up(), destWorld, count + 1);
            }
            else {
                if (dim.getY() < 0) return null;
                else return findValidSpawn(dim.down(), destWorld, count - 1);
            }
        }
    }
}
