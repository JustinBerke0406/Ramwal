package com.justinb.ramwal.mobs.renderers;

import com.justinb.ramwal.mobs.entities.MimicEntity;
import com.justinb.ramwal.mobs.models.MimicModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class MimicRenderer extends MobRenderer<MimicEntity, MimicModel<MimicEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/zombie/zombie.png");

    public MimicRenderer(EntityRendererManager renderManagerIn, MimicModel<MimicEntity> entityModelIn) {
        super(renderManagerIn, entityModelIn, 0.5f);
    }

    public MimicRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MimicModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(MimicEntity entity) {
        return TEXTURE;
    }
}
