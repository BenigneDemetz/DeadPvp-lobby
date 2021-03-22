package net.DeadPvp.utils;

import net.DeadPvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VanichUtil {

    public static Object Vanich(Player p){
        if (Main.getInstance ().vanishedPlayers.contains (p)){
            Main.getInstance().vanishedPlayers.remove(p);
            Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(p));
            p.sendMessage("§bTu n'est plus en Vanish.");
            System.out.println("DPReport 1 : " + p.getName() + " n'est plus en Vanich.");
        } else {
            Main.getInstance().vanishedPlayers.add(p);
            Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(p));
            p.sendMessage("§bTu es maintenant en Vanish.");
            System.out.println("DPReport 2 : " + p.getName() + " est en Vanich.");
        }
        return p;
    }


}