package cn.langya.ui;

import cn.langya.Wrapper;
import cn.langya.utils.HoveringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 表示界面元素的类，实现Wrapper接口
 */
@Getter
@Setter
public class Element implements Wrapper {
    /**
     * 构造函数，初始化元素名称
     * @param name 元素名称
     */
    public Element(String name) {
        this.name = name;
    }

    private final String name; // 元素名称
    private float x, y, width, height; // 元素的坐标和尺寸
    private boolean isHovering; // 是否悬停状态

    /**
     * 设置元素的位置
     * @param x X坐标
     * @param y Y坐标
     */
    public void setXY(float x,float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 设置元素的宽度和高度
     * @param width 宽度
     * @param height 高度
     */
    public void setWH(float width,float height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 检查鼠标是否悬停在元素上
     * @param mouseX 鼠标的X坐标
     * @param mouseY 鼠标的Y坐标
     * @return 如果鼠标悬停在元素上则返回true，否则返回false
     */
    public boolean checkHover(int mouseX, int mouseY) {
        return HoveringUtil.isHovering(x,y,width,height,mouseX,mouseY);
    }
}
