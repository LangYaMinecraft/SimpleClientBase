package cn.langya.utils;

import cn.langya.Wrapper;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventMotion;
import cn.langya.event.events.EventJump;
import cn.langya.event.events.EventPlayerMoveUpdate;
import lombok.Getter;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class RotationUtil implements Wrapper {
    public static final RotationUtil INSTANCE = new RotationUtil();

    @Getter
    private static float[] rotations = null;
    @Getter
    private static float yaw, pitch;
    private static boolean moveFix;

    public static void setRotations() {
        rotations = null;
        moveFix = false;
    }

    public static void setRotations(float[] rotations) {
        setRotations(rotations, true);
    }

    public static void setRotations(float[] rotations, boolean moveFix) {
        RotationUtil.rotations = rotations;
        RotationUtil.moveFix = moveFix;
        yaw = rotations[0];
        pitch = rotations[1];
    }

    @EventTarget
    public void setRotations(EventMotion event) {
        if (rotations != null) {
            event.setRotations(rotations);
        }
    }

    @EventTarget
    public void onPlayerMoveUpdateEvent(EventPlayerMoveUpdate event) {
        if (moveFix) {
            event.setYaw(yaw);
        }
    }

    @EventTarget
    public void onJumpFixEvent(EventJump event) {
        if (moveFix) {
            event.setYaw(yaw);
        }
    }

    public static float[] getRotationsNeeded(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null"); // 总有些傻鸟喜欢传null entity

        double x = entity.posX - mc.thePlayer.posX;
        double y = (entity.posY + entity.getEyeHeight() / 2) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = entity.posZ - mc.thePlayer.posZ;

        double theta = Math.hypot(x, z);
        float yaw = (float) Math.toDegrees(Math.atan2(z, x)) - 90;
        float pitch = (float) -Math.toDegrees(Math.atan2(y, theta));

        float newYaw = mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw);
        float newPitch = mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch);

        return new float[]{newYaw, newPitch};
    }
}
