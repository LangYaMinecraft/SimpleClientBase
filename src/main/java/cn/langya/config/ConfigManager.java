package cn.langya.config;

import cn.langya.utils.InitializerUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import cn.langya.Client;
import cn.langya.Wrapper;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigManager implements Wrapper {
    private final List<Config> configs = new ArrayList<>();
    private final File dir = new File(mc.mcDataDir, Client.name);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private boolean isFirst;

    public ConfigManager() {
        init();
    }

    public void init() {
        if (!dir.exists()) {
            isFirst = true;
            dir.mkdir();
        }

        InitializerUtil.initialize(clazz -> {
            if (!InitializerUtil.check(Config.class,clazz)) return;
            try {
                configs.add(((Class<? extends Config>) clazz).newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }, this.getClass());

        loadAllConfig();
    }

    public void loadConfig(String name) {
        File file = new File(dir, name);
        JsonParser jsonParser = new JsonParser();
        if (file.exists()) {
            System.out.println("Loading config: " + name);
            for (Config config : configs) {
                if (!config.name.equals(name)) continue;
                try {
                    config.loadConfig(jsonParser.parse(new FileReader(file)).getAsJsonObject());
                }
                catch (FileNotFoundException e) {
                    System.out.println("Failed to load config: " + name);
                    e.printStackTrace();
                }
                break;
            }
        } else {
            System.out.println("Config " + name + " doesn't exist, creating a new one...");
            saveConfig(name);
        }
    }

    public void saveConfig(String name) {
        File file = new File(dir, name);
        try {
            System.out.println("Saving config: " + name);
            file.createNewFile();
            for (Config config : configs) {
                if (!config.name.equals(name)) continue;
                FileUtils.writeByteArrayToFile(file, gson.toJson(config.saveConfig()).getBytes(StandardCharsets.UTF_8));
                break;
            }
        }
        catch (IOException e) {
            System.out.println("Failed to save config: " + name);
        }
    }

    public void loadAllConfig() {
        configs.forEach(it -> loadConfig(it.name));
        System.out.println("Loaded all configs");
    }

    public void saveAllConfig() {
        configs.forEach(it -> saveConfig(it.name));
        System.out.println("Saved all configs");
    }
}