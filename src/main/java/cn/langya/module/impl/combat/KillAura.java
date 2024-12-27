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

import java.util.List;
import java.util.stream.Collectors;

// 杀戮光环模块类
@Getter
@Setter
public class KillAura extends Module {

    private final NumberValue cpsValue = new NumberValue("CPS", 6, 20, 1, 1); // 每秒攻击次数设定
    private final NumberValue rangeValue = new NumberValue("Range", 3, 6, 1, 0.1F); // 攻击范围设定
    private final TimerUtil attackTimer = new TimerUtil(); // 攻击计时器
    private EntityLivingBase target; // 当前攻击目标

    // 构造函数，设置模块类别和快捷键
    public KillAura() {
        super(Category.Combat);
        setKeyCode(Keyboard.KEY_R);
    }

    // 更新事件处理方法
    @EventTarget
    public void onUpdate(EventUpdate event) {
        float reach = rangeValue.getValue();
        List<EntityLivingBase> validTargets = mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .map(entity -> (EntityLivingBase) entity)
                .filter(entity -> entity != mc.thePlayer && entity.getHealth() > 0 && !entity.isDead)
                .filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= reach)
                .collect(Collectors.toList());

        // 选择目标
        if (!validTargets.isEmpty()) {
            target = target == null || target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > reach
                    ? validTargets.get(0) : target;
        }
    }

    // 动作事件处理方法
    @EventTarget
    public void onMotion(EventMotion event) {
        if (target == null) {
            RotationUtil.setRotations();
            return;
        }

        if (event.isPre() && attackTimer.hasReached(1000 / cpsValue.getValue().intValue())) {
            RotationUtil.setRotations(RotationUtil.getRotationsNeeded(target));
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, target);
            attackTimer.reset();
        }
    }

    @Override
    public void onDisable() {
        RotationUtil.setRotations(); // 重置转头
        super.onDisable();
    }

    @Override
    public String getSuffix() {
        return String.valueOf(target != null ? 1 : 0); // 返回当前是否有目标
    }
}
