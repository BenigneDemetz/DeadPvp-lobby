package net.DeadPvp.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.Main;
import net.DeadPvp.utils.ItemBuilder;
import net.DeadPvp.utils.UtilityFunctions;
import net.DeadPvp.utils.VanichUtil;
import net.md_5.bungee.api.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Hub implements CommandExecutor {

    private Main main;

    public Hub(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return false;
        if (sender.hasPermission("deadpvp.hub") || sender.hasPermission("deadpvp.*")) {
            Player p = (Player) sender;
//        if (p.getServer().getName().equalsIgnoreCase("pvpsoup")) {
//            Location spawn = p.getLocation();
//            spawn.setWorld(p.getServer().getWorld("world"));
//            spawn = spawn.getWorld().getSpawnLocation();
//            p.teleport(spawn);
//        } else
//            {
            UtilityFunctions.tpToServ(p, "lobby");
//        }
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location spawn = p.getLocation();
                    spawn.setX(0.5);
                    spawn.setY(50);
                    spawn.setZ(0.5);
                    spawn.setYaw(0);
                    spawn.setPitch(0);
                    p.teleport(spawn);

                    ItemBuilder compass = new ItemBuilder(Material.COMPASS).setName("§aChoisir mode de jeu");
                    p.getInventory().clear();
                    p.getInventory().setItem(4, compass.toItemStack());

                }
            }.runTaskLater(main, 5);
        }
        else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        return false;
    }
}
