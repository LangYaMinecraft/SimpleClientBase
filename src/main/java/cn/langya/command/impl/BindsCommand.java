package cn.langya.command.impl;

import cn.langya.Client;
import cn.langya.command.Command;
import cn.langya.utils.ChatUtil;

/**
 * @author LangYa
 * @since 2024/12/28 01:16
 *
 * BindsCommand类用于处理与绑定命令相关的操作。
 */
public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds",".binds");
    }

    @Override
    public void run(String[] args) {
        // 打印命令帮助信息
        ChatUtil.info("Commands help:");
        // 遍历所有命令并打印其执行命令
        Client.getInstance().getCommandManager().getCommandMap().values().forEach(command ->  ChatUtil.info(command.getRunCommand()));
        super.run(args);
    }
}
