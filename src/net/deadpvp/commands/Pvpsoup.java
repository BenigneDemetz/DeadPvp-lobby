package net.deadpvp.commands;


import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pvpsoup implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            UtilityFunctions.tpToServ((Player) sender, "pvpsoup");
        } else sender.sendMessage("Tu dois etre joueur");
        
        
        return false;
    }
}