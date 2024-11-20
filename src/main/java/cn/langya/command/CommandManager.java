package cn.langya.command;

import cn.langya.command.impl.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LangYa
 * @since 2024/11/19 22:35
 */
@Getter
public class CommandManager {
    private final Map<String,Command> commandMap;

    public CommandManager() {
        this.commandMap = new HashMap<>();

        init();
    }

    public void addCommand(Command module) {
        this.commandMap.put(module.getName(),module);
    }

    public void init() {
        addCommand(new BindCommand());
        addCommand(new ToggleCommand());
    }

    public boolean runCommand(String message) {
        String[] args = message.split(" ");
        String commandName = args[0].replace(".","");
        for (Command command : commandMap.values()) {
            if (command.getName().equals(commandName)) {
                command.run(args);
                return true;
            }
        }

        return false;
    }
}
