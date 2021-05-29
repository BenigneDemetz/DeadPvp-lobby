package net.DeadPvp.timerstask;

import net.DeadPvp.event.EventListeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTaskUpdate extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()){
            Bukkit.broadcastMessage("UPDATED");
            EventListeners.updateScoreboard(player);
        }

    }
}
