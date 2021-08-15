package com.justinb.ramwal.mobs.entities;

import com.justinb.ramwal.init.SoundInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.lang.reflect.Field;
import java.util.logging.Logger;

public class DiscipleEntity extends MonsterEntity implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);
    private AnimationController controller = new AnimationController(this, "controller", 0, this::predicate);

    public DiscipleEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreFrustumCheck = true;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        /*if (isAggressive()) {
            if (event.isMoving())
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.disciple.chase", true));
            else
                //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.disciple.attack", false));
        }
        else
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.disciple.rest", true));*/

        if (event.isMoving())
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.disciple.chase", true));
        else
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.disciple.rest", true));

        return PlayState.CONTINUE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundInit.DISCIPLE_HURT.get();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 9d)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 0.4f)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.4f)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 200.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true, false));
    }

}
