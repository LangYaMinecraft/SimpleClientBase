package cn.langya.module.impl.move;

import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventUpdate;
import cn.langya.module.Category;
import cn.langya.module.Module;

/**
 * @author LangYa
 * @since 2024/11/16 04:14
 */
public class Sprint extends Module {

    public Sprint() {
        super(Category.Move);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.gameSettings.keyBindSprint.pressed = true;
    }
}
