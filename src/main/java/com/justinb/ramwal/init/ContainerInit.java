package com.justinb.ramwal.init;

import com.justinb.ramwal.Main;
import com.justinb.ramwal.containers.DeriverContainer;
import com.justinb.ramwal.containers.IntegratorContainer;
import com.justinb.ramwal.containers.LemonPouchContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerInit {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Main.MODID);

    public static RegistryObject<ContainerType<LemonPouchContainer>> LEMONPOUCH = CONTAINERS.register("lemonpouch",
            () -> IForgeContainerType.create(LemonPouchContainer::createContainerClientSide));

    public static RegistryObject<ContainerType<IntegratorContainer>> INTEGRATOR = CONTAINERS.register("integrator",
            () -> IForgeContainerType.create(IntegratorContainer::createContainerClientSide));

    public static RegistryObject<ContainerType<DeriverContainer>> DERIVER = CONTAINERS.register("deriver",
            () -> IForgeContainerType.create(DeriverContainer::createContainerClientSide));
}
