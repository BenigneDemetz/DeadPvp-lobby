package net.deadpvp.commands;

import com.viaversion.viaversion.api.Via;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Creatif implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) return true;
        Player p = (Player)sender;
        if(Via.getAPI ().getPlayerVersion (p) >= 736){
            UtilityFunctions.tpToServ (p, "crea");
            return true;
        } else {
            p.sendMessage ("§c Vous devez être en version 1.16.1 minimum");
        }
        return false;
    }
    
}
