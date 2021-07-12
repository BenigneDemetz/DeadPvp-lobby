package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.guiManager.PlayerGuiUtils;
import net.deadpvp.guiManager.guis.CompassGui;
import net.deadpvp.scoreboard.ScoreboardUtils;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PlayerListeners implements Listener {

    private CompassGui gui = new CompassGui();
    private PlayerGuiUtils playerGuiUtils;



    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(" ");
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack it = e.getItem();
        if (it == null) return;
        if (!e.getAction().equals(Action.PHYSICAL)) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
                e.setCancelled(true);
                gui.openInv(playerGuiUtils.getPlayer());
            } else if (e.getPlayer().getItemInHand().getType().equals(Material.EYE_OF_ENDER)
                    || e.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                e.setCancelled(true);
                UtilityFunctions.showHidePlayers(player);
            } else if (e.getPlayer().getItemInHand().getType().equals(Material.BLAZE_POWDER)) {
                player.performCommand("trails");
            }
        }
    }

    @EventHandler
    public void onDrop (PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e .getPlayer ();
        if (p.getLocation().getZ() >= 71.701 && Math.abs(p.getLocation().getX()) <= 12 && p.getLocation().getZ() <= 73)
        {
            UtilityFunctions.tpToServ(p, "pvpsoup");
            System.out.println(p.getName()+" est partie sur le pvpsoup.");
            p.setVelocity(new Vector(0,2,-5));
        }
        if(p.getLocation().getX()<=-70.701 && Math.abs(p.getLocation().getZ())<=12 && p.getLocation().getX()<=-70.701){
            p.setVelocity(new Vector(5,2,0));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        e.getPlayer ().setPlayerListName (UtilityFunctions.getPrefix (e.getPlayer ()) + e.getPlayer ().getName ());
        e.getPlayer ().setGameMode (GameMode.SURVIVAL);
        e.setJoinMessage ("");
        e.getPlayer ().teleport (new Location(e.getPlayer ().getWorld (), 0.5, 50, 0.5, 0, 0));
        e.getPlayer ().getInventory ().clear ();
        UtilityFunctions.initLobby (e.getPlayer ());
        ScoreboardUtils.setScoreBoard (e.getPlayer ());
        e.getPlayer ().setWalkSpeed ((float) 0.4);
        if (e.getPlayer ().hasPermission ("chat.builder") || e.getPlayer ().hasPermission ("chat.modo") || e.getPlayer ().hasPermission ("chat.admin") || e.getPlayer ().hasPermission ("chat.dev")) {
            Bukkit.broadcastMessage ("§7[§4§lD§9§lP§7] " + UtilityFunctions.getPrefix (e.getPlayer ()) + e.getPlayer ().getName () + " §6vient de rejoindre le lobby !");
            e.getPlayer ().getWorld ().strikeLightningEffect (e.getPlayer ().getLocation ());
        }
        for(Player pls : Bukkit.getOnlinePlayers ()){
            if(Main.getInstance ().hidePlayerOn.contains (pls)){
                pls.hidePlayer (e.getPlayer ());
            }
        }
    }
}