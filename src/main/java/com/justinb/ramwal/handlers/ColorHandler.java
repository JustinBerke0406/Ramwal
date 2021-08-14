package com.justinb.ramwal.handlers;

import com.justinb.ramwal.builders.LemonBuilder;
import com.justinb.ramwal.items.AbstractLemonItem;
import net.minecraft.item.Items;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class ColorHandler {
    public static void registerItemColor(ColorHandlerEvent.Item event) {
        event.getItemColors().register(AbstractLemonItem::getItemColor, LemonBuilder.getItems().toArray(new AbstractLemonItem[LemonBuilder.getItems().size()]));
    }
}
