package net.DeadPvp.event;

import net.DeadPvp.Main;
import net.DeadPvp.utils.AdminInv;
import net.DeadPvp.utils.ItemBuilder;
import net.DeadPvp.utils.UtilityFunctions;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftItemFrame;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;
import java.util.*;


public class EventListeners implements Listener {

    Main main;
    Map<Player, Long> spam = new HashMap<Player, Long>();
    Map<Player, String> doublemsg = new HashMap<Player, String>();
    Map<Player, Boolean> incombat = new HashMap<Player, Boolean>();



    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        doublemsg.put(e.getPlayer(),null);
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
        setScoreBoard(e.getPlayer());
        e.getPlayer().setWalkSpeed((float) 0.4);
        if (e.getPlayer().hasPermission("chat.builder") ||e.getPlayer().hasPermission("chat.modo") || e.getPlayer().hasPermission("chat.admin") || e.getPlayer().hasPermission("chat.dev") ){
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
        if(p.getLocation().getX()<=-70.701 && Math.abs(p.getLocation().getZ())<=12 && p.getLocation().getX()<=-70.701){
            p.setVelocity(new Vector(5,2,0));
        }
    }

    @EventHandler
    public void onHitGround (EntityDamageEvent e) {e.setCancelled(e.getEntity().getType().equals(EntityType.PLAYER));}

    @EventHandler
    public void onClickInv (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();
        e.setCancelled(!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE));
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
            msg = "§c[AdminChat] "+getPrefixColor(e.getPlayer())+e.getPlayer().getName()+"§6: "+e.getMessage();
            msg = msg.replace("!!","");
            e.setCancelled(true);
            for (Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("chat.admin") || p.hasPermission("chat.dev")){

                    p.sendMessage(msg+"");
                }
            }
            return;
        }
        if(msg.startsWith("!")){
            msg = "§d[StaffChat] "+getPrefixColor(e.getPlayer())+e.getPlayer().getName()+"§6: "+e.getMessage();
            msg = msg.replace("!","");
            e.setCancelled(true);
            for (Player p : Bukkit.getOnlinePlayers()){
                if(p.hasPermission("chat.admin") || p.hasPermission("chat.dev") || p.hasPermission("chat.modo") || p.hasPermission("chat.builder")){
                    p.sendMessage(msg+"");
                }
            }
            return;
        }


        if (msg.equals("[event cancelled by LiteBans")) return;
        Player p = e.getPlayer();
        if (spam.containsKey(p)) {
            if (spam.get(p) > System.currentTimeMillis()) {
                p.sendMessage("§cErreur : tu dois attendre entre chaque message !");
                e.setCancelled(true);
                return;
            }

        }
        Long c = System.currentTimeMillis() + (3 * 1000);
        if (!(p.hasPermission("chat.dev") || p.hasPermission("chat.admin") || p.hasPermission("chat.modo") || p.hasPermission("chat.builder"))) {
            spam.put(p, c);
        }
        if (e.getMessage().equalsIgnoreCase(doublemsg.get(p.getPlayer()))) {
            p.sendMessage("§cErreur : impossible d'envoyer 2 fois le même message d'affilé !");
            e.setCancelled(true);
            return;
        }
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
        doublemsg.put(e.getPlayer(), e.getMessage());
        e.setFormat(getPrefix(p)+p.getName()+": §f"+msg);

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
        if (command.equalsIgnoreCase("help")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§b§lHELP :" +
                                         "§6/hub :§f Téléportation au hub " +
                                         "§6/suggest :§f");
        }

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
        if(p.getName() == "Red_Spash") return "§5[Développeur] §5";
        if (p.hasPermission("chat.admin")) return "§c[Administrateur] §c";
        if (p.hasPermission("chat.dev")) return "§5[Développeur] §5";
        if (p.hasPermission("chat.modo")) return "§6[Modérateur] §6";
        if (p.hasPermission("chat.builder")) return "§9[Builder] §9";
        if (p.hasPermission("chat.vip")) return "§b[VIP] §b";
        else return "§7";
    }

    public static String getPrefixColor(Player p) {
        if (p.hasPermission("chat.admin")) return "§c";
        if (p.hasPermission("chat.dev")) return "§5";
        if (p.hasPermission("chat.modo")) return "§6";
        if (p.hasPermission("chat.builder")) return "§9";
        if (p.hasPermission("chat.vip")) return "§b";
        else return "§7";
    }

    public static String getPrefixname(Player p) {
        if (p.hasPermission("chat.admin")) return "§cAdministrateur";
        if (p.hasPermission("chat.dev")) return "§5Développeur";
        if (p.hasPermission("chat.modo")) return "§6Modérateur";
        if (p.hasPermission("chat.builder")) return "§9Builder";
        if (p.hasPermission("chat.vip")) return "§bVIP";
        else return "§7Joueur";
    }

    public static void compassEvent (Event e, Player player, ItemStack it) {
        if(it.getType()==Material.COMPASS && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§2§lSelection du mode de jeu")) {
            Inventory inv = Bukkit.createInventory(null, 36,"§2§lSelection du mode de jeu");
            List<String> lorecrea = Arrays.asList("§bDescription :", "  §7Construisez seul ou entre amis","  §7le plot de vos rêves !","§f ", "§4§lEn maintenance");
            ItemBuilder grass = new ItemBuilder(Material.GRASS).setLore(lorecrea).setName("§d§lCREATIF §c§l[EN MAINTENANCE]");
            inv.setItem(11, UtilityFunctions.iSDeleteDatas(grass.toItemStack()));

            List<String> lorepvp = Arrays.asList("§bDescription :", "  §7Un mode de jeu classique de DEADPVP ! ","  §7Combattez vos ennemis dans une map où les soupes","  §7peuvent vous sauver la vie !");
            ItemBuilder sword = new ItemBuilder(Material.DIAMOND_SWORD).setName("§c§lPVP§9§lSOUP").addEnchant(Enchantment.ARROW_FIRE, 1).setLore(lorepvp);
            inv.setItem(13, UtilityFunctions.iSDeleteDatas(sword.toItemStack()));

            List<String> loremaintenance = Arrays.asList("§bDescription :", "  §cEn maintenance !", "");
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
        onlineCounter.setPrefix("§b"+Main.getInstance().playerCount);
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
        int max = getmaxco();
        board.getTeam("onlineCounter").setPrefix("§b"+players);
        board.getTeam("maxco").setPrefix("§b§l"+max);
        board.getTeam("grade").setPrefix(getPrefixname(player));




    }

    public static int getmaxco(){
        String temp;
        int maxco = 0;
        try {
            Scanner sc = new Scanner(new File("/home/ubuntu/server/lobby_serv/plugins/DEADPVP/maxco.txt"));
            String[] tab = sc.nextLine().split(",");
            temp = tab[0];
            maxco = Integer.parseInt(temp);
            sc.close();

        }catch (IOException e){
            Bukkit.getConsoleSender().sendMessage("§c§lWarning : le fichier maxco.txt n'éxiste plus ! Merci de le créer ici : /home/ubuntu/server/lobby_serv/plugins/DEADPVP/");
        }

        return maxco;

    }

}