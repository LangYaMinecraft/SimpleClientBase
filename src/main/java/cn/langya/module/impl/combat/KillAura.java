package cn.langya.module.impl.combat;

import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventMotion;
import cn.langya.event.events.EventUpdate;
import cn.langya.module.Category;
import cn.langya.module.Module;
import cn.langya.utils.RotationUtil;
import cn.langya.utils.TimerUtil;
import cn.langya.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KillAura extends Module {

    public KillAura() {
        super(Category.Combat);
        this.attackTimer = new TimerUtil();
        setKeyCode(Keyboard.KEY_R);
    }

    private final NumberValue cpsValue = new NumberValue("CPS",6,20,1,1);
    private final NumberValue rangeValue = new NumberValue("Range",3,6,1,0.1F);
    
    private final List<EntityLivingBase> targets = new ArrayList<>();
    private EntityLivingBase target;
    private final TimerUtil attackTimer;

    @EventTarget
    public void onUpdate(EventUpdate event) {
        float reach = rangeValue.getValue();

        // getTargets
        for (Entity entity : mc.theWorld.loadedEntityList) {
            // 防止重复添加
            if (!(entity instanceof EntityLivingBase) || targets.contains(target)) continue;
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            if (entityLivingBase == mc.thePlayer || entityLivingBase.getHealth() <= 0 || 
            entityLivingBase.isDead || entityLivingBase.getDistanceToEntity(mc.thePlayer) > reach) continue;
            targets.add(entityLivingBase);
        }
        
        if (targets.isEmpty()) return;
        targets.removeIf(target -> target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > reach);

        if (target != null && (target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > reach)) target = null;
        
        // getTarget
        if (!targets.isEmpty() && target == null) {
            // single
            target = targets.get(0);
        }

        setSuffix(String.valueOf(targets.size()));
    }

    @EventTarget
    public void onMotion(EventMotion event) {
        if (target == null) RotationUtil.setRotations();
        if (event.isPre() && !targets.isEmpty() && target != null) {
            float[] rotations = RotationUtil.getRotationsNeeded(target);
            RotationUtil.setRotations(rotations);
            if (attackTimer.hasReached(1000 / cpsValue.getValue().intValue())) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer,target);
                attackTimer.reset();
            }
        }
    }

    @Override
    public void onDisable() {
        // 你忘记重置转头了
        RotationUtil.setRotations();
        super.onDisable();
    }
}
