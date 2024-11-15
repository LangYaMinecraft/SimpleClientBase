package cn.langya.event.events;

import cn.langya.Wrapper;
import cn.langya.event.impl.Event;
import lombok.Getter;
import lombok.Setter;

/**
 * @author LangYa
 * @since 2024/11/16 04:17
 */
@Getter
@Setter
public class EventMotion implements Event, Wrapper {
    private double x,y,z;
    private float yaw,pitch;
    private boolean onGround;
    private boolean pre;

    public EventMotion(double x, double y, double z,float yaw,float pitch,boolean onGround,boolean isPre) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.pre = isPre;
    }

    public EventMotion(boolean isPre) {
        this.pre = isPre;
    }

    public boolean isPost() {
        return !pre;
    }

    public void setRotations(float[] rotations) {
        this.setYaw(rotations[0]);
        this.setPitch(rotations[1]);
        mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationYawHead = yaw;
    }
}

