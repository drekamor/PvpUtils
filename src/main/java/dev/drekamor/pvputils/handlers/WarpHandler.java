package dev.drekamor.pvputils.handlers;

import dev.drekamor.pvputils.PvpUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Base64;
import java.util.List;

public class WarpHandler {
    private final PvpUtils plugin;

    public WarpHandler(PvpUtils plugin) {
        this.plugin = plugin;
    }

    public List<String> getIndex() {
        return this.plugin.getIndexCache().getWarpIndex();
    }

    public boolean add(Player player, String name, String gamemode){
        if (!player.hasPermission("pvputils.warps.manage")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        if(this.getIndex().contains(name)) {
            player.sendMessage("Warp %s already exists".formatted(name));
            return true;
        }

        if(this.plugin.getPluginConfig().getWarpLimitConfig().enabled()){
            if(this.plugin.getDatabaseManager().countWarps(player.getUniqueId().toString()) > this.plugin.getPluginConfig().getWarpLimitConfig().limit()){
                this.plugin.info("%s has reached their warps limit".formatted(player.getName()));
                player.sendMessage("You have reached your warps limit");

                return true;
            }
        }

        String location = this.toString(player.getLocation());

        if(getGamemode(gamemode) == null) {
            player.sendMessage("Unknown game mode");
            return false;
        }

        if(location == null) {
            this.plugin.warn("Failed to serialize a warp");
            player.sendMessage("Failed to add warp %s".formatted(name));
            return true;
        }

        if(this.plugin.getDatabaseManager().saveWarp(player.getUniqueId().toString(), name, location, gamemode)){
            this.plugin.info("%s saved warp %s".formatted(player.getName(), name));
            player.sendMessage("Successfully saved warp %s".formatted(name));

            this.plugin.getIndexCache().addWarp(name);

            return true;
        }
        player.sendMessage("Failed to save %s".formatted(name));
        return true;
    }

    public boolean warp(Player player, String name){
        if (!player.hasPermission("pvputils.warps.use")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        String[] serializedWarp = this.plugin.getDatabaseManager().getWarp(name);
        if(serializedWarp == null){
            player.sendMessage("Warp %s does not exist".formatted(name));
            return true;
        }

        Location location = this.fromString(serializedWarp[0]);
        GameMode gamemode = getGamemode(serializedWarp[1]);

        if(location == null || gamemode == null) {
            this.plugin.warn("Failed to deserialize a warp");
            player.sendMessage("Failed to warp to %s".formatted(name));
            return true;
        }

        if(player.teleport(location)){
            player.setGameMode(gamemode);
            this.plugin.info("Sent %s to %s".formatted(player.getName(), name));
            player.sendMessage("Warping to %s...".formatted(name));

            return true;
        }

        player.sendMessage("Failed to get you to %s".formatted(name));
        return true;
    }

    public boolean remove(Player player, String name){
        if (!player.hasPermission("pvputils.warps.manage")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        if(this.plugin.getDatabaseManager().removeWarp(name)){
            this.plugin.info("%s removed warp %s".formatted(player.getName(), name));
            player.sendMessage("Successfully removed warp %s".formatted(name));

            this.plugin.getIndexCache().removeWarp(name);

            return true;
        }
        player.sendMessage("Failed to remove %s".formatted(name));
        return true;
    }

    public boolean list(Player player){
        if (!player.hasPermission("pvputils.warps.use")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        if(getIndex() != null && !getIndex().isEmpty()){
            StringBuilder message = new StringBuilder(getIndex().get(0));
            for(int i = 1; i < getIndex().size(); i++){
                message.append(" ").append(getIndex().get(i));
            }

            player.sendMessage(message.toString());
            return true;
        }
        player.sendMessage("There are no saved warps");
        return true;
    }

    private @Nullable GameMode getGamemode(String gamemode) {
        return switch (gamemode) {
            case "creative" -> GameMode.CREATIVE;
            case "survival" -> GameMode.SURVIVAL;
            case "adventure" -> GameMode.ADVENTURE;
            case "spectator" -> GameMode.SPECTATOR;
            default -> null;
        };
    }

    private String toString(Location l) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);

            boos.writeObject(l);

            boos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Location fromString(String s) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(s));
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);

            Location l = (Location) bois.readObject();

            bois.close();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
