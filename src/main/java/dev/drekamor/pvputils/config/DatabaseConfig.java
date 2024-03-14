package dev.drekamor.pvputils.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DatabaseConfig implements ConfigurationSerializable {
    private String database;
    private String user;
    private String password;
    private String host;
    private int port;
    private int poolSize;
    private long connectionTimeout;
    private long idleTimeout;
    private long maxLifetime;

    public DatabaseConfig(String database, String user, String password, String host,
                          int port, int poolSize, long connectionTimeout, long idleTimeout, long maxLifetime) {
        this.database = database;
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.poolSize = poolSize;
        this.connectionTimeout = connectionTimeout;
        this.idleTimeout = idleTimeout;
        this.maxLifetime = maxLifetime;
    }

    public String getDatabase() {
        return  this.database;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return  this.password;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public int getPoolSize() {
        return this.poolSize;
    }

    public long getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public long getIdleTimeout() {
        return this.idleTimeout;
    }

    public long getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    @Override
    public final @NotNull Map<String, Object> serialize() {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("database", this.database);
        objectMap.put("user", this.user);
        objectMap.put("password", this.password);
        objectMap.put("host", this.host);
        objectMap.put("port", this.port);
        objectMap.put("poolSize", this.poolSize);
        objectMap.put("connectionTimeout", this.connectionTimeout);
        objectMap.put("idleTimeout", this.idleTimeout);
        objectMap.put("maxLifetime", this.maxLifetime);

        return objectMap;
    }
    public static @NotNull DatabaseConfig deserialize(@NotNull Map<String, Object> args) {
        String database = (String) args.get("database");
        String user = (String) args.get("user");
        String password = (String) args.get("password");
        String host = (String) args.get("host");
        int port = (int) args.get("port");
        int poolSize = (int) args.get("poolSize");
        long connectionTimeout = ((Number) args.get("connectionTimeout")).longValue();
        long idleTimeout = ((Number) args.get("idleTimeout")).longValue();
        long maxLifetime = ((Number) args.get("maxLifetime")).longValue();

        return new DatabaseConfig(database, user, password, host, port, poolSize, connectionTimeout, idleTimeout, maxLifetime);
    }
}
