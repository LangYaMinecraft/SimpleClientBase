package cn.langya.module.impl.move;

import cn.langya.event.annotations.EventTarget;
import cn.langya.event.events.EventSlowDown;
import cn.langya.module.Category;
import cn.langya.module.Module;

/**
 * @author LangYa
 * @since 2024/12/28 01:09
 * NoSlow模块用于取消减速效果的实现
 */
public class NoSlow extends Module {
    public NoSlow() {
        super(Category.Move);
    }

    @Override
    public String getSuffix() {
        // 返回模块的后缀名称
        return "Vanilla";
    }

    @EventTarget
    public void onSlowDown(EventSlowDown event) {
        // 当触发减速事件时，取消该事件
        event.setCancelled(true);
    }
}
