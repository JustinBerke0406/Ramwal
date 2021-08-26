package com.justinb.ramwal.mobs.models;

import com.justinb.ramwal.mobs.entities.MimicEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.MobEntity;

public class MimicModel<T extends MobEntity> extends BipedModel<T> {
    public MimicModel() {
        super(0, 0, 64,64);
    }
}
