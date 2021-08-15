package com.justinb.ramwal.mobs.renderers;

import com.justinb.ramwal.mobs.entities.DiscipleEntity;
import com.justinb.ramwal.mobs.models.DiscipleModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DiscipleRenderer extends GeoEntityRenderer<DiscipleEntity> {
    public DiscipleRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DiscipleModel());
        this.shadowSize = 0.5f;
    }

    @Override
    public ResourceLocation getEntityTexture(DiscipleEntity entity) {
        return this.getGeoModelProvider().getTextureLocation(entity);
    }
}
