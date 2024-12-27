package cn.langya.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

// 元素管理类，负责创建和管理元素
@Getter
@Setter
public class ElementManager {
    // 构造函数，初始化元素映射表
    public ElementManager() {
        this.elementMap = new HashMap<>();
    }

    private final Map<String,Element> elementMap;

    // 创建一个新的元素并设置初始坐标
    public Element createElement(String name,float initX,float initY) {
        Element element = new Element(name);
        element.setXY(initX,initY);
        elementMap.put(name,element);
        return elementMap.get(name);
    }

    // 创建一个新的元素，使用默认坐标 (10, 10)
    public Element createElement(String name) {
        return createElement(name,10,10);
    }
}
