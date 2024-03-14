package dev.drekamor.pvputils.commands;

import dev.drekamor.pvputils.handlers.WarpsHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCommand implements TabExecutor {
    private final WarpsHandler handler;
    public WarpCommand(WarpsHandler handler){
        this.handler = handler;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can use this command");
            return false;
        }
        if(args.length != 1) return false;
        return handler.warp((Player) sender, args[0]);
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return switch (args.length) {
            case 1 -> handler.getIndex();
            default -> null;
        };
    }
}
