package com.justinb.ramwal;

import com.justinb.ramwal.containers.LemonPouchContainer;
import com.justinb.ramwal.events.ItemEvents;
import com.justinb.ramwal.events.RegistrationEvents;
import com.justinb.ramwal.inherited.misc.ModSpawnEggItem;
import com.justinb.ramwal.handlers.ColorHandler;
import com.justinb.ramwal.init.*;
import com.justinb.ramwal.mobs.renderers.DiscipleRenderer;
import com.justinb.ramwal.mobs.renderers.MimicRenderer;
import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.recipes.Recipes;
import com.justinb.ramwal.rendering.SugarRushRender;
import com.justinb.ramwal.screencontainer.IntegratorScreenContainer;
import com.justinb.ramwal.screencontainer.LemonPouchScreenContainer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "ramwal";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public Main() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        bus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        bus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        bus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        bus.addListener(this::doClientStuff);

        bus.addListener(ColorHandler::registerItemColor);

        // Register ourselves for the server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SugarRushRender.class);
        MinecraftForge.EVENT_BUS.register(ModSpawnEggItem.class);
        MinecraftForge.EVENT_BUS.register(RegistrationEvents.class);
        MinecraftForge.EVENT_BUS.register(ItemEvents.class);

        //Register mod stuff
        SoundInit.SOUNDS.register(bus);
        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        LemonInit.ITEMS.register(bus);
        EffectInit.EFFECTS.register(bus);
        PotionInit.POTIONS.register(bus);
        LootModifierInit.SERIALIZERS.register(bus);
        EntityInit.ENTITIES.register(bus);
        ContainerInit.CONTAINERS.register(bus);
        TileEntityInit.TILE_ENTITIES.register(bus);

        NetworkHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(
                Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.SWIFTNESS)),
                Ingredient.fromItems(LemonInit.PINK_LEMON.get()),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionInit.SUGARRUSH.get()));

        Recipes.registerAll();

        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerInit.INTEGRATOR.get(), IntegratorScreenContainer::new);
        ScreenManager.registerFactory(ContainerInit.LEMONPOUCH.get(), LemonPouchScreenContainer::new);

        RenderingRegistry.registerEntityRenderingHandler(EntityInit.DISCIPLE.get(), DiscipleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.MIMIC.get(), MimicRenderer::new);

        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
