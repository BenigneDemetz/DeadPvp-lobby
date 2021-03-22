package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.VanichUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Fly implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("dp.modo.fly")) {
                p.setAllowFlight(!p.getAllowFlight());
                p.setFlying(p.getAllowFlight());
                if (p.getAllowFlight()) {
                    p.sendMessage("Tu peux fly");
                } else
                    p.sendMessage("Tu peux plus fly");
            }
        } else sender.sendMessage("Tu dois etre joueur");

        return false;
    }
}