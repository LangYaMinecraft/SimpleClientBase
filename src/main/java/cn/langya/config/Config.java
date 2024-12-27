package cn.langya.config;

import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public abstract class Config {
    public static final Config EMPTY = new Config("empty") {

        @Override
        public void loadConfig(JsonObject json) {
            // No-op for empty config
        }

        @Override
        public JsonObject saveConfig() {
            return new JsonObject();
        }
    };

    private final String name;
    
    public Config(final String name) {
        this.name = name + ".json";
    }

    public abstract JsonObject saveConfig();
    
    public abstract void loadConfig(JsonObject jsonObject);
}