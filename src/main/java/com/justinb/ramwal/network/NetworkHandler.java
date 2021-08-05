package com.justinb.ramwal.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;

import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkHandler {
    private static SimpleChannel INSTANCE;
    private static final String PROTOCOL = "1";

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("ramwal", "main"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
        INSTANCE.registerMessage(0, SpreadPacket.class, SpreadPacket::encode, SpreadPacket::new, SpreadPacket::handle);
    }

    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }
}
