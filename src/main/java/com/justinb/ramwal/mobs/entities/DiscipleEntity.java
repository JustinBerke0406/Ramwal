package com.justinb.ramwal.mobs.entities;

import com.justinb.ramwal.init.SoundInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class DiscipleEntity extends MonsterEntity {
    private static final DataParameter<Integer> attackTimer = EntityDataManager.createKey(DiscipleEntity.class, DataSerializers.VARINT);

    public DiscipleEntity(EntityType<? extends DiscipleEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreFrustumCheck = true;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 8d)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 0.7f)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.4f)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SheepEntity.class, true, false));
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        setAttackTimer(20);

        Logger.getAnonymousLogger().info(String.valueOf(this));

        return super.attackEntityAsMob(entityIn);
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if (getAttackTimer() > 0) setAttackTimer(getAttackTimer() - 1);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.DISCIPLE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.DISCIPLE_HURT.get();
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(attackTimer, 0);
    }

    public void setAttackTimer(int time) {
        this.dataManager.set(attackTimer, time);
    }

    public int getAttackTimer() {
        return this.dataManager.get(attackTimer);
    }
}
