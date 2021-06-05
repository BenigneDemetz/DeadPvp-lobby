package net.DeadPvp.timerstask;

import net.DeadPvp.event.EventListeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTaskUpdate extends BukkitRunnable {
    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() == 0){
            return;
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            EventListeners.updateScoreBoard(player);
        }

    }
}
