package cn.langya.value.impl;

import cn.langya.value.Value;

/**
 * @author LangYa
 * @since 2024/9/1 20:12
 */
public class NumberValue extends Value<Float> {

    public float maxValue;
    public float minValue;
    public float incValue;

    public NumberValue(String name, float value, float maxValue, float minValue, float incValue) {
        super(name, value);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.incValue = incValue;
    }

    public NumberValue(String name, int value, int maxValue, int minValue, float incValue) {
        super(name, (float) value);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.incValue = incValue;
    }

    public NumberValue(String name, int value, int maxValue, int minValue, int incValue) {
        super(name, (float) value);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.incValue = incValue;
    }

    public void add() {
        setValue(getValue() + incValue);
    }

    public void cut() {
        setValue(getValue() - incValue);
    }

}
