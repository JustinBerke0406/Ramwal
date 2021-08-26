package com.justinb.ramwal.mobs.models;

import com.google.common.collect.ImmutableList;
import com.justinb.ramwal.mobs.entities.DiscipleEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.logging.Logger;

import static net.minecraft.entity.Entity.horizontalMag;

public class DiscipleModel<T extends DiscipleEntity> extends SegmentedModel<T> {
    private ModelRenderer all;
    private ModelRenderer spine;
    private ModelRenderer body;
    private ModelRenderer bone;
    private ModelRenderer rightArm;
    private ModelRenderer bone3;
    private ModelRenderer leftArm;
    private ModelRenderer bone2;
    private ModelRenderer head;
    private ModelRenderer leftLeg;
    private ModelRenderer bone4;
    private ModelRenderer rightLeg;
    private ModelRenderer bone5;

    private int lastAnim = -1;

    public DiscipleModel() {

        this.textureWidth = 64;
        this.textureHeight = 32;

        this.all = new ModelRenderer(this);
        this.all.setRotationPoint(0.0F, 24.0F, 0.0F);
        setRotationAngle(all, -3.1416F, 0.0F, 3.1416F);


        this.spine = new ModelRenderer(this);
        this.spine.setRotationPoint(0.0F, -30.0F, 0.0F);
        this.all.addChild(spine);


        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.spine.addChild(body);
        this.body.setTextureOffset(32, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

        this.bone = new ModelRenderer(this);
        this.bone.setRotationPoint(0.0F, -12.0F, 0.0F);
        this.body.addChild(bone);
        this.bone.setTextureOffset(32, 16).addBox(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);

        this.rightArm = new ModelRenderer(this);
        this.rightArm.setRotationPoint(-5.0F, -10.0F, 0.0F);
        this.bone.addChild(rightArm);
        this.rightArm.setTextureOffset(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F, 0.0F, true);

        this.bone3 = new ModelRenderer(this);
        this.bone3.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.rightArm.addChild(bone3);
        this.bone3.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, true);

        this.leftArm = new ModelRenderer(this);
        this.leftArm.setRotationPoint(5.0F, -10.0F, 0.0F);
        this.bone.addChild(leftArm);
        this.leftArm.setTextureOffset(56, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 14.0F, 2.0F, 0.0F, true);

        this.bone2 = new ModelRenderer(this);
        this.bone2.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.leftArm.addChild(bone2);
        this.bone2.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 17.0F, 2.0F, 0.0F, true);

