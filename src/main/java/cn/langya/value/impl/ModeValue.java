package cn.langya.value.impl;

import cn.langya.value.Value;

/**
 * @author LangYa
 * @since 2024/9/1 20:12
 */
public class ModeValue extends Value<String> {
    public String[] modes;

    public ModeValue(String name,String value, String... modes) {
        super(name, value);
        this.modes = modes;
    }

    public boolean isMode(String value) {
        return this.getValue().equals(value);
    }

    public void setNextValue() {
        setValue(getNextValue());
    }

    public String getNextValue() {
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].equals(this.getValue())) {
                return modes[(i + 1) % modes.length];
            }
        }
        return null;
    }
}
