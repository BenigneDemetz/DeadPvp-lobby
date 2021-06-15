package net.deadpvp.events;

import net.deadpvp.Main;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.UtilityFunctions;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class EventListeners implements Listener {
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
    
    public static void compassEvent (Event e, Player player, ItemStack it) {
        if(it.getType()==Material.COMPASS && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§2§lSelection du mode de jeu")) {
            Inventory inv = Bukkit.createInventory (null, 36, "§2§lSelection du mode de jeu");
            List<String> lorecrea = Arrays.asList ("§bDescription :", "  §7Construisez seul ou entre amis", "  §7le plot de vos rêves !", "§f ", "§4§lEn maintenance");
            ItemBuilder grass = new ItemBuilder (Material.GRASS).setLore (lorecrea).setName ("§d§lCREATIF §c§l[EN MAINTENANCE]");
            inv.setItem (11, UtilityFunctions.iSDeleteDatas (grass.toItemStack ()));
    
            List<String> lorepvp = Arrays.asList ("§bDescription :", "  §7Un mode de jeu classique de DEADPVP ! ", "  §7Combattez vos ennemis dans une map où les soupes", "  §7peuvent vous sauver la vie !");
            ItemBuilder sword = new ItemBuilder (Material.DIAMOND_SWORD).setName ("§c§lPVP§9§lSOUP").addEnchant (Enchantment.ARROW_FIRE, 1).setLore (lorepvp);
            inv.setItem (13, UtilityFunctions.iSDeleteDatas (sword.toItemStack ()));
    
            List<String> loremaintenance = Arrays.asList ("§bDescription :", "  §cEn maintenance !", "");
            ItemBuilder barrier = new ItemBuilder (Material.BARRIER).setName ("§4Maintenance").setLore (" ");
            inv.setItem (15, UtilityFunctions.iSDeleteDatas (barrier.toItemStack ()));
    
            ItemBuilder paper = new ItemBuilder (Material.PAPER).setName ("§d§lSite de DeadPVP").setLore (" ");
            inv.setItem (31, UtilityFunctions.iSDeleteDatas (paper.toItemStack ()));
    
            for (int i = 0; i < inv.getSize (); ++i) {
                if (inv.getItem (i) == null) {
                    inv.setItem (i, UtilityFunctions.voidItem (DyeColor.BROWN));
                }
            }
    
            player.openInventory (inv);
    
        }
    }
    @EventHandler
    public void onDrop (PlayerDropItemEvent e) {
        e.setCancelled(true);
    }
    
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
    
    public static String getPrefix(Player p) {
        if (p.hasPermission("chat.admin")) return "§c[Administrateur] §c";
        if (p.hasPermission("chat.dev")) return "§5[Développeur] §5";
        if (p.hasPermission("chat.modo")) return "§6[Modérateur] §6";
        if (p.hasPermission("chat.builder")) return "§9[Builder] §9";
        //if (p.hasPermission("chat.swag")) return "§4[§cS§eW§aA§bG§9] §4";
        if (p.hasPermission("chat.vip")) return "§b[VIP] §b";
        else return "§7";
    }
    public static String getPrefixname(Player p) {
        if (p.hasPermission("chat.admin")) return "§cAdministrateur";
        if (p.hasPermission("chat.dev")) return "§5Développeur";
        if (p.hasPermission("chat.modo")) return "§6Modérateur";
        if (p.hasPermission("chat.builder")) return "§9Builder";
        //if (p.hasPermission("chat.swag")) return "§nSWAG";
        if (p.hasPermission("chat.vip")) return "§bVIP";
        else return "§7Joueur";
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e){
       Player p = e .getPlayer ();
        if (p.getLocation().getZ() >= 71.701 && Math.abs(p.getLocation().getX()) <= 12 && p.getLocation().getZ() <= 73)
        {
            UtilityFunctions.tpToServ(p, "pvpsoup");
            System.out.println(p.getName()+" est partie sur le pvpsoup.");
            p.setVelocity(new Vector (0,2,-5));
        }
        if(p.getLocation().getX()<=-70.701 && Math.abs(p.getLocation().getZ())<=12 && p.getLocation().getX()<=-70.701){
            p.setVelocity(new Vector(5,2,0));
        }
    }
    public static void setScoreBoard(Player player) {
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 59-date.getMinutes();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("§4§lDEAD§1§lPVP", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score14 = obj.getScore("§b§l---------------§r");
        Score score13 = obj.getScore("§5§l§r ");
        Score score12 = obj.getScore("§6>>> Connectés :");
        //                              "x joueurs"
        Score score10 = obj.getScore("§4§l ");
        Score score9 = obj.getScore("§6>>> Connexion simultanée :");
        //                              "nbr de co "
        Score score7 = obj.getScore("§5§l§r§c§l ");
        Score score6 = obj.getScore("§6>>> §6Votre grade :");
        //                             "GRADE "
        Score score4 = obj.getScore("§5§l§c§r ");
        Score score3 = obj.getScore("§dEn cas de bug faites");
        Score score2 = obj.getScore("§d/bug <votre bug> !");
        Score score1 = obj.getScore("§c§b§l---------------§r");
        Score score0 = obj.getScore("§c§lmc.deadpvp.fr");
        
        score0.setScore(0);
        score1.setScore(1);
        score2.setScore(2);
        score3.setScore(3);
        score4.setScore(4);
        score6.setScore(6);
        score7.setScore(7);
        score10.setScore(10);
        score9.setScore(9);
        score14.setScore(14);
        score13.setScore(13);
        score12.setScore(12);
        
        Team grade = board.registerNewTeam("grade");
        grade.addEntry(ChatColor.BLACK + "" + ChatColor.MAGIC);
        grade.setPrefix(getPrefixname(player));
        obj.getScore(ChatColor.BLACK + "" + ChatColor.MAGIC).setScore(5);
        
        Team onlineCounter = board.registerNewTeam("onlineCounter");
        onlineCounter.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC);
        onlineCounter.setPrefix("§b"+ Main.getInstance().playerCount);
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC).setScore(11);

        Team maxco = board.registerNewTeam("maxco");
        maxco.addEntry(ChatColor.BLACK + "" + ChatColor.WHITE);
        maxco.setPrefix("§bRecord : §b§l0");
        obj.getScore(ChatColor.BLACK + "" + ChatColor.WHITE).setScore(8);
        
        player.setScoreboard(board);
    }
    
    public static void updateScoreBoard(Player player) {
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 59-date.getMinutes();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        int players = Main.getInstance().playerCount;
        Scoreboard board = player.getScoreboard();
        int max = UtilityFunctions.getmaxco();
        board.getTeam("onlineCounter").setPrefix("§b"+players);
        board.getTeam("maxco").setPrefix("§b§l"+max);
        board.getTeam("grade").setPrefix(getPrefixname(player));
        
        
    }
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        e.setCancelled (true);
    }
    
    @EventHandler
    public void onWeather(WeatherChangeEvent e) {e.setCancelled (true);}
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!e.getPlayer ().isOp ()) e.setCancelled (true);
    }
    
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!e.getPlayer ().isOp ()) e.setCancelled (true);
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer ().setPlayerListName (getPrefix (e.getPlayer ()) + e.getPlayer ().getName ());
        e.getPlayer ().setGameMode (GameMode.SURVIVAL);
        e.setJoinMessage ("");
        e.getPlayer ().teleport (new Location (e.getPlayer ().getWorld (), 0.5, 50, 0.5, 0, 0));
        e.getPlayer ().getInventory ().clear ();
        UtilityFunctions.initLobby (e.getPlayer ());
        setScoreBoard (e.getPlayer ());
        e.getPlayer ().setWalkSpeed ((float) 0.4);
        if (e.getPlayer ().hasPermission ("chat.builder") || e.getPlayer ().hasPermission ("chat.modo") || e.getPlayer ().hasPermission ("chat.admin") || e.getPlayer ().hasPermission ("chat.dev")) {
            Bukkit.broadcastMessage ("§7[§4§lD§9§lP§7] " + getPrefix (e.getPlayer ()) + e.getPlayer ().getName () + " §6vient de rejoindre le lobby !");
            e.getPlayer ().getWorld ().strikeLightningEffect (e.getPlayer ().getLocation ());
        }
        for(Player pls : Bukkit.getOnlinePlayers ()){
            if(Main.getInstance ().hidePlayerOn.contains (pls)){
                pls.hidePlayer (e.getPlayer ());
            }
        }
    }
    
    
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
            msg = "§c[AdminChat] "+getPrefix(e.getPlayer())+e.getPlayer().getName()+"§6: "+e.getMessage();
            msg = msg.replace("!!","");
            e.setCancelled(true);
            for (Player pl : Bukkit.getOnlinePlayers()){
                if(pl.hasPermission("chat.admin") || pl.hasPermission("chat.dev")){
                    
                    p.sendMessage(msg+"");
                }
            }
            return;
        }
        if(msg.startsWith("!")){
            msg = "§d[StaffChat] "+getPrefix(e.getPlayer())+e.getPlayer().getName()+"§6: "+e.getMessage();
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
                msg = msg.replace("@everyone","§c§l@everyone");
                player2.playSound(player2.getLocation(), Sound.NOTE_PLING, 1, 2);
            }
            
        }
        e.setFormat(getPrefix(p)+p.getName()+": §f"+msg);
        
    }
}
