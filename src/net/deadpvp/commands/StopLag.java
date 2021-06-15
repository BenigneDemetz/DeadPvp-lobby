package net.deadpvp.commands;

import net.deadpvp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StopLag implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("deadpvp.stoplag") || p.hasPermission("deadpvp.*") ||p.isOp ()) {
                Main.getInstance().stopLag = !Main.getInstance().stopLag;
                p.sendMessage("StopLag : " + Main.getInstance().stopLag);
            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        } else sender.sendMessage("Tu dois etre joueur");
        
        return false;
    }
}