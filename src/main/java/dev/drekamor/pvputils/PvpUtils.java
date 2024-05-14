package dev.drekamor.pvputils;

import dev.drekamor.pvputils.commands.InventoryCommand;
import dev.drekamor.pvputils.commands.WarpCommand;
import dev.drekamor.pvputils.commands.WarpsCommand;
import dev.drekamor.pvputils.config.Config;
import dev.drekamor.pvputils.config.DatabaseConfig;
import dev.drekamor.pvputils.handlers.InventoryHandler;
import dev.drekamor.pvputils.handlers.WarpHandler;
import dev.drekamor.pvputils.utils.IndexCache;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class PvpUtils extends JavaPlugin {
    static {
        registerSerializations();
    }
    private Config config;
    private DatabaseManager databaseManager;
    private IndexCache indexCache;
    private InventoryHandler inventoriesHandler;
    private WarpHandler warpsHandler;
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.config = new Config(this.getConfig());

        this.databaseManager = new DatabaseManager(this, this.config.getDatabaseConfig());

        this.initializeHandlers();
        this.registerCommands();

        this.initializeIndices();
    }

    @Override
    public void onDisable() {
        this.registerSerializations();
    }

    private void registerCommands() {
        this.getCommand("inventory").setExecutor(new InventoryCommand(this.inventoriesHandler));
        this.getCommand("warp").setExecutor(new WarpCommand(this.warpsHandler));
        this.getCommand("warps").setExecutor(new WarpsCommand(this.warpsHandler));
    }

    private void initializeHandlers() {
        this.inventoriesHandler = new InventoryHandler(this);
        this.warpsHandler = new WarpHandler(this);
    }
    private static void registerSerializations() {
        ConfigurationSerialization.registerClass(DatabaseConfig.class);
    }

    private void initializeIndices() {
        this.indexCache = new IndexCache();
        this.indexCache.setInventoryIndex(this.databaseManager.getInventoryIndex());
        this.indexCache.setWarpIndex(this.databaseManager.getWarpIndex());
    }

    public Config getPluginConfig() {
        return this.config;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public IndexCache getIndexCache() {
        return this.indexCache;
    }

    public void info(String string) { this.getLogger().info(string);}
    public void warn(String string) {this.getLogger().severe(string);}
    public void severe(String string) {this.getLogger().severe(string);}
}
