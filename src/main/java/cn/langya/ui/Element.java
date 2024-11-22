package cn.langya.ui;

import cn.langya.Wrapper;
import cn.langya.utils.HoveringUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Element implements Wrapper {
    public Element(String elementName, String moduleName) {
        this.elementName = elementName;
        this.moduleName = moduleName;
    }

    private final String moduleName, elementName;
    private float x, y, width, height;
    private boolean isHovering;

    public void setXY(float x,float y) {
        this.x = x;
        this.y = y;
    }

    public void setWH(float width,float height) {
        this.width = width;
        this.height = height;
    }

    public boolean checkHover(int mouseX, int mouseY) {
        return HoveringUtil.isHovering(x,y,width,height,mouseX,mouseY);
    }
}
