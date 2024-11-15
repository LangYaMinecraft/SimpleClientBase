package cn.langya.value;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LangYa
 * @since 2024/9/1 20:07
 */
@Getter
@Setter
public class Value<T> {
    private String name;
    private T value;
    public boolean isHide = false;

    public Value(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
        onEditValue();
    }

    public void onEditValue() { }
}
