package cn.langya.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author cubk1, LangYa466
 * 工具类，用于初始化和检查类
 */
public class InitializerUtil {
   /**
    * 初始化方法，接受一个处理器和输入类，遍历类的路径并应用处理器。
    *
    * @param handler 处理类的消费者
    * @param inputClazz 输入的类
    */
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

    /**
     * 检查一个类是否可以被另一个类赋值。
     *
     * @param inputClazz 输入的类
     * @param checkClazz 需要检查的类
     * @return 如果可以赋值，返回 true；否则返回 false
     */
    public static boolean check(Class<?> inputClazz,Class<?> checkClazz) {
       return inputClazz.isAssignableFrom(checkClazz);
    }
}
