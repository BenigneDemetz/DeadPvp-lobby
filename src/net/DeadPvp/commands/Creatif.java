package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.UtilityFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.myles.*;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.ViaAPI;

public class Creatif implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (Via.getAPI().getPlayerVersion(sender) >= 736) UtilityFunctions.tpToServ((Player)sender, "crea");
            else sender.sendMessage("§cTon jeu n'est pas en 1.16.1");
        } else sender.sendMessage("Tu dois etre joueur");


        return false;
    }
}