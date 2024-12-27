package cn.langya.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author cubk1
 * 工具类，用于反射获取指定包中的类信息
 */
public class ReflectionUtil {
    /**
     * 根据包名获取该包下的所有类
     * @param packageName 包名
     * @return 类的数组
     */
    public static Class<?>[] getClassesInPackage(String packageName) {
        try {
            Set<String> classNames = getClassNamesFromJarFile(Paths.get(ReflectionUtil.path()).toFile());
            List<Class<?>> classes = new ArrayList<>();

            for (String className : classNames) {
                try {
                    if (className.startsWith(packageName)) {
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                } catch (NoClassDefFoundError | ClassNotFoundException |
                         UnsupportedClassVersionError ignored) {
                }
            }

            return classes.toArray(new Class[0]);
        } catch (Exception exception) {

            File directory = getPackageDirectory(packageName);

            if (!directory.exists()) {
                throw new IllegalArgumentException("Could not get directory resource for package " + packageName);
            }

            return getClassesInPackage(packageName, directory);
        }
    }

    /**
     * 从指定的JAR文件中获取类名
     * @param givenFile JAR文件
     * @return 类名的集合
     * @throws IOException 文件读取异常
     */
    public static Set<String> getClassNamesFromJarFile(File givenFile) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }

    /**
     * 根据包名和目录获取该包下的所有类
     * @param packageName 包名
     * @param directory 目录
     * @return 类的数组
     */
    private static Class<?>[] getClassesInPackage(String packageName, File directory) {
        List<Class<?>> classes = new ArrayList<>();

        for (String filename : Objects.requireNonNull(directory.list())) {
            if (filename.endsWith(".class")) {
                String className = buildClassName(packageName, filename);
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    System.err.println("Error creating class " + className);
                }
            } else if (!filename.contains(".")) {
                String name = packageName + (packageName.endsWith(".") ? "" : ".") + filename;
                classes.addAll(Arrays.asList(getClassesInPackage(name, getPackageDirectory(name))));
            }
        }

        return classes.toArray(new Class[0]);
    }

    /**
     * 构建类名
     * @param packageName 包名
     * @param filename 文件名
     * @return 完整的类名
     */
    public static String buildClassName(String packageName, String filename) {
        return packageName + '.' + filename.replace(".class", "");
    }

    /**
     * 获取指定包的目录
     * @param packageName 包名
     * @return 目录文件对象
     */
    private static File getPackageDirectory(String packageName) {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        if (cld == null) {
            throw new IllegalStateException("Can't get class loader.");
        }

        URL resource = cld.getResource(packageName.replace('.', '/'));

        if (resource == null) {
            throw new RuntimeException("Package " + packageName + " not found on classpath.");
        }

        return new File(resource.getFile());
    }

    /**
     * 判断指定包是否存在
     * @param packageName 包名
     * @return 存在返回true，不存在返回false
     */
    public static boolean dirExist(String packageName) {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        URL resource = cld.getResource(packageName.replace('.', '/'));
        return resource != null;
    }

    /**
     * 获取当前类的路径
     * @return 当前类的路径
     * @throws URISyntaxException URI语法异常
     */
    public static String path() throws URISyntaxException {
        return new File(ReflectionUtil.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
    }
}
