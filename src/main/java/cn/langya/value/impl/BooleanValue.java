package cn.langya.value.impl;

import cn.langya.value.Value;

/**
 * @author LangYa
 * @since 2024/9/1 20:11
 */
public class BooleanValue extends Value<Boolean> {

    public BooleanValue(String name, Boolean value) {
        super(name, value);
    }

    public void toggle() {
        this.setValue(!getValue());
    }
}
