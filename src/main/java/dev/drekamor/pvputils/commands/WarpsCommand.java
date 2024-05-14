package dev.drekamor.pvputils.commands;

import dev.drekamor.pvputils.handlers.WarpHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpsCommand implements TabExecutor {
    private final WarpHandler handler;
    public WarpsCommand(WarpHandler handler){
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
        if(args.length == 2) return args[0].equalsIgnoreCase("remove") && handler.remove((Player) sender, args[1]);
        if(args.length != 3) return false;
        return args[0].equalsIgnoreCase("add") && handler.add((Player) sender, args[1], args[2]);
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) return List.of("add", "remove", "list");
        if (args.length == 2) return args[0].equalsIgnoreCase("remove")? handler.getIndex() : null;
        if(args.length == 3) return List.of("creative", "survival", "adventure", "spectator");
        return null;
    }
}
