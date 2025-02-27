package net.minecraft.util;

import org.apache.commons.lang3.Validate;

/**
 * 表示资源位置的类，通常用于Minecraft中的资源管理。
 */
public class ResourceLocation
{
    protected final String resourceDomain;
    protected final String resourcePath;

    /**
     * 保护构造函数，用于初始化资源域和资源路径。
     * @param p_i45928_1_ 未使用的参数
     * @param resourceName 包含资源域和资源路径的字符串数组
     */
    protected ResourceLocation(int p_i45928_1_, String... resourceName)
    {
        this.resourceDomain = org.apache.commons.lang3.StringUtils.isEmpty(resourceName[0]) ? "minecraft" : resourceName[0].toLowerCase();
        this.resourcePath = resourceName[1];
        Validate.notNull(this.resourcePath);
    }

    /**
     * 构造函数，通过单个字符串初始化资源位置。
     * @param resourceName 包含资源域和资源路径的字符串，格式为"domain:path"
     */
    public ResourceLocation(String resourceName)
    {
        this(0, splitObjectName(resourceName));
    }

    /**
     * 构造函数，通过资源域和资源路径初始化资源位置。
     * @param resourceDomainIn 资源域
     * @param resourcePathIn 资源路径
     */
    public ResourceLocation(String resourceDomainIn, String resourcePathIn)
    {
        this(0, resourceDomainIn, resourcePathIn);
    }

    /**
     * 将资源名称字符串分割成资源域和资源路径。
     * @param toSplit 包含资源域和资源路径的字符串，格式为"domain:path"
     * @return 包含资源域和资源路径的字符串数组
     */
    protected static String[] splitObjectName(String toSplit)
    {
        String[] astring = new String[] {null, toSplit};
        int i = toSplit.indexOf(58);

        if (i >= 0)
        {
            astring[1] = toSplit.substring(i + 1);

            if (i > 1)
            {
                astring[0] = toSplit.substring(0, i);
            }
        }

        return astring;
    }

    /**
     * 获取资源路径。
     * @return 资源路径
     */
    public String getResourcePath()
    {
        return this.resourcePath;
    }

    /**
     * 获取资源域。
     * @return 资源域
     */
    public String getResourceDomain()
    {
        return this.resourceDomain;
    }

    /**
     * 返回资源位置的字符串表示，格式为"domain:path"。
     * @return 资源位置的字符串表示
     */
    public String toString()
    {
        return this.resourceDomain + ':' + this.resourcePath;
    }

    /**
     * 比较两个对象是否相等。
     * @param p_equals_1_ 要比较的对象
     * @return 如果对象相等返回true，否则返回false
     */
    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
            return this.resourceDomain.equals(resourcelocation.resourceDomain) && this.resourcePath.equals(resourcelocation.resourcePath);
        }
    }

    /**
     * 返回资源位置的哈希码。
     * @return 哈希码
     */
    public int hashCode()
    {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}
