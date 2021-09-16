package com.justinb.ramwal.util;

import net.minecraft.client.renderer.model.ModelRenderer;

public class Animation {
    public static void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public static float interp(float key, float frame, float angle) {
        return (float) (-Math.cos((key/(frame)) * Math.PI) + 1)/2 * angle;
    }

    public static float interp(float key, float frame, float from, float to) {
        return interp(key, frame, to - from) + from;
    }

    public static float l_interp(float key, float frame, float angle) {
        return (key/(frame) * angle);
    }

    public static float l_interp(float key, float frame, float from, float to) {
        return l_interp(key, frame, to - from) + from;
    }

    public static float degToRad(float deg) {
        return (float) (deg * Math.PI/180);
    }
}
