package dev.drekamor.pvputils.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final FileConfiguration config;
    public Config(FileConfiguration config){
        this.config = config;
    }

    public DatabaseConfig getDatabaseConfig() {
        return (DatabaseConfig) this.config.get("db");
    }

    public LimitConfig getInventoryLimitConfig() {
        ConfigurationSection limit = this.config.getConfigurationSection("limits.inventories");
        return new LimitConfig(limit.getBoolean("enabled"), limit.getInt("limit"));
    }

    public LimitConfig getWarpLimitConfig() {
        ConfigurationSection limit = this.config.getConfigurationSection("limits.warps");
        return new LimitConfig(limit.getBoolean("enabled"), limit.getInt("limit"));
    }
}
