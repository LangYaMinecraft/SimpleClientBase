package cn.langya;

import cn.langya.command.CommandManager;
import cn.langya.config.ConfigManager;
import cn.langya.ui.ElementManager;
import cn.langya.event.EventManager;
import cn.langya.module.ModuleManager;
import cn.langya.value.ValueManager;
import de.florianmichael.viamcp.ViaMCP;
import lombok.Getter;
import org.lwjgl.opengl.Display;

/**
 * 客户端主类，负责客户端的初始化和管理。
 *
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

    /**
     * 初始化客户端，设置各个管理器并配置显示标题。
     */
    public void initClient() {
        Logger.info("Start initializing the client");

        this.elementManager = new ElementManager();
        this.moduleManager = new ModuleManager();
        this.valueManager = new ValueManager();
        this.configManager = new ConfigManager();
        this.commandManager = new CommandManager();

        initViaMCP();

        Display.setTitle(String.format("%s - %s",name,version));
        Logger.info("Client side initialization complete.");
    }

    private void initViaMCP() {
        try {
            ViaMCP.create();

            // In case you want a version slider like in the Minecraft options, you can use this code here, please choose one of those:

            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
            ViaMCP.INSTANCE.initAsyncSlider(5, 5, 100, 20); // For custom position and size slider
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止客户端，保存所有配置。
     */
    public void stopClient() {
        this.configManager.saveAllConfigs();
    }
}
