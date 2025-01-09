package cn.langya.module.impl.combat;

import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventMotion;
import cn.langya.event.events.EventUpdate;
import cn.langya.module.Category;
import cn.langya.module.Module;
import cn.langya.utils.RotationUtil;
import cn.langya.utils.TimerUtil;
import cn.langya.value.impl.BooleanValue;
import cn.langya.value.impl.ModeValue;
import cn.langya.value.impl.NumberValue;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LangYa466
 * @since 2025/1/9
 */
@Getter
@Setter
public class KillAura extends Module {
    private final NumberValue maxCPSValue = new NumberValue("Max CPS", 6, 20, 1, 1); // 最大CPS设定
    private final NumberValue minCPSValue = new NumberValue("Min CPS", 6, maxCPSValue.getValue(), 1, 1); // 最小CPS设定
    private final ModeValue getCPSModeValue = new ModeValue("Get CPS Mode", "Random", "Increasing", "Decreasing", "Random"); // 获取CPS模式设定
    private final NumberValue scanRangeValue = new NumberValue("Scan Range", 4, 6, 1, 0.1F); // 寻找目标范围设定
    private final NumberValue attackRangeValue = new NumberValue("Attack Range", 3, scanRangeValue.getValue(), 1, 0.1F); // 攻击范围设定
    private final ModeValue targetsSortModeValue = new ModeValue("Targets Sort Mode", "Distance", "Distance", "Health"); // 目标排序模式设定
    private final ModeValue setTargetModeValue = new ModeValue("Set Target Mode", "Single", "Single", "Switch"); // 目标模式设定
    private final BooleanValue moveFixValue = new BooleanValue("Move Fix", true); // 移动修正设定
    private final TimerUtil attackTimer = new TimerUtil(); // 攻击计时器
    private final TimerUtil switchTimer = new TimerUtil(); // 切换目标计时器
    private EntityLivingBase target; // 当前攻击目标
    private int currentCPS;

    private int targetIndex = 0; // 当前目标索引，用于切换目标模式

    // 构造函数，设置模块类别和快捷键
    public KillAura() {
        super(Category.Combat);
        setKeyCode(Keyboard.KEY_R);
    }

    // 动作事件处理方法
    @EventTarget
    public void onMotion(EventMotion event) {
        // 更新目标
        getTargets();
        if (target == null) {
            RotationUtil.setRotations();
            return;
        }

        if (event.isPre() && attackTimer.hasReached(1000 / getCPS())) {
            RotationUtil.setRotations(RotationUtil.getRotationsNeeded(target), moveFixValue.getValue());
            mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, target);
            attackTimer.reset();
        }
    }

    private int getCPS() {
        int min =  minCPSValue.getValue().intValue();
        int max = maxCPSValue.getValue().intValue();
        int range = max - min;

        if (range <= 0) return min; // 确保范围有效

        switch (getCPSModeValue.getValue()) {
            case "Random":
                return (int) (Math.random() * (range + 1) + min);

            case "Increasing":
                // 每次调用递增，达到最大值后重置为最小值
                currentCPS = (currentCPS >= max) ? min : currentCPS + (range / 10);
                return currentCPS;

            case "Decreasing":
                // 每次调用递减，达到最小值后重置为最大值
                currentCPS = (currentCPS <= min) ? max : currentCPS - (range / 10);
                return currentCPS;

            default:
                return min; // 默认返回最小值
        }
    }

    private void getTargets() {
        List<EntityLivingBase> validTargets = mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .map(entity -> (EntityLivingBase) entity)
                .filter(entity -> entity != mc.thePlayer && entity.getHealth() > 0 && !entity.isDead)
                .filter(entity -> entity.getDistanceToEntity(mc.thePlayer) <= scanRangeValue.getValue())
                .sorted(getTargetComparator()) // 根据排序模式排序
                .collect(Collectors.toList());

        float reach = attackRangeValue.getValue();
        // 根据目标模式选择目标
        if (!validTargets.isEmpty()) {
            if ("Single".equalsIgnoreCase(setTargetModeValue.getValue())) {
                target = target == null || target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > reach
                        ? validTargets.get(0) : target;
            } else if ("Switch".equalsIgnoreCase(setTargetModeValue.getValue())) {
                if (target == null || target.getHealth() <= 0 || target.getDistanceToEntity(mc.thePlayer) > reach
                        || switchTimer.hasReached(1000)) {
                    targetIndex = (targetIndex + 1) % validTargets.size();
                    target = validTargets.get(targetIndex);
                    switchTimer.reset();
                }
            }
        } else {
            target = null;
        }
    }

    private Comparator<EntityLivingBase> getTargetComparator() {
        switch (targetsSortModeValue.getValue()) {
            case "Health":
                return Comparator.comparingDouble(EntityLivingBase::getHealth);
            case "Distance":
            default:
                return Comparator.comparingDouble(entity -> entity.getDistanceToEntity(mc.thePlayer));
        }
    }

    @Override
    public void onDisable() {
        RotationUtil.setRotations(); // 重置转头
        super.onDisable();
    }

    @Override
    public String getSuffix() {
        return String.valueOf(target != null ? target.getName() : null); // 返回当前是否有目标
    }
}
