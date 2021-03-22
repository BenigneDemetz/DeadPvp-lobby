package net.DeadPvp.commands;

import net.DeadPvp.utils.VanichUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vanich implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("dp.modo.vanich")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                VanichUtil.Vanich (p);
            }
            else sender.sendMessage("Tu dois etre joueur");
        }
        else sender.sendMessage("Tu n'as pas la permission d'éxecuter cette commande.");


        return false;
    }
}