package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.containers.ProgrammerContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static RegistryObject<ContainerType<ProgrammerContainer>> PROGRAMMER = CONTAINERS.register("programmer",
            () -> IForgeContainerType.create(ProgrammerContainer::createContainerClientSide));
}
