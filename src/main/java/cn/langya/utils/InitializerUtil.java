package cn.langya.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author cubk1, LangYa466
 */
public class InitializerUtil {
   public static void initialize(Consumer<Class<?>> handler, Class<?> inputClazz){
        List<String> paths = new ArrayList<>();

        paths.add(inputClazz.getName().replace("." + inputClazz.getSimpleName(),"."));

        for (String path : paths) {
            if (!ReflectionUtil.dirExist(path)) continue;
            Class<?>[] classes = ReflectionUtil.getClassesInPackage(path);
            for (Class<?> clazz : classes) {
                handler.accept(clazz);
            }
            break;
        }
    }

    public static boolean check(Class<?> inputClazz,Class<?> checkClazz) {
       return inputClazz.isAssignableFrom(checkClazz);
    }
}