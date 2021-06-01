package net.DeadPvp.commands;

import net.DeadPvp.utils.UtilityFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Builder implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("deadpvp.builder") ||sender.hasPermission("deadpvp.*"))
            UtilityFunctions.tpToServ((Player) sender, "builder");
        } else sender.sendMessage("Tu dois etre un joueur");


        return false;
    }
}