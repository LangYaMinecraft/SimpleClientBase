package cn.langya.module.impl.move;

import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventSlowDown;
import cn.langya.module.Category;
import cn.langya.module.Module;

/**
 * @author LangYa
 * @since 2024/12/28 01:09
 */
public class NoSlow extends Module {
    public NoSlow() {
        super(Category.Move);
    }

    @Override
    public String getSuffix() {
        return "Vanilla";
    }

    @EventTarget
    public void onSlowDown(EventSlowDown event) {
        event.setCancelled(true);
    }
}
