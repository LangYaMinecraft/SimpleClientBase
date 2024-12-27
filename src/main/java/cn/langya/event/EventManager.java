package cn.langya.event;

import cn.langya.Logger;
import cn.langya.event.annotations.EventPriority;
import cn.langya.event.annotations.EventTarget;
import cn.langya.event.impl.Event;
import cn.langya.utils.RotationUtil;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author cubk, LangYa
 * 事件管理器
 */
public class EventManager {
    private final Map<Method, Class<?>> registeredMethodMap; // 存储注册的方法和事件类型的映射
    private final Map<Method, Object> methodObjectMap; // 存储方法和其对应对象的映射
    private final Map<Class<? extends Event>, List<RegisteredMethod>> priorityMethodMap; // 存储按优先级排序的方法

    // 构造函数，初始化事件管理器并注册默认的旋转工具实例
    public EventManager() {
        registeredMethodMap = new HashMap<>();
        methodObjectMap = new HashMap<>();
        priorityMethodMap = new HashMap<>();

        register(RotationUtil.INSTANCE);
    }

    // 注册或注销模块
    public void registerModule(boolean enable, Object obj) {
        if (enable) register(obj); else unregister(obj);
    }

    // 注册多个对象
    public void register(Object... obj) {
        for (Object object : obj) {
            register(object);
        }
    }

    // 注册单个对象，查找其标记为事件的方法
    public void register(Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == EventTarget.class && method.getParameterTypes().length == 1) {
                    registeredMethodMap.put(method, method.getParameterTypes()[0]);
                    methodObjectMap.put(method, obj);

                    Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);
                    EventPriority priority = method.getAnnotation(EventPriority.class);
                    int priorityValue = (priority != null) ? priority.value() : 10;

                    // 使用更高效的结构存储排序的方法
                    priorityMethodMap.computeIfAbsent(eventClass, k -> new ArrayList<>())
                            .add(new RegisteredMethod(method, priorityValue));
                }
            }
        }

        // 在注册时排序方法，避免每次事件调用时都进行排序
        priorityMethodMap.values().forEach(list -> list.sort(Comparator.comparingInt(RegisteredMethod::getPriority)));
    }

    // 注销对象注册的方法
    public void unregister(Object obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (registeredMethodMap.containsKey(method)) {
                registeredMethodMap.remove(method);
                methodObjectMap.remove(method);
                Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);
                List<RegisteredMethod> priorityMethods = priorityMethodMap.get(eventClass);
                if (priorityMethods != null) {
                    priorityMethods.removeIf(registeredMethod -> registeredMethod.getMethod().equals(method));
                }
            }
        }
    }

    // 调用事件，触发所有已注册的事件处理方法
    public Event call(Event event) {
        Class<? extends Event> eventClass = event.getClass();

        List<RegisteredMethod> methods = priorityMethodMap.get(eventClass);
        if (methods != null) {
            for (RegisteredMethod registeredMethod : methods) {
                Method method = registeredMethod.getMethod();
                Object obj = methodObjectMap.get(method);
                method.setAccessible(true);
                try {
                    method.invoke(obj, event);
                } catch (Exception e) {
                    Logger.error("Error calling event method: ",e);
                }
            }
        }

        return event;
    }

    // 辅助类，用于存储方法及其优先级
    @Getter
    private static class RegisteredMethod {
        private final Method method; // 被注册的方法
        private final int priority; // 方法的优先级

        // 构造函数，初始化方法和优先级
        public RegisteredMethod(Method method, int priority) {
            this.method = method;
            this.priority = priority;
        }
    }
}