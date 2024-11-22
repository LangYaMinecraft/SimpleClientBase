package cn.langya.ui;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ElementManager {
    public ElementManager() {
        this.elementMap = new HashMap<>();
    }

    private final Map<String,Element> elementMap;

    public Element createElement(String name,float initX,float initY) {
        Element element = new Element(name);
        element.setXY(initX,initY);
        elementMap.put(name,element);
        return elementMap.get(name);
    }

    public Element createElement(String name) {
        return createElement(name,10,10);
    }
}
