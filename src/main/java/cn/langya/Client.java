package cn.langya;

import cn.langya.command.CommandManager;
import cn.langya.config.ConfigManager;
import cn.langya.ui.ElementManager;
import cn.langya.event.EventManager;
import cn.langya.module.ModuleManager;
import cn.langya.value.ValueManager;
import lombok.Getter;
import org.lwjgl.opengl.Display;

/**
 * @author LangYa
 * @since 2024/11/16 03:35
 */
@Getter
public class Client {
    @Getter
    private static final Client instance = new Client();

    public static final String name = "ClientBase";
    public static final String version = "0.1";

    private final EventManager eventManager = new EventManager();
    private ElementManager elementManager;
    private ModuleManager moduleManager;
    private ValueManager valueManager;
    private ConfigManager configManager;
    private CommandManager commandManager;

    public void initClient() {
        Logger.info("Start initializing the client");

        this.elementManager = new ElementManager();
        this.moduleManager = new ModuleManager();
        this.valueManager = new ValueManager();
        this.configManager = new ConfigManager();
        this.commandManager = new CommandManager();

        Display.setTitle(String.format("%s - %s",name,version));
        Logger.info("Client side initialization complete.");
    }

    public void stopClient() {
        this.configManager.saveAllConfigs();
    }
}

