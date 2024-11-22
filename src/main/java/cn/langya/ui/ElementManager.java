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

    public Element createElement(String elementName,String moduleName,float initX,float initY) {
        Element element = new Element(elementName,moduleName);
        element.setXY(initX,initY);
        elementMap.put(moduleName,element);
        return elementMap.get(moduleName);
    }

    public Element createElement(String elementName,String moduleName) {
        return createElement(elementName,moduleName,10,10);
    }
}
