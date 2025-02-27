package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.world.Explosion;

public class DamageSource
{
    // 定义各种伤害来源，包括火焰、闪电、溺水、饥饿等
    public static DamageSource inFire = (new DamageSource("inFire")).setFireDamage();
    public static DamageSource lightningBolt = new DamageSource("lightningBolt");
    public static DamageSource onFire = (new DamageSource("onFire")).setDamageBypassesArmor().setFireDamage();
    public static DamageSource lava = (new DamageSource("lava")).setFireDamage();
    public static DamageSource inWall = (new DamageSource("inWall")).setDamageBypassesArmor();
    public static DamageSource drown = (new DamageSource("drown")).setDamageBypassesArmor();
    public static DamageSource starve = (new DamageSource("starve")).setDamageBypassesArmor().setDamageIsAbsolute();
    public static DamageSource cactus = new DamageSource("cactus");
    public static DamageSource fall = (new DamageSource("fall")).setDamageBypassesArmor();
    public static DamageSource outOfWorld = (new DamageSource("outOfWorld")).setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static DamageSource generic = (new DamageSource("generic")).setDamageBypassesArmor();
    public static DamageSource magic = (new DamageSource("magic")).setDamageBypassesArmor().setMagicDamage();
    public static DamageSource wither = (new DamageSource("wither")).setDamageBypassesArmor();
    public static DamageSource anvil = new DamageSource("anvil");
    public static DamageSource fallingBlock = new DamageSource("fallingBlock");
    // 表示伤害是否无法被阻挡
    private boolean isUnblockable;
    // 表示伤害是否在创造模式下也被允许
    private boolean isDamageAllowedInCreativeMode;
    // 表示伤害是否绝对，不受任何防御影响
    private boolean damageIsAbsolute;
    // 饥饿伤害的数值
    private float hungerDamage = 0.3F;
    // 表示伤害是否来自火焰
    private boolean fireDamage;
    // 表示伤害是否来自投射物
    private boolean projectile;
    // 表示伤害是否根据难度进行调整
    private boolean difficultyScaled;
    // 表示伤害是否为魔法伤害
    private boolean magicDamage;
    // 表示伤害是否为爆炸伤害
    private boolean explosion;
    // 伤害类型的字符串标识
    public String damageType;

    public static DamageSource causeMobDamage(EntityLivingBase mob)
    {
        return new EntityDamageSource("mob", mob);
    }

    public static DamageSource causePlayerDamage(EntityPlayer player)
    {
        return new EntityDamageSource("player", player);
    }

    public static DamageSource causeArrowDamage(EntityArrow arrow, Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("arrow", arrow, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeFireballDamage(EntityFireball fireball, Entity indirectEntityIn)
    {
        return indirectEntityIn == null ? (new EntityDamageSourceIndirect("onFire", fireball, fireball)).setFireDamage().setProjectile() : (new EntityDamageSourceIndirect("fireball", fireball, indirectEntityIn)).setFireDamage().setProjectile();
    }

    public static DamageSource causeThrownDamage(Entity source, Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("thrown", source, indirectEntityIn)).setProjectile();
    }

    public static DamageSource causeIndirectMagicDamage(Entity source, Entity indirectEntityIn)
    {
        return (new EntityDamageSourceIndirect("indirectMagic", source, indirectEntityIn)).setDamageBypassesArmor().setMagicDamage();
    }

    public static DamageSource causeThornsDamage(Entity source)
    {
        return (new EntityDamageSource("thorns", source)).setIsThornsDamage().setMagicDamage();
    }

    public static DamageSource setExplosionSource(Explosion explosionIn)
    {
        return explosionIn != null && explosionIn.getExplosivePlacedBy() != null ? (new EntityDamageSource("explosion.player", explosionIn.getExplosivePlacedBy())).setDifficultyScaled().setExplosion() : (new DamageSource("explosion")).setDifficultyScaled().setExplosion();
    }

    public boolean isProjectile()
    {
        return this.projectile;
    }

    public DamageSource setProjectile()
    {
        this.projectile = true;
        return this;
    }

    public boolean isExplosion()
    {
        return this.explosion;
    }

    public DamageSource setExplosion()
    {
        this.explosion = true;
        return this;
    }

    public boolean isUnblockable()
    {
        return this.isUnblockable;
    }

    public float getHungerDamage()
    {
        return this.hungerDamage;
    }

    public boolean canHarmInCreative()
    {
        return this.isDamageAllowedInCreativeMode;
    }

    public boolean isDamageAbsolute()
    {
        return this.damageIsAbsolute;
    }

    protected DamageSource(String damageTypeIn)
    {
        this.damageType = damageTypeIn;
    }

    public Entity getSourceOfDamage()
    {
        return this.getEntity();
    }

    public Entity getEntity()
    {
        return null;
    }

    protected DamageSource setDamageBypassesArmor()
    {
        this.isUnblockable = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setDamageAllowedInCreativeMode()
    {
        this.isDamageAllowedInCreativeMode = true;
        return this;
    }

    protected DamageSource setDamageIsAbsolute()
    {
        this.damageIsAbsolute = true;
        this.hungerDamage = 0.0F;
        return this;
    }

    protected DamageSource setFireDamage()
    {
        this.fireDamage = true;
        return this;
    }

    public IChatComponent getDeathMessage(EntityLivingBase entityLivingBaseIn)
    {
        EntityLivingBase entitylivingbase = entityLivingBaseIn.getAttackingEntity();
        String s = "death.attack." + this.damageType;
        String s1 = s + ".player";
        return entitylivingbase != null && StatCollector.canTranslate(s1) ? new ChatComponentTranslation(s1, entityLivingBaseIn.getDisplayName(), entitylivingbase.getDisplayName()): new ChatComponentTranslation(s, entityLivingBaseIn.getDisplayName());
    }

    public boolean isFireDamage()
    {
        return this.fireDamage;
    }

    public String getDamageType()
    {
        return this.damageType;
    }

    public DamageSource setDifficultyScaled()
    {
        this.difficultyScaled = true;
        return this;
    }

    public boolean isDifficultyScaled()
    {
        return this.difficultyScaled;
    }

    public boolean isMagicDamage()
    {
        return this.magicDamage;
    }

    public DamageSource setMagicDamage()
    {
        this.magicDamage = true;
        return this;
    }

    public boolean isCreativePlayer()
    {
        Entity entity = this.getEntity();
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
    }
}
