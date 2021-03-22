package net.DeadPvp.event;

import net.DeadPvp.Main;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.VanichUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class EventListeners implements Listener {

    Main main;
    List<String> bannedCommands = new ArrayList<String>();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String name = e.getPlayer().getName();
        e.setJoinMessage("§2[§4+§a] " + name);
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().setGameMode(GameMode.CREATIVE); //a delete
                e.getPlayer().performCommand("spawn");
            }
        }.runTaskLater(Main.getInstance(),5L);

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        e.setQuitMessage("§c[§4-§d] " + name);
        if (Main.getInstance().vanishedPlayers.contains(p)) {
            Main.getInstance().vanishedPlayers.remove(p);
            Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(p));
            p.sendMessage("§bTu n'est plus en Vanish.");
        }
        if (Main.getInstance().freezedPlayers.contains(p)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "litebans:ban " +
                    name + " déconnexion durant un freeze.");
            System.out.println("DPReport 3 : " + name + " s'est déconnecté en étant freeze.");
            Main.getInstance().freezedPlayers.remove(p);
        }
        if (Main.getInstance().staffModePlayers.contains(p)) {
            AdminInv ai = AdminInv.getFromPlayer(p);
            Main.getInstance().staffModePlayers.remove(p);
            p.getInventory().clear();
            ai.giveInv(p);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        if (Main.getInstance().staffModePlayers.contains(p)) {
            AdminInv ai = AdminInv.getFromPlayer(p);
            Main.getInstance().staffModePlayers.remove(p);
            p.getInventory().clear();
            ai.giveInv(p);
        }
    }

    //Eclair Mort
    @EventHandler
    public void onPDeath(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();
        Location pos = player.getLocation().getBlock().getLocation();
        player.getWorld().strikeLightningEffect(pos);
        Random rdn = new Random();

    } //Eclair Mort


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (Main.getInstance().freezedPlayers.contains(p)) {
            if (e.getTo().getX() != e.getFrom().getX() && e.getTo().getZ() != e.getFrom().getZ()) {
                e.setTo(e.getFrom());
            }
        }
        if (isBetween(p) && !p.getGameMode().equals(GameMode.CREATIVE)){
            p.setVelocity(p.getVelocity().setY(2));
            p.setVelocity(p.getVelocity().setZ(10));
            Main.getInstance().hasJump.add(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Main.getInstance().hasJump.contains(p))
                    Main.getInstance().hasJump.remove(p);
                }
            }.runTaskLater(Main.getInstance(), 100L);
        }
    }

    @EventHandler
    public void onHitGround (EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL) &&
                e.getEntity() instanceof Player && Main.getInstance().hasJump.contains(e.getEntity())){
            e.setCancelled(true);
            Main.getInstance().hasJump.remove(e.getEntity());
        }

    }


    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        if (Main.getInstance().staffModePlayers.contains(e.getPlayer())) {
            e.getPlayer().setGameMode(GameMode.CREATIVE);
            e.getPlayer().setAllowFlight(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType().equals(EntityType.PRIMED_TNT) || e.getEntityType().equals(EntityType.MINECART_TNT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRain(WeatherChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        if (Main.getInstance().didCommandJoin.contains(e.getPlayer())) {
            Main.getInstance().isPlaying.add(e.getPlayer());
            Main.getInstance().didCommandJoin.remove(e.getPlayer());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getPlayer().getInventory().getItem(8) != null) {
                    if (e.getPlayer().getInventory().getItem(8).getItemMeta().getDisplayName().contains("§7") &&
                            e.getPlayer().getHealth() > 0) {
                        int nbChest = e.getPlayer().getInventory().getItem(8).getAmount();
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 20L);
        ////////utiliser database but don't remember comment

    }

    @EventHandler
    public void useCommand(PlayerCommandPreprocessEvent e) {
// use command in pvpsoup
        if (e.getMessage().contains("kb join")) {
            Main.getInstance().didCommandJoin.add(e.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (Main.getInstance().didCommandJoin.contains(e.getPlayer()))
                        Main.getInstance().didCommandJoin.remove(e.getPlayer());
                }
            }.runTaskLater(Main.getInstance(), 60L);
        }
        if (e.getMessage().contains("kb leave")) {
            Main.getInstance().isPlaying.remove(e.getPlayer());
        }
        bannedCommands.addAll(Main.getInstance().getConfig().getStringList("Commandes"));
        if (Main.getInstance().isPlaying.contains(e.getPlayer())) {
            e.setCancelled(bannedCommands.contains(e.getMessage().substring(1)));
            if (e.isCancelled()) e.getPlayer().sendMessage("§cTu ne peux pas utiliser cette commande actuellement !");
        }
//end
    }

    @EventHandler
    public void sendMessage(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        String newMsg = getPrefix(p) + e.getPlayer().getDisplayName() + " §f: ";
        e.setCancelled(true);
        if ((p.hasPermission("dp.modo.chat") || p.hasPermission("dp.modo.*") || p.hasPermission("dp.*") ||
                p.hasPermission("dp.admin.chat") || p.hasPermission("dp.admin.*")) && e.getMessage().startsWith("!")) {

            if (msg.startsWith("!!") && (p.hasPermission("dp.admin.chat") || p.hasPermission("dp.admin.*")
                                                            || p.hasPermission("dp.*"))) {
                msg = msg.substring(2);
                for (Player reciever : Bukkit.getOnlinePlayers()) {
                    if (reciever.hasPermission("dp.admin.chat") || reciever.hasPermission("dp.admin.*") ||
                                                        reciever.hasPermission("dp.*")) {
                        reciever.sendRawMessage("§d[AdminChat] " + newMsg + msg);
                    }
                }
                Bukkit.getConsoleSender().sendMessage("§d[AdminChat] " + newMsg + msg);

            }
            else {
                e.setCancelled(true);
                msg = msg.substring(1);
                for (Player reciever : Bukkit.getOnlinePlayers()) {
                    if (reciever.hasPermission("dp.modo.chat") || reciever.hasPermission("dp.modo.*") ||
                            reciever.hasPermission("dp.*") || reciever.hasPermission("dp.admin.chat") ||
                            reciever.hasPermission("dp.admin.*")) {
                        reciever.sendRawMessage("§d[StaffChat] " + newMsg + msg);
                    }
                }
                Bukkit.getConsoleSender().sendMessage("§d[AdminChat] " + newMsg + msg);
            }
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                players.sendRawMessage(newMsg + msg);
            }
            Bukkit.getConsoleSender().sendMessage(newMsg + msg);
        } 
    }

    @EventHandler
    public void looseFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getServer().getName().contains("pvpsoup") || p.getServer().getName().contains("login")
                    || p.getServer().getName().contains("lobby"))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        String nameServ = e.getPlayer().getServer().getName();
        if (nameServ.contains("lobby") || nameServ.contains("login") || nameServ.contains("pvpsoup")) {
            e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
        }

        e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

    @EventHandler
    public void painting (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player)
        e.setCancelled((e.getEntity().getType().equals(EntityType.PAINTING)  || e.getEntity().getType().equals(EntityType.ITEM_FRAME)) && !((Player) e.getDamager()).getGameMode().equals(GameMode.CREATIVE));
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e){
        Material cb = e.getClickedBlock().getType();
        if (e.getPlayer().getServer().getServerName().contains("pvpsoup") && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(cb.equals(Material.ACACIA_DOOR) || cb.equals(Material.BIRCH_DOOR) ||
                    cb.equals(Material.DARK_OAK_DOOR) || cb.equals(Material.JUNGLE_DOOR) ||
                    cb.equals(Material.SPRUCE_DOOR) || cb.equals(Material.TRAP_DOOR) || cb.equals(Material.WOOD_DOOR)
                    || cb.equals(Material.WOODEN_DOOR) || cb.equals(Material.CHEST) || cb.equals(Material.ENDER_CHEST)
                    || cb.equals(Material.ANVIL) || cb.equals(Material.ENCHANTMENT_TABLE) || cb.equals(Material.WORKBENCH)
                    || cb.equals(Material.JUKEBOX) || cb.equals(Material.NOTE_BLOCK) || cb.equals(Material.FURNACE)
                    || cb.equals(Material.BURNING_FURNACE) || cb.equals(Material.FENCE_GATE));
        }
    }

    public static String getPrefix(Player p) {


        if (p.getEffectivePermissions().contains("displayname.Builder")) return "§a[Builder] §6";
        if (p.hasPermission("displayname.admin")) return "§c[Admin] §6";
        else if (p.hasPermission("displayname.Modo")) return "§b[Modo] §6";
        else if (p.hasPermission("displayname.Builder")) return "§a[Builder] §6";
        else return "§7";

    }

    public static boolean isBetween (Player p) {
        Location loc = p.getLocation();
        FileConfiguration fConf = Main.getInstance().getConfig();
        if (loc.getX() <= fConf.getDouble("Bump.world.1.1.X") &&
                loc.getX() >= fConf.getDouble("Bump.world.1.2.X") &&
                loc.getY() <= fConf.getDouble("Bump.world.1.1.Y") &&
                loc.getY() >= fConf.getDouble("Bump.world.1.2.Y") &&
                loc.getZ() <= fConf.getDouble("Bump.world.1.1.Z") &&
                loc.getZ() >= fConf.getDouble("Bump.world.1.2.Z")){
            return true;
        }
        for (int i = 1; i <= fConf.getInt("Bump.world"); ++i) {
        }
        return false;
    }
}