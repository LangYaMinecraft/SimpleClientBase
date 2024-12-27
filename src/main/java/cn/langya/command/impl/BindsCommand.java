package cn.langya.command.impl;

import cn.langya.Client;
import cn.langya.command.Command;
import cn.langya.utils.ChatUtil;

/**
 * @author LangYa
 * @since 2024/12/28 01:16
 */
public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds",".binds");
    }

    @Override
    public void run(String[] args) {
        ChatUtil.info("Commands help:");
        for (Command command : Client.getInstance().getCommandManager().getCommandMap().values()) {
            ChatUtil.info(command.getName() + " - " + command.getRunCommand());
        }
        super.run(args);
    }
}
