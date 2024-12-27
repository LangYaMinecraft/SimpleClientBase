package cn.langya.command;

import cn.langya.Wrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LangYa
 * @since 2024/11/19 22:34
 */
@AllArgsConstructor
@Getter
public class Command implements Wrapper {
    private final String name;
    private final String runCommand;

    public void run(String[] args) { }
}

