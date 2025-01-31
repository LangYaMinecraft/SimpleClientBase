package net.minecraft.util;

/**
 * 这是一个用于平滑鼠标移动的过滤器类。
 */
public class MouseFilter
{
    private float field_76336_a;
    private float field_76334_b;
    private float field_76335_c;

    /**
     * 对鼠标移动进行平滑处理。
     * @param p_76333_1_ 鼠标移动的原始值。
     * @param p_76333_2_ 平滑系数。
     * @return 平滑后的鼠标移动值。
     */
    public float smooth(float p_76333_1_, float p_76333_2_)
    {
        this.field_76336_a += p_76333_1_;
        p_76333_1_ = (this.field_76336_a - this.field_76334_b) * p_76333_2_;
        this.field_76335_c += (p_76333_1_ - this.field_76335_c) * 0.5F;

        if (p_76333_1_ > 0.0F && p_76333_1_ > this.field_76335_c || p_76333_1_ < 0.0F && p_76333_1_ < this.field_76335_c)
        {
            p_76333_1_ = this.field_76335_c;
        }

        this.field_76334_b += p_76333_1_;
        return p_76333_1_;
    }

    /**
     * 重置平滑过滤器的状态。
     */
    public void reset()
    {
        this.field_76336_a = 0.0F;
        this.field_76334_b = 0.0F;
        this.field_76335_c = 0.0F;
    }
}
