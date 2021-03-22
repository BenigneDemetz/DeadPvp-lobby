package net.DeadPvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class World implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            Player p = (Player) sender;
            p.sendMessage(p.getServer().getServerName());
            p.sendMessage(p.getWorld().getName());
            p.sendMessage(" ");
            for (org.bukkit.World world : p.getServer().getWorlds()) {

                p.sendMessage(world.getName());
            }
            
        return false;
    }
}
