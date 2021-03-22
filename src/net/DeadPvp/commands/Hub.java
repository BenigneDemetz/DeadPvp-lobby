package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.VanichUtil;
import net.md_5.bungee.api.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;

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

        Player p = (Player) sender;
        //if (p.getServer().getName().equalsIgnoreCase("pvpsoup")) {
        Location spawn = p.getLocation();
        spawn.setWorld(p.getServer().getWorld("world"));
        spawn = spawn.getWorld().getSpawnLocation();
        p.teleport(spawn);
        //}
        /*else
        {

            ByteArrayOutputStream bite = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bite);

            try {
                dos.writeUTF("Connect");
                dos.writeUTF("lobby"); // Le nom du srv
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendPluginMessage(main, "BungeeCord", bite.toByteArray());
        }*/
        return false;
    }
}