package net.DeadPvp.event;

import net.DeadPvp.Main;
import net.DeadPvp.timerstask.TimerTaskUpdate;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.ItemBuilder;
import net.DeadPvp.utils.UtilityFunctions;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.minecraft.server.v1_8_R1.ScoreboardScore;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftItemFrame;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;
import us.myles.ViaVersion.ViaVersionPlugin;
import us.myles.ViaVersion.api.command.ViaVersionCommand;

import java.util.Date;
import java.util.TimeZone;


public class EventListeners implements Listener {

    Main main;


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setPlayerListName(getPrefix(e.getPlayer())+e.getPlayer().getName());
        e.getPlayer().setGameMode(GameMode.SURVIVAL);
        e.setJoinMessage("");
        Location spawn = e.getPlayer().getLocation();
        spawn.setX(0.5);
        spawn.setY(50);
        spawn.setZ(0.5);
        spawn.setYaw(0);
        spawn.setPitch(0);
        e.getPlayer().teleport(spawn);
        e.getPlayer().getInventory().clear();
        UtilityFunctions.initLobby(e.getPlayer());
        setScoreboard(e.getPlayer());
        e.getPlayer().setWalkSpeed((float) 0.4);
        if (e.getPlayer().hasPermission("chat.modo") || e.getPlayer().hasPermission("chat.admin") || e.getPlayer().hasPermission("chat.dev") ){
            Bukkit.broadcastMessage("§7[§4§lD§9§lP§7] "+getPrefix(e.getPlayer())+e.getPlayer().getName()+" §6vient de rejoindre le lobby !");
            e.getPlayer().getWorld().strikeLightningEffect(e.getPlayer().getLocation());
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();
        e.setQuitMessage("");
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
    } //Eclair Mort


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (Main.getInstance().freezedPlayers.contains(p)) {
            if (e.getTo().getX() != e.getFrom().getX() && e.getTo().getZ() != e.getFrom().getZ()) {
                e.setTo(e.getFrom());
            }
        }
        if (p.getLocation().getZ() >= 71.701 && Math.abs(p.getLocation().getX()) <= 12 && p.getLocation().getZ() <= 73)
        {
            UtilityFunctions.tpToServ(p, "pvpsoup");
            System.out.println(p.getName()+" est partie sur le pvpsoup.");
            p.setVelocity(new Vector(0,2,-5));
        }
    }

    @EventHandler
    public void onHitGround (EntityDamageEvent e) {e.setCancelled(e.getEntity().getType().equals(EntityType.PLAYER));}

    @EventHandler
    public void onClickInv (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();
        e.setCancelled(true);
        if(p.getOpenInventory().getTitle().equalsIgnoreCase("§2§lSelection du mode de jeu")) {
            switch(current.getType()) {
                case DIAMOND_SWORD:
                    UtilityFunctions.tpToServ((Player) e.getWhoClicked(), "pvpsoup");
                    return;
                case GRASS:
                    Player player = (Player) e.getWhoClicked();
                    if (player.hasPermission("chat.admin") || player.hasPermission("chat.dev") || player.hasPermission("chat.builder")){

                        UtilityFunctions.tpToServ((Player) e.getWhoClicked(), "crea");
                        return;
                    }else{
                        player.closeInventory();
                        player.sendMessage("§cLe serveur est en maintenance !");
                        return;
                    }
                case PAPER:
                    if (e.getCurrentItem().getType() != null && e.getCurrentItem().getItemMeta().getDisplayName() == "§d§lSite de DeadPVP"){
                        e.getWhoClicked().sendMessage("§2§ldeadpvp.fr");
                        e.getWhoClicked().closeInventory();
                        return;
                    }
                default:
                    break;
            }

        }




    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType().equals(EntityType.PRIMED_TNT) || e.getEntityType().equals(EntityType.MINECART_TNT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRain(WeatherChangeEvent e) { e.setCancelled(true); }

    @EventHandler
    public void sendMessage(PlayerChatEvent e) {
        try {
            Player p = e.getPlayer();
            String msg = e.getMessage();
            String newMsg = getPrefix(p) + e.getPlayer().getDisplayName() + " §f: ";
            e.setCancelled(true);
            if (msg.equals("[event cancelled by LiteBans]")) return;
            if ((p.hasPermission("chat.admin") || p.hasPermission("chat.dev") || p.hasPermission("chat.modo")) && e.getMessage().startsWith("!")) {
                if (msg.startsWith("!!") && (p.hasPermission("chat.admin") || p.hasPermission("dp.admin.*") || p.hasPermission("dp.*"))) {
                    msg = msg.substring(2);
                    for (ProxiedPlayer reciever : BungeeCord.getInstance().getPlayers()) {
                        if (reciever.hasPermission("chat.admin") || reciever.hasPermission("dp.admin.*") ||
                                reciever.hasPermission("dp.*")) {
                            String msgAdminStr = "§d[AdminChat] " + newMsg + msg;
                            BaseComponent msgAdminBC = null;
                            msgAdminBC.addExtra(msgAdminStr);
                            reciever.sendMessage(ChatMessageType.CHAT, msgAdminBC);
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage("§d[AdminChat] " + newMsg + msg);

                } else {
                    e.setCancelled(true);
                    msg = msg.substring(1);
                    for (Player reciever : Bukkit.getOnlinePlayers()) {
                        if (reciever.hasPermission("chat.admin") || reciever.hasPermission("chat.dev") || reciever.hasPermission("chat.modo")) {
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
        } catch (Exception ee) {
        }
    }

    @EventHandler
    public void looseFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

    @EventHandler
    public void painting (EntityDamageByEntityEvent e) {
        e.setCancelled(true);
        try {
            if (e.getDamager() instanceof Player)
                e.setCancelled(((e.getEntity().getType().equals(EntityType.PAINTING) ||
                        e.getEntity().getType().equals(EntityType.ITEM_FRAME)) &&
                        !((Player) e.getDamager()).getGameMode().equals(GameMode.CREATIVE)) ||
                        e.getEntity().getType().equals(EntityType.ENDER_CRYSTAL));
        }
        catch (Exception ee){}
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack it = e.getItem();
        if(it == null) return;
        if (!e.getAction().equals(Action.PHYSICAL)) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
                e.setCancelled(true);
                compassEvent(e, player, it);
            } else if (e.getPlayer().getItemInHand().getType().equals(Material.EYE_OF_ENDER)
                    || e.getPlayer().getItemInHand().getType().equals(Material.ENDER_PEARL)) {
                e.setCancelled(true);
                UtilityFunctions.showHidePlayers(player);
            }
            else if (e.getPlayer().getItemInHand().getType().equals(Material.BLAZE_POWDER))
            {
                player.performCommand("trails");
            }
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() == Main.getInstance().robert)
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage("vote");
        }
        else if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
        {
            if(e.getRightClicked() instanceof ItemFrame ||
                    e.getRightClicked() instanceof CraftItemFrame ||
                    e.getRightClicked().getType().equals(EntityType.ITEM_FRAME))
            {
                ItemFrame itemframe = (ItemFrame) e.getRightClicked();
                itemframe.setRotation(Rotation.COUNTER_CLOCKWISE_45);
            }
        }
    }

    @EventHandler
    public void onDrop (PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onCommand (PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        if((command.equalsIgnoreCase("/pl") || command.equalsIgnoreCase("/plugins")
        || command.equalsIgnoreCase("/list") || command.equalsIgnoreCase("/me") ||
                command.equalsIgnoreCase("/help")) && !e.getPlayer().hasPermission("deadpvp.unrestrictedchat"))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage("Commande inconnue");
        }
        try {
            Player p = e.getPlayer();
            if (main.specItemMode.contains(p) && e.getMessage().contains("clear") && (e.getMessage().length() <= 8 || e.getMessage().
                                                                        contains(p.getName()))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.performCommand("spec");
                    }
                }.runTaskLater(Main.getInstance(), 10);
            }
        }
        catch (Exception exception)
        {}
    }

    @EventHandler
    public void onBlockMouvement(BlockFromToEvent e){
        e.setCancelled(Main.getInstance().stopLag);}

    @EventHandler
    public void onPlaceBlock (BlockPlaceEvent e) {
        e.setCancelled(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE));
    }

    public static String getPrefix(Player p) {
        if (p.hasPermission("chat.admin")) return "§c[Administrateur] §c";
        if (p.hasPermission("chat.dev")) return "§5[Développeur] §5";
        if (p.hasPermission("chat.modo")) return "§6[Modérateur] §6";
        if (p.hasPermission("chat.builder")) return "§9[Builder] §9";
        if (p.hasPermission("chat.swag")) return "§4[§cS§eW§aA§bG§9] §4";
        if (p.hasPermission("chat.vip")) return "§b[VIP] §b";
        else return "§7";
    }


    public static void compassEvent (Event e, Player player, ItemStack it) {
        if(it.getType()==Material.COMPASS && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§2§lSelection du mode de jeu")) {
            Inventory inv = Bukkit.createInventory(null, 36,"§2§lSelection du mode de jeu");
            ItemBuilder grass = new ItemBuilder(Material.GRASS).setLore(" ").
                            setName("§d§lCREATIF §c§l[EN MAINTENANCE]");
            inv.setItem(11, UtilityFunctions.iSDeleteDatas(grass.toItemStack()));
            ItemBuilder sword = new ItemBuilder(Material.DIAMOND_SWORD).setName("§c§lPVP§9§lSOUP").
                    addEnchant(Enchantment.ARROW_FIRE, 1).setLore(" ");
            inv.setItem(13, UtilityFunctions.iSDeleteDatas(sword.toItemStack()));
            ItemBuilder barrier = new ItemBuilder(Material.BARRIER).setName("§4Maintenance").setLore(" ");
            inv.setItem(15, UtilityFunctions.iSDeleteDatas(barrier.toItemStack()));
            ItemBuilder paper = new ItemBuilder(Material.PAPER).setName("§d§lSite de DeadPVP").setLore(" ");
            inv.setItem(31, UtilityFunctions.iSDeleteDatas(paper.toItemStack()));

            for (int i = 0; i<inv.getSize(); ++i){
                if (inv.getItem(i) == null) {
                    inv.setItem(i, UtilityFunctions.voidItem(DyeColor.BROWN));
                }
            }

            player.openInventory(inv);


        }
    }
    public static void updateScoreboard(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("DPScoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§4§lDEAD§1§lPVP");
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 60-date.getMinutes();
        int nbrjoueur = Bukkit.getOnlinePlayers().size();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        Score score14 = objective.getScore("§b§l---------------§r");
        Score score13 = objective.getScore("§5§l§r ");
        Score score12 = objective.getScore("§6>>> Connectés :");
        Score score11 = objective.getScore("    §c"+nbrjoueur+"§6 joueurs");
        Score score10 = objective.getScore("§4§l ");
        Score score9 = objective.getScore("§6>>> §6Phase Beta :");
        Score score8;
        Score score7;
        if (date.getHours() <=15){
            int xv2 = (16-date.getHours())-1;
            int yv2 = 60 - date.getMinutes();
            score8 = objective.getScore("§b§l   Ouverture de la beta");
            score7 = objective.getScore("§b§l   dans "+xv2+"h"+yv2);
        }else {
            score8 = objective.getScore("§b§l   Fin de la beta");
            score7 = objective.getScore("§b§l   dans " + x + "h" + y2);
        }
        Score score6 = objective.getScore("§7§l§r ");
        Score score5 = objective.getScore("§6>>> DP :");
        Score score4 = objective.getScore("    §cA venir ...");
        Score score3 = objective.getScore("§f§l");;
        Score score2 = objective.getScore("§b§l---------------");
        Score score1 = objective.getScore("§c§l§r ");
        Score score0 = objective.getScore("§cmc.deadpvp.fr");

        score14.setScore(14);
        score13.setScore(13);
        score12.setScore(12);
        score11.setScore(11);
        score10.setScore(10);
        score9.setScore(9);
        score8.setScore(8);
        score7.setScore(7);
        score6.setScore(6);
        score5.setScore(5);
        score4.setScore(4);
        score3.setScore(3);
        score2.setScore(2);
        score1.setScore(1);
        score0.setScore(0);
        player.setScoreboard(board);


    }
    public void setScoreboard(Player p){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("DPScoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§4§lDEAD§1§lPVP");
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 60-date.getMinutes();
        int nbrjoueur = Bukkit.getOnlinePlayers().size();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        Score score14 = objective.getScore("§b§l---------------§r");
        Score score13 = objective.getScore("§5§l§r ");
        Score score12 = objective.getScore("§6>>> Connectés :");
        Score score11 = objective.getScore("    §c"+nbrjoueur+"§6 joueurs");
        Score score10 = objective.getScore("§4§l ");
        Score score9 = objective.getScore("§6>>> §6Phase Beta :");
        Score score8;
        Score score7;
        if (date.getHours() <=15){
            int xv2 = (16-date.getHours())-1;
            int yv2 = 60 - date.getMinutes();
            score8 = objective.getScore("§b§l   Ouverture de la beta");
            score7 = objective.getScore("§b§l   dans "+xv2+"h"+yv2);
        }else {
            score8 = objective.getScore("§b§l   Fin de la beta");
            score7 = objective.getScore("§b§l   dans " + x + "h" + y2);
        }
        Score score6 = objective.getScore("§7§l§r ");
        Score score5 = objective.getScore("§6>>> DP :");
        Score score4 = objective.getScore("    §cA venir ...");
        Score score3 = objective.getScore("§f§l");;
        Score score2 = objective.getScore("§b§l---------------");
        Score score1 = objective.getScore("§c§l§r ");
        Score score0 = objective.getScore("§cmc.deadpvp.fr");

        score14.setScore(14);
        score13.setScore(13);
        score12.setScore(12);
        score11.setScore(11);
        score10.setScore(10);
        score9.setScore(9);
        score8.setScore(8);
        score7.setScore(7);
        score6.setScore(6);
        score5.setScore(5);
        score4.setScore(4);
        score3.setScore(3);
        score2.setScore(2);
        score1.setScore(1);
        score0.setScore(0);
        p.setScoreboard(board);
    }
}