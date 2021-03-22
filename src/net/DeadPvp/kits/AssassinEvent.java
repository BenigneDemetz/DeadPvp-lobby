package net.DeadPvp.kits;

import me.wazup.kitbattle.PlayerSelectKitEvent;
import net.DeadPvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class AssassinEvent implements Listener {

    boolean isLeaving = false;

    @EventHandler
    public void onSelectKit (PlayerSelectKitEvent e){
        if (e.getKit().getName().equalsIgnoreCase("assassin")){
            Main.getInstance().hasKit.put(e.getPlayer(),"assassin");
        }
    }

    @EventHandler
    public void onSneak (PlayerToggleSneakEvent e){
        if (Main.getInstance().hasKit.get(e.getPlayer()) == "assassin"){

        }
    }

    @EventHandler
    public void onLeave (PlayerCommandPreprocessEvent e){
        if (e.getMessage().contains("kb leave")){
            Main.getInstance().hasKit.remove(e.getPlayer());
        }
    }
}
