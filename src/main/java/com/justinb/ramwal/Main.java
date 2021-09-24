package com.justinb.ramwal;

import com.justinb.ramwal.events.ItemEvents;
import com.justinb.ramwal.events.RegistrationEvents;
import com.justinb.ramwal.handlers.ColorHandler;
import com.justinb.ramwal.inherited.misc.ModSpawnEggItem;
import com.justinb.ramwal.init.*;
import com.justinb.ramwal.mobs.renderers.DiscipleRenderer;
import com.justinb.ramwal.mobs.renderers.MimicRenderer;
import com.justinb.ramwal.network.NetworkHandler;
import com.justinb.ramwal.recipes.Recipes;
import com.justinb.ramwal.rendering.SugarRushRender;
import com.justinb.ramwal.screencontainer.DeriverScreenContainer;
import com.justinb.ramwal.screencontainer.IntegratorScreenContainer;
import com.justinb.ramwal.screencontainer.LemonPouchScreenContainer;
import com.justinb.ramwal.structures.ModConfiguredStructures;
import com.justinb.ramwal.structures.ModStructures;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    public static final String MODID = "ramwal";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static IStructurePieceType LEMONTREASUREPIECE = null;


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

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

        // The comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

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
        //StructureInit.STRUCTURES.register(bus);

        NetworkHandler.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        BrewingRecipeRegistry.addRecipe(
                Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.SWIFTNESS)),
                Ingredient.fromItems(LemonInit.PINK_LEMON.get()),
                PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionInit.SUGARRUSH.get()));

        Recipes.registerAll();

        DeferredWorkQueue.runLater(() -> {
            Iterator<Biome> biomes = ForgeRegistries.BIOMES.iterator();
            biomes.forEachRemaining((biome) -> {
                biome.getGenerationSettings().getStructures().add(() -> (ModConfiguredStructures.LEMONTREASURE));
            });
        });

        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ContainerInit.INTEGRATOR.get(), IntegratorScreenContainer::new);
        ScreenManager.registerFactory(ContainerInit.LEMONPOUCH.get(), LemonPouchScreenContainer::new);
        ScreenManager.registerFactory(ContainerInit.DERIVER.get(), DeriverScreenContainer::new);

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

    @Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class WorldGenRegistryEvents {
        @SubscribeEvent
        public static void registerStructures(RegistryEvent.Register<Structure<?>> event) {
            ModStructures.registerStructures(event);
            ModConfiguredStructures.registerConfiguredStructures();
        }
    }

    /*@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBiomeLoad(BiomeLoadingEvent event) {
        event.getGeneration().withStructure(ModConfiguredStructures.LEMONTREASURE);
    }*/

    // Registration helper
    public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey)
    {
        entry.setRegistryName(new ResourceLocation(Main.MODID, registryKey));
        registry.register(entry);
        return entry;
    }

    public void biomeModification(final BiomeLoadingEvent event) {
        /*
         * Add our structure to all biomes including other modded biomes.
         * You can skip or add only to certain biomes based on stuff like biome category,
         * temperature, scale, precipitation, mod id, etc. All kinds of options!
         *
         * You can even use the BiomeDictionary as well! To use BiomeDictionary, do
         * RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()) to get the biome's
         * registrykey. Then that can be fed into the dictionary to get the biome's types.
         */

        String cat = event.getCategory().getName();

        if (cat.equals("plains") || cat.equals("forest") || cat.equals("savanna"))
            event.getGeneration().getStructures().add(() -> ModConfiguredStructures.LEMONTREASURE);
    }

    /**
     * Will go into the world's chunkgenerator and manually add our structure spacing.
     * If the spacing is not added, the structure doesn't spawn.
     *
     * Use this for dimension blacklists for your structure.
     * (Don't forget to attempt to remove your structure too from the map if you are blacklisting that dimension!)
     * (It might have your structure in it already.)
     *
     * Basically use this to make absolutely sure the chunkgenerator can or cannot spawn your structure.
     */
    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            /*
             * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
             * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
             * This here is done with reflection as this tutorial is not about setting up and using Mixins.
             * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
             */
            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                java.util.logging.Logger.getAnonymousLogger().info("Was unable to check if " + serverWorld.getDimensionType() + " is using Terraforged's ChunkGenerator.");
            }

            /*
             * Prevent spawning our structure in Vanilla's superflat world as
             * people seem to want their superflat worlds free of modded structures.
             * Also that vanilla superflat is really tricky and buggy to work with in my experience.
             */
            if(serverWorld.getChunkProvider().generator instanceof FlatChunkGenerator &&
                    serverWorld.getDimensionKey().equals(World.OVERWORLD)){
                return;
            }

            /*
             * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
             * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
             *
             * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
             * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
             * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
             */
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_);
            tempMap.putIfAbsent(ModStructures.LEMONTREASURE, DimensionStructuresSettings.field_236191_b_.get(ModStructures.LEMONTREASURE));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}
