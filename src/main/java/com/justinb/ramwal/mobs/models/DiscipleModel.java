package com.justinb.ramwal.mobs.models;

import com.justinb.ramwal.mobs.entities.DiscipleEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DiscipleModel extends AnimatedGeoModel<DiscipleEntity> {

    @Override
    public ResourceLocation getModelLocation(DiscipleEntity object) {
        return new ResourceLocation("ramwal:geo/disciplemodel.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DiscipleEntity object) {
        return new ResourceLocation("ramwal:textures/entities/disciple.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DiscipleEntity animatable) {
        return new ResourceLocation("ramwal:animations/disciple.animation.json");
    }
}
