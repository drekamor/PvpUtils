package dev.drekamor.pvputils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.drekamor.pvputils.config.DatabaseConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager {
    public final PvpUtils plugin;
    public final DataSource dataSource;

    public DatabaseManager(PvpUtils plugin, DatabaseConfig config) {
        this.plugin = plugin;

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://%s:%s/%s".formatted(config.getHost(), config.getPort(), config.getDatabase()));
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMaximumPoolSize(config.getPoolSize());
        hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
        hikariConfig.setIdleTimeout(config.getIdleTimeout());
        hikariConfig.setMaxLifetime(config.getMaxLifetime());

        this.dataSource = new HikariDataSource(hikariConfig);

        this.initializeTables();
    }
    private Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (Exception e){
            plugin.severe("Could not Establish connection to the database");
            plugin.severe(Arrays.toString(e.getStackTrace()));
        }
        return null;
    }
    private DataSource getDataSource() throws SQLException {
        if (this.dataSource != null) {
            return this.dataSource;
        }
        throw new SQLException("No data source available");
    }
    public void initializeTables() {
        Connection connection = this.getConnection();
        if(connection == null) {plugin.severe("Failed to initialize tables!"); return;}

        try {

            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `inventories`" +
                    "(uuid VARCHAR(128), name VARCHAR(64) UNIQUE, sic TEXT, sac TEXT);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `warps`" +
                    "(uuid VARCHAR(128), name VARCHAR(64) UNIQUE, location TEXT, gamemode VARCHAR(16));").execute();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to initialize tables");
            e.printStackTrace();
        }
    }

    public boolean saveInventory(String uuid, String name, String sic, String sac){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO inventories(uuid, name, sic, sac) VALUES (?, ?, ?, ?)");
            statement.setString(1, uuid);
            statement.setString(2, name);
            statement.setString(3, sic);
            statement.setString(4, sac);
            statement.execute();
            statement.close();
            connection.close();
            return true;
        } catch (Exception e) {
            plugin.severe("Failed to save inventory %s".formatted(name));
            e.printStackTrace();
            return false;
        }
    }
    public String[] getInventory(String name){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM inventories WHERE name=?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String sic = result.getString("sic");
                String sac = result.getString("sac");
                statement.close();
                result.close();
                connection.close();

                return new String[]{sic, sac};
            }
            statement.close();
            result.close();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to get inventory %s".formatted(name));
            e.printStackTrace();
        }
        return null;
    }
    public boolean removeInventory(String name){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM inventories WHERE name=?");
            statement.setString(1, name);
            statement.execute();
            statement.close();
            connection.close();
            return true;
        } catch (Exception exception) {
            plugin.severe("Failed to remove inventory %s.".formatted(name));
            exception.printStackTrace();
            return false;
        }
    }
    public int countInventories(String uuid){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM inventories WHERE uuid=?");
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                int count = result.getInt(1);
                statement.close();
                result.close();
                connection.close();
                return count;
            }
            statement.close();
            result.close();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to get inventories");
            e.printStackTrace();
        }
        return 0;
    }
    public List<String> getInventoryIndex(){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM inventories");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                List<String> names = new ArrayList<>();
                names.add(result.getString("name"));
                while(result.next()){
                    names.add(result.getString("name"));
                }
                statement.close();
                result.close();
                connection.close();
                return names;
            }
            statement.close();
            result.close();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to get inventories");
            e.printStackTrace();
        }
        return null;
    }
    public boolean saveWarp(String uuid, String name, String location, String gamemode){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("REPLACE INTO warps(uuid, name, location, gamemode) VALUES (?, ?, ?, ?)");
            statement.setString(1, uuid);
            statement.setString(2, name);
            statement.setString(3, location);
            statement.setString(4, gamemode);
            statement.execute();
            statement.close();
            connection.close();
            return true;
        } catch (Exception e) {
            plugin.severe("Failed to save warp %s".formatted(name));
            e.printStackTrace();
            return false;
        }
    }
    public String[] getWarp(String name){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM warps WHERE name=?");
            statement.setString(1, name);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String location = result.getString("location");
                String gamemode = result.getString("gamemode");
                statement.close();
                result.close();
                connection.close();

                return new String[]{location, gamemode};
            }
            statement.close();
            result.close();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to get warp %s".formatted(name));
            e.printStackTrace();
        }
        return null;
    }
    public boolean removeWarp(String name){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM warps WHERE name=?");
            statement.setString(1, name);
            statement.execute();
            statement.close();
            connection.close();
            return true;
        } catch (Exception exception) {
            plugin.severe("Failed to remove warp %s.".formatted(name));
            exception.printStackTrace();
            return false;
        }
    }
    public int countWarps(String uuid){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM warps WHERE uuid=?");
            statement.setString(1, uuid);
            ResultSet result = statement.executeQuery();
            if(result.next()){
                int count = result.getInt(1);
                statement.close();
                result.close();
                connection.close();
                return count;
            }
            statement.close();
            result.close();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to get warps");
            e.printStackTrace();
        }
        return 0;
    }
    public List<String> getWarpIndex(){
        try {
            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM warps");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                List<String> names = new ArrayList<>();
                names.add(result.getString("name"));
                while(result.next()){
                    names.add(result.getString("name"));
                }
                statement.close();
                result.close();
                connection.close();
                return names;
            }
            statement.close();
            result.close();
            connection.close();
        } catch (Exception e) {
            plugin.severe("Failed to get the warps");
            e.printStackTrace();
        }
        return null;
    }
}
