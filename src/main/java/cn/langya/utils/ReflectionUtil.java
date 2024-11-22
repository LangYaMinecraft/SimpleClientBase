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
 */
public class ReflectionUtil {
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

            return classes.toArray(new Class[classes.size()]);
        } catch (Exception exception) {

            File directory = getPackageDirectory(packageName);

            if (!directory.exists()) {
                throw new IllegalArgumentException("Could not get directory resource for package " + packageName);
            }

            return getClassesInPackage(packageName, directory);
        }
    }

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

        return classes.toArray(new Class[classes.size()]);
    }

    public static String buildClassName(String packageName, String filename) {
        return packageName + '.' + filename.replace(".class", "");
    }

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

    public static boolean dirExist(String packageName) {
        ClassLoader cld = Thread.currentThread().getContextClassLoader();
        URL resource = cld.getResource(packageName.replace('.', '/'));
        return resource != null;
    }

    public static String path() throws URISyntaxException {
        return new File(ReflectionUtil.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
    }
}