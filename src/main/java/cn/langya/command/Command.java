package cn.langya.command;

import cn.langya.Wrapper;
import lombok.Getter;

/**
 * @author LangYa
 * @since 2024/11/19 22:34
 */
@Getter
public class Command implements Wrapper {
    private final String name;

    public Command(String name) {
        this.name = name;
    }

    public void run(String[] args) { }
}

