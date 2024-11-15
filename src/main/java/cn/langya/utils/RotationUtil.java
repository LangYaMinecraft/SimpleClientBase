package cn.langya.utils;

import cn.langya.Wrapper;
import cn.langya.event.annotations.EventPriority;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventMotion;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class RotationUtil implements Wrapper {
    public static final RotationUtil INSTANCE = new RotationUtil();
    @Getter
    @Setter
    private static float[] rotations = null;

    public static void setRotations() {
        rotations = null;
    }

    @EventTarget
    // 看我超低优先级害死你们这群在module瞎改转头的
    @EventPriority(value = 114514)
    public void onMotion(EventMotion event) {
        if (rotations != null) event.setRotations(rotations);
    }
    
    public static float[] getRotationsNeeded(Entity entity) {
        if (entity == null) return null;
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }
}
