package dev.drekamor.pvputils.commands;

import dev.drekamor.pvputils.handlers.InventoriesHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class InventoryCommand implements TabExecutor {
    private final InventoriesHandler handler;
    public InventoryCommand(InventoriesHandler handler){
        this.handler = handler;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command");
            return false;
        }
        if(args.length == 0) return false;
        if(args.length == 1) return args[0].equalsIgnoreCase("list") && handler.list((Player) sender);
        if(args.length != 2) return false;
        return switch (args[0]){
            case "save" -> handler.save((Player) sender, args[1]);
            case "load" -> handler.load((Player) sender, args[1]);
            case "remove" -> handler.remove((Player) sender, args[1]);
            default -> false;
        };
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("save", "load", "remove", "list");
            case 2 -> handler.getIndex();
            default -> null;
        };
    }
}