        this.head = new ModelRenderer(this);
        this.head.setRotationPoint(0.0F, -13.0F, 0.0F);
        this.bone.addChild(head);
        this.head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F, false);
        this.head.setTextureOffset(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, -0.5F, false);

        this.leftLeg = new ModelRenderer(this);
        this.leftLeg.setRotationPoint(2.0F, -30.0F, 0.0F);
        this.all.addChild(leftLeg);
        this.leftLeg.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, 0.0F, true);

        this.bone4 = new ModelRenderer(this);
        this.bone4.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.leftLeg.addChild(bone4);
        this.bone4.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, 0.0F, true);

        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.setRotationPoint(-2.0F, -30.0F, 0.0F);
        this.all.addChild(rightLeg);
        this.rightLeg.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, 0.0F, false);

        this.bone5 = new ModelRenderer(this);
        this.bone5.setRotationPoint(0.0F, 15.0F, 0.0F);
        this.rightLeg.addChild(bone5);
        this.bone5.setTextureOffset(56, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 15.0F, 2.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.all);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //Logger.getAnonymousLogger().info("Not sure:    " + entityIn.toString());

        if (entityIn.getAttackTimer() > 0) {
            float key = 20 - entityIn.getAttackTimer();

            resetAllAngles();

            this.head.rotateAngleX = degToRad(-20);
            this.bone2.rotateAngleX = degToRad(12.5f);
            this.bone3.rotateAngleX = degToRad(12.5f);
            this.bone.rotateAngleX = degToRad(-10);

            if (key < 10) {
                this.bone.rotateAngleY = degToRad(interp(key, 10, 0, -20));
                this.rightArm.rotateAngleX = degToRad(interp(key, 10, 0, -75));
                this.rightArm.rotateAngleY = degToRad(l_interp(key, 10, 0, 32.5f));
                this.bone3.rotateAngleY = degToRad(interp(key, 10, 0, -82.5f));
                this.bone3.rotateAngleZ = degToRad(interp(key, 10, 0, 80));
                this.head.rotateAngleY = degToRad(interp(key, 10, 0, 20));
            }
            else {
                key -= 10;

                if (key < 5) {
                    this.bone.rotateAngleY = degToRad(interp(key, 5, -20, 0));
                    this.rightArm.rotateAngleX = degToRad(interp(key, 5, -75, -75));
                    this.rightArm.rotateAngleY = degToRad(l_interp(key, 5, 32.5f, 175));
                    this.bone3.rotateAngleY = degToRad(interp(key, 5, -82.5f, -82.5f));
                    this.bone3.rotateAngleZ = degToRad(interp(key, 5, 80, 0));
                    this.head.rotateAngleY = degToRad(interp(key, 10, 20, 0));
                }
                else {
                    key -= 5;

                    this.rightArm.rotateAngleX = degToRad(interp(key, 5, -75, 0));
                    this.rightArm.rotateAngleY = degToRad(l_interp(key, 5, 175, 0));
                    this.bone3.rotateAngleY = degToRad(interp(key, 5, -82.5f, 0));
                }
            }

            this.lastAnim = 2;
        }
        else if (horizontalMag(entityIn.getMotion()) > 0) {
            float key = ageInTicks % 20;

            resetAllAngles();

            this.spine.rotateAngleX = degToRad(-7.5f);
            this.bone.rotateAngleX = degToRad(-5);

            if (key < 10) {
                this.rightArm.rotateAngleX = degToRad(interp(key, 10, -17.5f, 20));
                this.leftArm.rotateAngleX = degToRad(interp(key, 10, 20, -17.5f));
                this.bone3.rotateAngleX = degToRad(interp(key, 10, 25, 72.5f));
                this.bone2.rotateAngleX = degToRad(interp(key, 10, 72.5f, 25));

                if (key < 5) {
                    this.leftLeg.rotateAngleX = degToRad(l_interp(key, 5, -12.5f, 42.5f));
                    this.rightLeg.rotateAngleX = degToRad(l_interp(key, 5, 15, -5));
                    this.head.rotateAngleX = degToRad(interp(key, 5, 0, 2));

                    if (key < 2) {
                        this.bone5.rotateAngleX = degToRad(l_interp(key, 2, -5, -10));
                        this.bone4.rotateAngleX = degToRad(l_interp(key, 2, -27.5f, -38.75f));
                    }
                    else {
                        key -= 2;

                        this.bone5.rotateAngleX = degToRad(l_interp(key, 3, -10, -5));
                        this.bone4.rotateAngleX = degToRad(l_interp(key, 3, -38.75f, -45));
                    }
                }
                else {
                    key -= 5;

                    this.leftLeg.rotateAngleX = degToRad(l_interp(key, 5, 42.5f, 15));
                    this.rightLeg.rotateAngleX = degToRad(l_interp(key, 5, -5, -12.5f));
                    this.bone4.rotateAngleX = degToRad(l_interp(key, 5, -45, -5));
                    this.head.rotateAngleX = degToRad(interp(key, 5, 2, 0));

                    if (key < 3) {
                        this.bone5.rotateAngleX = degToRad(l_interp(key, 3, -5, -13.75f));
                    }
                    else {
                        key -= 3;

                        this.bone5.rotateAngleX = degToRad(l_interp(key, 2, -13.75f, -27.5f));
                    }
                }
            }
            else {
                key -= 10;

                this.rightArm.rotateAngleX = degToRad(interp(key, 10, 20, -17.5f));
                this.leftArm.rotateAngleX = degToRad(interp(key, 10, -17.5f, 20));
                this.bone3.rotateAngleX = degToRad(interp(key, 10, 72.5f, 25));
                this.bone2.rotateAngleX = degToRad(interp(key, 10, 25, 72.5f));

                if (key < 5) {
                    this.leftLeg.rotateAngleX = degToRad(l_interp(key, 5, 15f, -5));
                    this.rightLeg.rotateAngleX = degToRad(l_interp(key, 5, -12.5f, 42.5f));
                    this.bone4.rotateAngleX = degToRad(l_interp(key, 5, -5f, -5));
                    this.head.rotateAngleX = degToRad(interp(key, 5, 0, 2));

                    if (key < 2) {
                        this.bone5.rotateAngleX = degToRad(l_interp(key, 2, -27.5f, -13.75f));
                    }
                    else {
                        key -= 2;

                        this.bone5.rotateAngleX = degToRad(l_interp(key, 3, -13.75f, -45));
                    }
                }
                else {
                    key -= 5;

                    this.leftLeg.rotateAngleX = degToRad(l_interp(key, 5, -5, -12.5f));
                    this.rightLeg.rotateAngleX = degToRad(l_interp(key, 5, 42.5f, 15));
                    this.head.rotateAngleX = degToRad(interp(key, 5, 2, 0));

                    if (key < 3) {
                        this.bone5.rotateAngleX = degToRad(l_interp(key, 3, -45, -10));
                        this.bone4.rotateAngleX = degToRad(l_interp(key, 3, -5f, -38.75f));
                    }
                    else {
                        key -= 3;

                        this.bone5.rotateAngleX = degToRad(l_interp(key, 2, -10, -5));
                        this.bone4.rotateAngleX = degToRad(l_interp(key, 2, -38.75f, -27.5f));
                    }
                }
            }

            this.lastAnim = 1;
        }
        else {
            float key = 0;

            resetAllAngles();

            if (this.lastAnim != 0) {
                key += Math.random() * 20;
            }

            key = (key + ageInTicks) % 40;

            this.rightLeg.rotateAngleX = 0;
            this.leftLeg.rotateAngleX = 0;
            this.bone4.rotateAngleX = 0;
            this.bone5.rotateAngleX = 0;

            if (key < 20) {
                this.spine.rotateAngleX = degToRad(interp(key, 20, -12.5f, -15f));
                this.rightArm.rotateAngleX = degToRad(interp(key, 20, 0, 5));
                this.leftArm.rotateAngleX = degToRad(interp(key, 20, 5, 7.5f));
                this.bone2.rotateAngleX = degToRad(interp(key, 20, 25, 30));
                this.bone3.rotateAngleX = degToRad(interp(key, 20, 22.5f, 25));
                this.head.rotateAngleX = degToRad(interp(key, 20, -10, -20));
            }
            else {
                key %= 20;

                this.spine.rotateAngleX = degToRad(interp(key, 20, -15f, -12.5f));
                this.rightArm.rotateAngleX = degToRad(interp(key, 20, 5, 0));
                this.leftArm.rotateAngleX = degToRad(interp(key, 20, 7.5f, 5));
                this.bone2.rotateAngleX = degToRad(interp(key, 20, 30, 25));
                this.bone3.rotateAngleX = degToRad(interp(key, 20, 25, 22.5f));
                this.head.rotateAngleX = degToRad(interp(key, 20, -20, -10));
            }

            this.bone.rotateAngleX = degToRad(-10);

            this.lastAnim = 0;
        }

        head.rotateAngleY = degToRad(5 * entityIn.getAttackTimer());
    }

    public void resetAllAngles() {
        setRotationAngle(all, -3.1416F, 0.0F, 3.1416F);
        setRotationAngle(spine, 0, 0, 0);
        setRotationAngle(body, 0, 0, 0);
        setRotationAngle(bone, 0, 0, 0);
        setRotationAngle(rightArm, 0, 0, 0);
        setRotationAngle(leftArm, 0, 0, 0);
        setRotationAngle(bone2, 0, 0, 0);
        setRotationAngle(bone3, 0, 0, 0);
        setRotationAngle(bone4, 0, 0, 0);
        setRotationAngle(bone5, 0, 0, 0);
        setRotationAngle(leftLeg, 0, 0, 0);
        setRotationAngle(rightLeg, 0, 0, 0);
        setRotationAngle(head, 0, 0, 0);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    private float interp(float key, float frame, float angle) {
        return (float) (-Math.cos((key/(frame)) * Math.PI) + 1)/2 * angle;
    }

    private float interp(float key, float frame, float from, float to) {
        return interp(key, frame, to - from) + from;
    }

    private float l_interp(float key, float frame, float angle) {
        return (key/(frame) * angle);
    }

    private float l_interp(float key, float frame, float from, float to) {
        return l_interp(key, frame, to - from) + from;
    }

    private float degToRad(float deg) {
        return (float) (deg * Math.PI/180);
    }
}
