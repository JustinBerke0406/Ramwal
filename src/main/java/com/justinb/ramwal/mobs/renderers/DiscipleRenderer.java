package com.justinb.ramwal.mobs.renderers;

import com.justinb.ramwal.mobs.entities.DiscipleEntity;
import com.justinb.ramwal.mobs.models.DiscipleModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DiscipleRenderer extends MobRenderer<DiscipleEntity, DiscipleModel<DiscipleEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("ramwal:textures/entities/disciple.png");

    public DiscipleRenderer(EntityRendererManager renderManager) {
        super(renderManager, new DiscipleModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(DiscipleEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(DiscipleEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
