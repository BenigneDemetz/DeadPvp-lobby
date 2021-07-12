package net.deadpvp.events;

import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListeners implements Listener {

    @EventHandler
    public void sendMessage(PlayerChatEvent e) {
        Player p = e.getPlayer ();

        String msg = e.getMessage();
        int maj =0;
        int max=1;
        String msgsans = msg;
        for (int k = 0; k < e.getMessage().length(); k++) {
            if (Character.isUpperCase(msg.charAt(k))) {
                maj++;
                char temp = Character.toLowerCase(e.getMessage().charAt(k));
                char x= e.getMessage().charAt(k);
                max++;
                msgsans.replace(x,Character.toLowerCase(x));
            }
        }
        int pourcentage = (maj * 100 )/ max;
        if(pourcentage >= 30){
            msg = msgsans;
        }

        if(msg.startsWith("!!")){
            msg = "§c[AdminChat] "+ UtilityFunctions.getPrefix(e.getPlayer())+e.getPlayer().getName()+"§6: "+e.getMessage();
            msg = msg.replace("!!","");
            e.setCancelled(true);
            for (Player pl : Bukkit.getOnlinePlayers()){
                if(pl.hasPermission("chat.admin") || pl.hasPermission("chat.dev")){
                    pl.sendMessage(msg+"");
                }
            }
            return;
        }
        if(msg.startsWith("!")){
            msg = "§d[StaffChat] "+ UtilityFunctions.getPrefix(e.getPlayer())+e.getPlayer().getName()+"§6: "+e.getMessage();
            msg = msg.replace("!","");
            e.setCancelled(true);
            for (Player pl : Bukkit.getOnlinePlayers()){
                if(pl.hasPermission("chat.admin") || pl.hasPermission("chat.dev") || pl.hasPermission("chat.modo") || pl.hasPermission("chat.builder")){
                    pl.sendMessage(msg+"");
                }
            }
            return;
        }


        Long c = System.currentTimeMillis() + (3 * 1000);

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (e.getMessage().contains(players.getName())) {
                msg = msg.replace(players.getName(), "§b"+players.getName()) + "§f";
                players.playSound(players.getLocation(), Sound.NOTE_PLING, 1, 2);
            }
        }
        if(e.getMessage().contains("@everyone") && (e.getPlayer().hasPermission("chat.admin") || e.getPlayer().hasPermission("chat.dev") || e.getPlayer().hasPermission("chat.modo") || e.getPlayer().hasPermission("chat.builder")) ){
            for(Player player2 : Bukkit.getOnlinePlayers()){
                msg = msg.replace("@everyone","§c§l@everyone§r");
                player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 2);
            }

        }
        e.setFormat(UtilityFunctions.getPrefix(p)+p.getName()+": §f"+msg);

    }
}
