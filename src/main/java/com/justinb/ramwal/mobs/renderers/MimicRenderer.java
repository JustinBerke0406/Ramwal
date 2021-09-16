package com.justinb.ramwal.mobs.renderers;

import com.justinb.ramwal.mobs.entities.MimicEntity;
import com.justinb.ramwal.mobs.models.MimicModel;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class MimicRenderer extends BipedRenderer<MimicEntity, MimicModel<MimicEntity>> {
    private static final ResourceLocation DEFAULT = new ResourceLocation("textures/entity/zombie/zombie.png");

    public MimicRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MimicModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(MimicEntity entity) {
        PlayerEntity owner = entity.getOwner();

        if (owner == null) return DEFAULT;
        else return ((AbstractClientPlayerEntity) owner).getLocationSkin();
    }
}
