package com.justinb.ramwal.mobs.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class MimicEntity extends MonsterEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(MimicEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);;

    public MimicEntity(EntityType<? extends MimicEntity> type, World in) {
        super(type, in);
        this.ignoreFrustumCheck = true;
        world = in;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    public void setOwnerUniqueId(UUID uuid) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
    }

    public void setOwner(PlayerEntity owner) {
        if (owner == null) setOwnerUniqueId(null);
        else setOwnerUniqueId(owner.getUniqueID());
    }

    public UUID getOwnerUniqueId() {
        if (!hasOwner()) return null;

        return this.dataManager.get(OWNER_UNIQUE_ID).get();
    }

    public PlayerEntity getOwner() {
        if (!hasOwner()) return null;

        return world.getPlayerByUuid(getOwnerUniqueId());
    }

    public boolean hasOwner() {
        return this.dataManager.get(OWNER_UNIQUE_ID).isPresent();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(MimicEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, MimicEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes()
    {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.5d)
                .createMutableAttribute(Attributes.ATTACK_SPEED, 0.265f)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.4f)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    public void tick() {
        super.tick();

        PlayerEntity closest = world.getClosestPlayer(this, 20);

        setOwner(closest);

        ItemStack held = (closest != null) ? closest.getHeldItemMainhand() : ItemStack.EMPTY;

        setHeldItem(getActiveHand(), held);
    }
}
