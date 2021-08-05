package com.justinb.ramwal.network;

import com.justinb.ramwal.init.BlockInit;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SpreadPacket {
    private final int x;
    private final int y;
    private final int z;

    public SpreadPacket(PacketBuffer buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public SpreadPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    void encode(PacketBuffer buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context cont = context.get();
        cont.enqueueWork(
                () -> DistExecutor.safeRunWhenOn(Dist.CLIENT,
                        () -> Handle.handler(cont.getSender().getServerWorld(), new BlockPos(x, y, z))));
        cont.setPacketHandled(true);
    }

    public static class Handle {
        public static DistExecutor.SafeRunnable handler(World world, BlockPos pos) {
            return new DistExecutor.SafeRunnable() {
                @Override
                public void run() {
                    world.setBlockState(pos, BlockInit.GLITCH.get().getDefaultState(), 3);
                }
            };
        }
    }
}
