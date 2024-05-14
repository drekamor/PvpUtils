package dev.drekamor.pvputils.handlers;

import dev.drekamor.pvputils.PvpUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

public class InventoryHandler {
    private final PvpUtils plugin;

    public InventoryHandler(PvpUtils plugin) {
        this.plugin = plugin;
    }

    public List<String> getIndex() {
        return this.plugin.getIndexCache().getInventoryIndex();
    }

    public boolean save(Player player, String name){
        if (!player.hasPermission("pvputils.inventories.manage")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        if(this.getIndex().contains(name)) {
            player.sendMessage("Inventory %s already exists".formatted(name));
            return true;
        }

        if(this.plugin.getPluginConfig().getInventoryLimitConfig().enabled()){
            if(this.plugin.getDatabaseManager().countInventories(player.getUniqueId().toString()) >= this.plugin.getPluginConfig().getInventoryLimitConfig().limit()){
                this.plugin.info("%s has reached their inventories limit".formatted(player.getName()));
                player.sendMessage("You have reached your inventories limit");

                return true;
            }
        }

        String inventoryContents = this.toString(player.getInventory().getContents());
        String armourContents = this.toString(player.getInventory().getArmorContents());

        if(inventoryContents == null || armourContents == null) {
            this.plugin.warn("Failed to serialize an inventory");
            player.sendMessage("Failed to save %s".formatted(name));
            return true;
        }

        if(this.plugin.getDatabaseManager().saveInventory(player.getUniqueId().toString(), name, inventoryContents, armourContents)){
            this.plugin.info("%s saved inventory %s".formatted(player.getName(), name));
            player.sendMessage("Successfully saved inventory %s".formatted(name));

            this.plugin.getIndexCache().addInventory(name);

            return true;
        }

        player.sendMessage("Failed to save %s".formatted(name));
        return true;
    }

    public boolean load(Player player, String name){
        if (!player.hasPermission("pvputils.inventories.use")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        String[] serializedInventory = this.plugin.getDatabaseManager().getInventory(name);
        if(serializedInventory == null){
            player.sendMessage("Inventory %s does not exist".formatted(name));
            return true;
        }
        ItemStack[] inventoryContents = this.fromString(serializedInventory[0]);
        ItemStack[] armourContents = this.fromString(serializedInventory[1]);

        if(inventoryContents == null || armourContents == null) {
            this.plugin.warn("Failed to deserialize an inventory");
            player.sendMessage("Failed to load %s".formatted(name));
            return true;
        }

        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armourContents);
        player.updateInventory();

        this.plugin.info("%s loaded inventory %s".formatted(player.getName(), name));
        player.sendMessage("Loaded inventory %s".formatted(name));

        return true;
    }

    public boolean remove(Player player, String name){
        if (!player.hasPermission("pvputils.inventories.manage")) {
            player.sendMessage("You do not have permission to execute this command");
            return true;
        }

        if(this.plugin.getDatabaseManager().removeInventory(name)){
            this.plugin.info("%s removed inventory %s".formatted(player.getName(), name));
            player.sendMessage("Successfully removed inventory %s".formatted(name));

            this.plugin.getIndexCache().removeInventory(name);

            return true;
        }
        player.sendMessage("Failed to remove %s".formatted(name));
        return true;
    }

    public boolean list(Player player){
        if (!player.hasPermission("pvputils.inventories.use")) {
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
        player.sendMessage("There are no saved inventories");
        return true;
    }

    private String toString(ItemStack[] i) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BukkitObjectOutputStream boos = new BukkitObjectOutputStream(baos);

            boos.writeInt(i.length);

            for (ItemStack item : i)
                boos.writeObject(item);

            boos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    private ItemStack[] fromString(String s) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(s));
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            ItemStack[] items = new ItemStack[bois.readInt()];

            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) bois.readObject();

            bois.close();
            return items;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
