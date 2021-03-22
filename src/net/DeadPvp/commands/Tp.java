package net.DeadPvp.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.DeadPvp.Main;
import net.DeadPvp.utils.VanichUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class Tp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Bukkit.broadcastMessage(String.valueOf(args.length));
        for (int i = 0; i < args.length; ++i)
        {
            Bukkit.broadcastMessage(args[i]);
        }
        Player player = null;

            if (sender.hasPermission("dp.modo.tp") || sender.hasPermission("dp.*") || sender.hasPermission("dp.modo.*")) {

                if (args.length == 1) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("Tu n'es pas un joueur !");
                        return false;
                    }
                        Player p = (Player) sender;

                    for (World world : Bukkit.getServer().getWorlds())
                    {
                        if (args[0].equalsIgnoreCase(world.getName()))
                        {
                            p.teleport(world.getSpawnLocation());
                            return false;
                        }
                    }

                    for (OfflinePlayer target : Bukkit.getOfflinePlayers()) {
                        sender.sendMessage("§b" + target.getName() + " n'est pas connecté");
                        return false;
                    }
                    for (Player target : Bukkit.getOnlinePlayers()) {

                        if (target.getName().equalsIgnoreCase(args[0])) {
                            p.teleport(target);
                            p.sendMessage("Tu as été téléporté à §b" + target.getName());
                            return false;
                        }
                    }

                }

                else if (args.length == 2) {
                    for (Player teleported : Bukkit.getOnlinePlayers()) {
                        for (Player target : Bukkit.getOnlinePlayers()) {
                            if (teleported.getName().equalsIgnoreCase(args[0]) && target.getName().equalsIgnoreCase(args[1])) {
                                teleported.teleport(target);
                                sender.sendMessage("§b" + teleported.getName() + "§f a été téleporté à §b" + target.getName());
                                teleported.sendMessage("Tu as été téléporté à §b" + target.getName());
                                return false;
                            }
                        }
                    }
                }

                else if (!(sender instanceof Player)) return false;
                else if (sender instanceof Player) player = (Player) sender;

                if (args.length == 3)
                {
                    try {
                        int x = Integer.parseInt(args[0]);
                        int y = Integer.parseInt(args[1]);
                        int z = Integer.parseInt(args[2]);
                        Bukkit.broadcastMessage("pipi");
                        Location loc = player.getLocation();
                        loc.setX(x);
                        loc.setY(y);
                        loc.setZ(z);
                        player.teleport(loc);
                    }
                    catch (Exception e)
                    {
                        sender.sendMessage("Mauvaises coordonnées");
                    }
                }



            }
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");

        return false;
    }
}
