package com.justinb.ramwal.handlers;

import com.justinb.ramwal.builders.CraftableLemonBuilder;
import com.justinb.ramwal.builders.LemonBuilder;
import com.justinb.ramwal.items.AbstractCraftableLemonItem;
import com.justinb.ramwal.items.AbstractLemonItem;
import net.minecraftforge.client.event.ColorHandlerEvent;

public class ColorHandler {
    public static void registerItemColor(ColorHandlerEvent.Item event) {
        event.getItemColors().register(AbstractLemonItem::getItemColor, LemonBuilder.getItems().toArray(new AbstractLemonItem[0]));
        event.getItemColors().register(AbstractCraftableLemonItem::getItemColor, CraftableLemonBuilder.getCraftableItems().toArray(new AbstractCraftableLemonItem[0]));
    }
}
