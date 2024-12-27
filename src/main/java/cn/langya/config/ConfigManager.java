package cn.langya.config;

import cn.langya.Logger;
import cn.langya.utils.InitializerUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cn.langya.Client;
import cn.langya.Wrapper;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LangYa
 * @since 2024/12/28 02:38
 * 配置管理器类，负责加载和保存配置文件
 */
@Getter
public class ConfigManager implements Wrapper {
    private final Map<String, Config> configs = new HashMap<>();
    private final File dir = new File(mc.mcDataDir, Client.name);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser parser = new JsonParser();
    private boolean isFirst;

    /**
     * 构造函数，初始化配置管理器，创建目录并加载配置
     */
    public ConfigManager() {
        if (!dir.exists()) {
            isFirst = dir.mkdir();
        }
        initializeConfigs();
        loadAllConfigs();
    }

    /**
     * 初始化配置，遍历所有配置类并实例化
     */
    private void initializeConfigs() {
        InitializerUtil.initialize(clazz -> {
            if (InitializerUtil.check(Config.class, clazz)) {
                try {
                    Config configInstance = (Config) clazz.getDeclaredConstructor().newInstance();
                    configs.put(configInstance.getName(), configInstance);
                } catch (ReflectiveOperationException e) {
                    Logger.error("Failed to initialize config", e);
                }
            }
        }, this.getClass());
    }

    /**
     * 加载指定名称的配置
     * @param name 配置名称
     */
    public void loadConfig(String name) {
        File file = new File(dir, name + ".json");
        if (file.exists()) {
            Logger.info("Loading config: " + name);
            configs.getOrDefault(name, Config.EMPTY).loadConfig(parseJson(file));
        } else {
            Logger.error("Config " + name + " doesn't exist, creating a new one...");
            saveConfig(name);
        }
    }

    /**
     * 保存指定名称的配置
     * @param name 配置名称
     */
    public void saveConfig(String name) {
        File file = new File(dir, name + ".json");
        try {
            Logger.info("Saving config: " + name);
            FileUtils.writeStringToFile(file, gson.toJson(configs.getOrDefault(name, Config.EMPTY).saveConfig()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Logger.error("Failed to save config: " + name, e);
        }
    }

    /**
     * 加载所有配置
     */
    public void loadAllConfigs() {
        configs.keySet().forEach(this::loadConfig);
        Logger.info("Loaded all configs");
    }

    /**
     * 保存所有配置
     */
    public void saveAllConfigs() {
        configs.keySet().forEach(this::saveConfig);
        Logger.info("Saved all configs");
    }

    /**
     * 解析 JSON 文件
     * @param file JSON 文件
     * @return 解析后的 JsonObject
     */
    private static JsonObject parseJson(File file) {
        try (FileReader reader = new FileReader(file)) {
            return parser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            Logger.error("Failed to parse JSON file: " + file.getName(), e);
            return new JsonObject();
        }
    }
}
