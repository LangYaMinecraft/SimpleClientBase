package cn.langya.command;

import cn.langya.utils.InitializerUtil;
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

    public void addCommand(Class<? extends Command> command) throws InstantiationException, IllegalAccessException {
        this.commandMap.put(command.getSimpleName(), command.newInstance());
    }

    public void init() {
        InitializerUtil.initialize(clazz -> {
            if (!InitializerUtil.check(Command.class,clazz)) return;
            try {
                addCommand((Class<? extends Command>) clazz);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }, this.getClass());
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
