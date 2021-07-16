package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.commands.Vanich;
import net.deadpvp.guiManager.PlayerGuiUtils;
import net.deadpvp.guiManager.guis.CompassGui;
import net.deadpvp.guiManager.guis.PnjGui;
import net.deadpvp.scoreboard.ScoreboardUtils;
import net.deadpvp.scoreboard.TabUtils;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.UtilityFunctions;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class PlayerListeners implements Listener {

    private PlayerGuiUtils playerGuiUtils;



    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!p.hasPermission("deadpvp.vanich")){
            for(Player playervanished : Vanich.inVanish){
                p.hidePlayer(playervanished);
            }
        }
        try {
            if (!sqlUtilities.hasData("moneyserv", "player", p.getName())) {
                sqlUtilities.insertData("moneyserv", p.getName(), 0, 0, "player, mystiques, karma");
            }
        }
        catch (Exception ee) {
            ee.printStackTrace();
        }

        e.getPlayer ().setPlayerListName (UtilityFunctions.getPrefix (e.getPlayer ()) + e.getPlayer ().getName ());
        e.getPlayer ().setGameMode (GameMode.SURVIVAL);
        e.setJoinMessage ("");
        e.getPlayer ().teleport (new Location (e.getPlayer ().getWorld (), 0.5, 50, 0.5, 0, 0));
        e.getPlayer ().getInventory ().clear ();
        UtilityFunctions.initLobby (e.getPlayer ());
        TabUtils.settab(e.getPlayer());
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

    @EventHandler
    public void PlayerInteractEntityEvent(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();

        if (entity instanceof Villager && entity.getName().equalsIgnoreCase("§b§lNEW §2§lVote")) {
            PnjGui pnjGui = new PnjGui(Main.getPlayerGuiUtils(p));
            pnjGui.openInv();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack it = e.getItem();
        if (it == null) return;
        if (!e.getAction().equals(Action.PHYSICAL)) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
                e.setCancelled(true);
                CompassGui compassGui = new CompassGui(Main.getPlayerGuiUtils(e.getPlayer()));
                compassGui.openInv();
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
    public void onMove(PlayerMoveEvent e){
        Player p = e .getPlayer ();
        if (p.getLocation().getZ() >= 71.701 && Math.abs(p.getLocation().getX()) <= 12 && p.getLocation().getZ() <= 73)  {
            if(!Main.serveurEnMaintenance.contains("pvpsoup")){
                UtilityFunctions.tpToServ(p, "pvpsoup");
                System.out.println(p.getName()+" est partie sur le pvpsoup.");
                p.setVelocity(new Vector(0,2,-5));
            } else {
                p.sendMessage("§4Serveur en Maintenance.");
                p.setVelocity(new Vector(0,2,-5));
            }
        }
        if(p.getLocation().getX()<=-70.701 && Math.abs(p.getLocation().getZ())<=12 && p.getLocation().getX()<=-70.701){
            if(!Main.serveurEnMaintenance.contains("crea")){
                UtilityFunctions.tpToServ(p, "crea");
                System.out.println(p.getName()+" est partie sur le créatif.");
                p.setVelocity(new Vector(0,2,-5));
            } else {
                p.sendMessage("§4Serveur en Maintenance.");
                p.setVelocity(new Vector(5,2,0));
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }



}
