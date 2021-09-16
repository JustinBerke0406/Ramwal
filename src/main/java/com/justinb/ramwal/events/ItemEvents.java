package com.justinb.ramwal.events;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.items.BatteryItem;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ItemEvents {
    @SubscribeEvent
    public static void onItemCraft(PlayerEvent.ItemCraftedEvent event) {

    }
}
