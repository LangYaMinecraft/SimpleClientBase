package cn.langya.command;

import cn.langya.Logger;
import cn.langya.utils.InitializerUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令管理器类，负责管理和执行命令
 * @author LangYa
 * @since 2024/11/19 22:35
 */
@Getter
public class CommandManager {
    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * 构造函数，初始化命令管理器
     */
    public CommandManager() {
        init();
    }

    /**
     * 添加命令到命令映射表
     * @param command 要添加的命令
     */
    public void addCommand(Command command) {
        commandMap.put(command.getName(), command);
    }

    /**
     * 初始化命令管理器，加载所有命令
     */
    public void init() {
        InitializerUtil.initialize(clazz -> {
            if (InitializerUtil.check(Command.class, clazz)) {
                try {
                    addCommand((Command) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    Logger.error("Failed to initialize command: ",e);
                }
            }
        }, this.getClass());
    }

    /**
     * 执行指定的命令
     * @param message 包含命令的消息
     * @return true 如果命令成功执行，false 如果命令不存在
     */
    public boolean runCommand(String message) {
        String commandName = message.split(" ")[0].replace(".", "");
        Command command = commandMap.get(commandName);
        if (command != null) {
            command.run(message.split(" "));
            return true;
        }
        return false;
    }
}
