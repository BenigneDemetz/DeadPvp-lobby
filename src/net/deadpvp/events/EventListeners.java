package net.deadpvp.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.weather.WeatherChangeEvent;


public class EventListeners implements Listener {


    @EventHandler
    public void command(PlayerCommandPreprocessEvent e){
        if (( e.getMessage().startsWith("/minecraft:list") || e.getMessage().startsWith("/list") || e.getMessage().startsWith("/pl")
                || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/help")|| e.getMessage().startsWith("/bukkit:pl")
                || e.getMessage().startsWith("/bukkit:plugins") || e.getMessage().startsWith("/bukkit:?") || e.getMessage().startsWith("/?")
                || e.getMessage().startsWith("/bukkit:help"))){
            e.setCancelled(true);
            e.getPlayer().sendMessage("§fCommande inconnue.");
        }
    }

    @EventHandler
    public void FallDamage(EntityDamageEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        e.setCancelled (true);
    }
    
    @EventHandler
    public void onWeather(WeatherChangeEvent e) {e.setCancelled (true);}
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!e.getPlayer ().isOp () && e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled (true);
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!e.getPlayer ().isOp () && e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled (true);
    }

}
