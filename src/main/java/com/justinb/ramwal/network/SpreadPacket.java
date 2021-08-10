package com.justinb.ramwal.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Supplier;

public class SpreadPacket {
    private final String block;
    private final BlockPos pos;

    public SpreadPacket(PacketBuffer buf) {
        this.block = buf.readString();
        this.pos = buf.readBlockPos();
    }

    public SpreadPacket(String block, BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }

    void encode(PacketBuffer buf) {
        buf.writeString(block);
        buf.writeBlockPos(pos);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context cont = context.get();
        cont.enqueueWork(
                () -> DistExecutor.safeRunWhenOn(Dist.CLIENT,
                        () -> Handle.handler(cont.getSender().getServerWorld(), pos, block)));
        cont.setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handler(World world, BlockPos pos, String reg) {
            return new DistExecutor.SafeRunnable() {
                @Override
                public void run() {
                    world.setBlockState(pos, ForgeRegistries.BLOCKS.getValue(new ResourceLocation(reg)).getDefaultState(), 3);
                }
            };
        }
    }
}
