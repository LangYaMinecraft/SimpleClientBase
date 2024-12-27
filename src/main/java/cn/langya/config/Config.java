package cn.langya.config;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public abstract class Config {
    private final String name;
    
    public Config(final String name) {
        this.name = name + ".json";
    }

    public abstract JsonObject saveConfig();
    
    public abstract void loadConfig(JsonObject jsonObject);
}