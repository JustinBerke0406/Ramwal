package com.justinb.ramwal.network;

import com.justinb.ramwal.init.BlockInit;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
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
    private final int x;
    private final int y;
    private final int z;
    private final String block;

    public SpreadPacket(PacketBuffer buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.block = buf.readString();
    }

    public SpreadPacket(int x, int y, int z, String block) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.block = block;
    }

    void encode(PacketBuffer buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeString(block);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context cont = context.get();
        cont.enqueueWork(
                () -> DistExecutor.safeRunWhenOn(Dist.CLIENT,
                        () -> Handle.handler(cont.getSender().getServerWorld(), new BlockPos(x, y, z), block)));
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
