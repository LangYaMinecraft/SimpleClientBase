package cn.langya.utils;

import java.util.function.Consumer;

/**
 * @author cubk, LangYa
 * Utility class for class initialization and checking.
 * 工具类，用于类的初始化和检查。
 */
public class InitializerUtil {

    /**
     * Initializes classes in the specified package and applies the handler to each class.
     * 初始化指定包中的类，并将处理器应用于每个类。
     *
     * @param handler  The handler to process each class.
     * @param inputClazz The class whose package will be processed.
     */
    public static void initialize(Consumer<Class<?>> handler, Class<?> inputClazz) {
        String packagePath = inputClazz.getPackage().getName() + ".";

        // Check if the package exists and process classes in it
        // 检查包是否存在并处理其中的类
        if (ReflectionUtil.dirExist(packagePath)) {
            for (Class<?> clazz : ReflectionUtil.getClassesInPackage(packagePath)) {
                handler.accept(clazz);
            }
        }
    }

    /**
     * Checks if one class can be assigned to another.
     * 检查一个类是否可以被赋值给另一个类。
     *
     * @param inputClazz The input class.
     * @param checkClazz The class to check.
     * @return true if inputClazz can be assigned to checkClazz, false otherwise.
     */
    public static boolean check(Class<?> inputClazz, Class<?> checkClazz) {
        return inputClazz.isAssignableFrom(checkClazz);
    }
}
