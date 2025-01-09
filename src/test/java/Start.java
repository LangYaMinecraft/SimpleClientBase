import java.io.File;
import java.util.Arrays;

import net.minecraft.client.main.Main;

/**
 * Welcome to MCP 1.8.9 for Maven
 * This repository has been created to make working with MCP 1.8.9 easier and cleaner.
 * You can view the MCP 1.8.9 repo here: <a href="https://github.com/Marcelektro/MCP-919">...</a>
 * If you have any questions regarding this, feel free to contact me here: <a href="https://marcloud.net/discord">...</a>
 * <p>
 * Have fun with the MC development ^^
 * Marcelektro
 */

public class Start {
    public static void main(String[] args) {
        // Provide natives
        // Currently supported Linux and Windows
        System.setProperty("org.lwjgl.librarypath", new File("../test_natives/" + (System.getProperty("os.name").startsWith("Windows") ? "windows" : "linux")).getAbsolutePath());

        Main.main(concat(new String[]{"--version", "MavenMCP", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.8", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
