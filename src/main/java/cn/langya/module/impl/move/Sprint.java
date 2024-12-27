package cn.langya.module.impl.move;

import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventUpdate;
import cn.langya.module.Category;
import cn.langya.module.Module;

/**
 * @author LangYa
 * @since 2024/11/16 04:14
 * Sprint模块用于实现游戏中的冲刺功能
 */
public class Sprint extends Module {

    /**
     * 构造函数，初始化Sprint模块并设置其类别为移动类
     */
    public Sprint() {
        super(Category.Move);
    }

    /**
     * 事件处理方法，当更新事件发生时设置冲刺按键被按下
     * @param event 更新事件
     */
    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }
}

