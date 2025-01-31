package net.minecraft.client.network;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;

public class NetworkPlayerInfo {
    /**
     * 玩家对应的游戏档案信息。
     */
    private final GameProfile gameProfile;

    /**
     * 玩家当前的游戏模式（生存、创造、冒险等）。
     */
    private WorldSettings.GameType gameType;

    /**
     * 服务器响应时间（延迟），单位为毫秒。
     */
    private int responseTime;

    /**
     * 是否已加载玩家的皮肤和其他纹理。
     */
    private boolean playerTexturesLoaded = false;

    /**
     * 玩家皮肤的资源位置。
     */
    private ResourceLocation locationSkin;

    /**
     * 玩家斗篷的资源位置，如果没有斗篷，则可能为 null。
     */
    private ResourceLocation locationCape;

    /**
     * 皮肤类型（例如 "default" 或 "slim"）。
     */
    private String skinType;

    /**
     * 玩家在聊天或界面中的显示名称（可能带有格式化）。
     */
    private IChatComponent displayName;

    /**
     * 未知用途的整数字段，可能与玩家状态或计数器相关。
     */
    private int field_178873_i = 0;

    /**
     * 未知用途的整数字段，可能与网络或渲染相关。
     */
    private int field_178870_j = 0;

    /**
     * 记录某个时间戳（单位：毫秒），可能用于超时、动画或状态更新。
     */
    private long field_178871_k = 0L;

    /**
     * 记录某个时间戳（单位：毫秒），可能用于网络同步或状态管理。
     */
    private long field_178868_l = 0L;

    /**
     * 记录某个时间戳（单位：毫秒），可能用于服务器响应或玩家活动追踪。
     */
    private long field_178869_m = 0L;

    public NetworkPlayerInfo(GameProfile p_i46294_1_)
    {
        this.gameProfile = p_i46294_1_;
    }

    public NetworkPlayerInfo(S38PacketPlayerListItem.AddPlayerData p_i46295_1_)
    {
        this.gameProfile = p_i46295_1_.getProfile();
        this.gameType = p_i46295_1_.getGameMode();
        this.responseTime = p_i46295_1_.getPing();
        this.displayName = p_i46295_1_.getDisplayName();
    }

    public GameProfile getGameProfile()
    {
        return this.gameProfile;
    }

    public WorldSettings.GameType getGameType()
    {
        return this.gameType;
    }

    public int getResponseTime()
    {
        return this.responseTime;
    }

    protected void setGameType(WorldSettings.GameType p_178839_1_)
    {
        this.gameType = p_178839_1_;
    }

    protected void setResponseTime(int p_178838_1_)
    {
        this.responseTime = p_178838_1_;
    }

    public boolean hasLocationSkin()
    {
        return this.locationSkin != null;
    }

    public String getSkinType()
    {
        return this.skinType == null ? DefaultPlayerSkin.getSkinType(this.gameProfile.getId()) : this.skinType;
    }

    public ResourceLocation getLocationSkin()
    {
        if (this.locationSkin == null)
        {
            this.loadPlayerTextures();
        }

        return (ResourceLocation)Objects.firstNonNull(this.locationSkin, DefaultPlayerSkin.getDefaultSkin(this.gameProfile.getId()));
    }

    public ResourceLocation getLocationCape()
    {
        if (this.locationCape == null)
        {
            this.loadPlayerTextures();
        }

        return this.locationCape;
    }

    public ScorePlayerTeam getPlayerTeam()
    {
        return Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(this.getGameProfile().getName());
    }

    protected void loadPlayerTextures()
    {
        synchronized (this)
        {
            if (!this.playerTexturesLoaded)
            {
                this.playerTexturesLoaded = true;
                Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, new SkinManager.SkinAvailableCallback()
                {
                    public void skinAvailable(Type p_180521_1_, ResourceLocation location, MinecraftProfileTexture profileTexture)
                    {
                        switch (p_180521_1_)
                        {
                            case SKIN:
                                NetworkPlayerInfo.this.locationSkin = location;
                                NetworkPlayerInfo.this.skinType = profileTexture.getMetadata("model");

                                if (NetworkPlayerInfo.this.skinType == null)
                                {
                                    NetworkPlayerInfo.this.skinType = "default";
                                }

                                break;

                            case CAPE:
                                NetworkPlayerInfo.this.locationCape = location;
                        }
                    }
                }, true);
            }
        }
    }

    public void setDisplayName(IChatComponent displayNameIn)
    {
        this.displayName = displayNameIn;
    }

    public IChatComponent getDisplayName()
    {
        return this.displayName;
    }

    public int func_178835_l()
    {
        return this.field_178873_i;
    }

    public void func_178836_b(int p_178836_1_)
    {
        this.field_178873_i = p_178836_1_;
    }

    public int func_178860_m()
    {
        return this.field_178870_j;
    }

    public void func_178857_c(int p_178857_1_)
    {
        this.field_178870_j = p_178857_1_;
    }

    public long func_178847_n()
    {
        return this.field_178871_k;
    }

    public void func_178846_a(long p_178846_1_)
    {
        this.field_178871_k = p_178846_1_;
    }

    public long func_178858_o()
    {
        return this.field_178868_l;
    }

    public void func_178844_b(long p_178844_1_)
    {
        this.field_178868_l = p_178844_1_;
    }

    public long func_178855_p()
    {
        return this.field_178869_m;
    }

    public void func_178843_c(long p_178843_1_)
    {
        this.field_178869_m = p_178843_1_;
    }
}
