package net.DeadPvp;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.commands.*;
import net.DeadPvp.commands.World;
import net.DeadPvp.timerstask.TimerTaskUpdate;
import net.DeadPvp.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class Main extends JavaPlugin implements Listener, PluginMessageListener {


    public ArrayList<Player> vanishedPlayers = new ArrayList<Player>();
    public ArrayList<Player> staffModePlayers = new ArrayList<Player>();
    public HashMap<Player, AdminInv> adminPlayerHashmap = new HashMap<>();
    public ArrayList<Player> freezedPlayers = new ArrayList<Player>();
    public ArrayList<Player> onlinePlayers = new ArrayList<Player>();
    public ArrayList<Player> isWaiting = new ArrayList<>();
    public ArrayList<Player> specItemMode = new ArrayList<>();
    public boolean stopLag = false;
    public Entity robert;
    public Location robertLoc;
    public int playerCount = 0;
    public boolean titlechanged = false;

    private static Main instance;


    private Connection connection;
    public String host, database, username, password;
    public int port;

    public void mysqlSetup() {
        host = "localhost";
        port = 3306;
        database = "minecraft";
        username = "root";
        password = "";

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));

                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void onEnable() {

        new TimerTaskUpdate().runTaskTimer(this, 1L, 1200L);
        mysqlSetup();
//        for (Iterator<Recipe> it = this.getServer().recipeIterator(); it.hasNext(); ) {
//            Recipe recipe = it.next();
//            if (recipe != null) it.remove();
//        }
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        saveDefaultConfig();
        instance = this;
        registerEvents();
        registerCmd();
        restartServ();
        TabList.Tab();

        super.onEnable();
    }


    public static Main getInstance() {
        return instance;
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new net.DeadPvp.event.EventListeners(), this);
        pm.registerEvents(new net.DeadPvp.event.StaffModeEventListener(), this);
        //pm.registerEvents(new net.DeadPvp.kits.AssassinEvent(), this);
    }

    private void registerCmd() {
        getCommand("vanish").setExecutor(new Vanich());
        getCommand("freeze").setExecutor(new Freeze());
        getCommand("hub").setExecutor(new Hub(this));
        getCommand("spawn").setExecutor(new Spawn());
        getCommand("fly").setExecutor(new Fly());
        getCommand("heal").setExecutor(new Heal());
        getCommand("feed").setExecutor(new Feed());
        getCommand("sm").setExecutor(new StaffMode(this));
        getCommand("speed").setExecutor(new Speed());
        getCommand("dtp").setExecutor(new Tp());
        getCommand("world").setExecutor(new World());
        getCommand("spec").setExecutor(new Spec());
        getCommand("stoplag").setExecutor(new StopLag());
        getCommand("pvpsoup").setExecutor(new Pvpsoup());
        getCommand("creatif").setExecutor(new Creatif());
        getCommand("npc").setExecutor(new npc());
    }

    public void restartServ() {
        new BukkitRunnable() {
            @Override
            public void run() {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh");
                LocalDateTime now = LocalDateTime.now();
                if (dtf.format(now) == "5") {
                    Bukkit.broadcastMessage("Le Serveur va redémarrer dans 30 secondes !");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.broadcastMessage("Le Serveur va redémarrer dans 5 secondes !");
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Bukkit.getConsoleSender().sendMessage("Le serveur redemarre !");
                                    Bukkit.getServer().dispatchCommand(getServer().getConsoleSender(), "stop");
                                }
                            }.runTaskLater(Main.getInstance(), 20 * 5L);
                        }
                    }.runTaskLater(Main.getInstance(), 500L);
                }
            }
        }.runTaskTimer(this, 54000L, 54000L);
    }

    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (staffModePlayers.contains(player)) {
                AdminInv ai = AdminInv.getFromPlayer(player);
                ai.destroy();
                staffModePlayers.remove(player);
                ai.giveInv(player);
            }
            vanishedPlayers.remove(player);
            player.closeInventory();
        }
        super.onDisable();
    }


    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            playerCount = in.readInt();
            return;
        }


    }
}