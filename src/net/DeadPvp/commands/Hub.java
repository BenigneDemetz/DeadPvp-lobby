package net.DeadPvp.commands;

import net.DeadPvp.Main;
import net.DeadPvp.utils.UtilityFunctions;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

                    p.getInventory().clear();

                    UtilityFunctions.initLobby(p);

                }
            }.runTaskLater(main, 5);
        }
        else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        return false;
    }
}
