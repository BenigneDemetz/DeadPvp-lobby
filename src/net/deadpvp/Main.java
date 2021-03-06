package net.deadpvp;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.commands.Creatif;
import net.deadpvp.commands.FixVote;
import net.deadpvp.commands.Pvpsoup;
import net.deadpvp.events.ChatListeners;
import net.deadpvp.events.EventListeners;
import net.deadpvp.events.InventoryListeners;
import net.deadpvp.events.PlayerListeners;
import net.deadpvp.guiManager.PlayerGuiUtils;
import net.deadpvp.runnable.TImerTaskUpdate;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main extends JavaPlugin implements PluginMessageListener {
    
    public boolean stopLag = false;
    public int playerCount;
    public int creatifcount=0;
    public int pvpsoupcount=0;

    private Connection connection;
    public String host, database, username, password;
    public int port;

    public ArrayList<Player> staffModePlayers = new ArrayList<Player>();
    public ArrayList<Player> hidePlayerOn = new ArrayList<> ();
    public HashMap<Player, AdminInv> adminPlayerHashmap = new HashMap<>();
    private static final HashMap<Player, PlayerGuiUtils> playerGuiUtilsMap = new HashMap();
    public static final ArrayList<String> serveurEnMaintenance = new ArrayList<>();
    private static Main instance;
    
    public static Main getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {


        mysqlSetup();
        instance = this;
        PluginManager pm = Bukkit.getServer().getPluginManager ();
        pm.registerEvents (new EventListeners(), this);
        pm.registerEvents(new ChatListeners(), this);
        pm.registerEvents(new InventoryListeners(), this);
        pm.registerEvents(new PlayerListeners(), this);
        getCommand ("creatif").setExecutor(new Creatif ());
        getCommand ("pvpsoup").setExecutor(new Pvpsoup ());
        getCommand("fixvote").setExecutor(new FixVote());
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "Return", this);
        new TImerTaskUpdate ().runTaskTimer(this, 1L, 20L);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord",this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        super.onEnable ();
    }

    public static PlayerGuiUtils getPlayerGuiUtils(Player p){
        PlayerGuiUtils playerGuiUtils;
        if(!playerGuiUtilsMap.containsKey(p)){
            playerGuiUtils = new PlayerGuiUtils(p);
            playerGuiUtilsMap.put(p, playerGuiUtils);
            return playerGuiUtils;
        } else {
            return playerGuiUtilsMap.get(p);
        }
    }

    public void mysqlSetup() {
        host = "localhost";
        port = 3306;
        database = "minecraftrebased";
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
        } catch (SQLException throwables) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL ERROR");
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL ERROR");
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        DataInputStream in = new DataInputStream (new ByteArrayInputStream (bytes));
    
        try {
            String sub = in.readUTF (); // Sub-Channel
            if (sub.equals ("command")) { // As in bungee part we gave the sub-channel name "command", here we're checking it sub-channel really is "command", if it is we do the rest of code.
                String cmd = in.readUTF (); // Command we gave in Bungee part.
                System.out.println ("\n[DPBOUTIQUE] Achat d'un grade !\n");
                getServer ().dispatchCommand (getServer ().getConsoleSender (), cmd); // Executing the command!!
    
            }
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
        if (!s.equals ("BungeeCord")) {
            return;
        }
        ByteArrayDataInput ine = ByteStreams.newDataInput (bytes);
        String subchannel = ine.readUTF ();
        if (subchannel.equals ("PlayerCount")) {
            String server = ine.readUTF ();

            if(server.equalsIgnoreCase("pvpsoup")){
                pvpsoupcount = ine.readInt();
                return;
            }
            if(server.equalsIgnoreCase("crea")){
                creatifcount = ine.readInt();
                return;
            }

            if(server.equalsIgnoreCase("all")){
                playerCount = ine.readInt();
                if (playerCount > UtilityFunctions.getmaxco ()) {
                    try {

                        String content = playerCount + "";

                        File file = new File ("/home/ubuntu/server/lobby_serv/plugins/DEADPVP/maxco.txt");

                        if (!file.exists ()) {
                            file.createNewFile ();
                        }

                        FileWriter fw = new FileWriter (file.getAbsoluteFile ());
                        BufferedWriter bw = new BufferedWriter (fw);
                        bw.write (content);
                        bw.close ();


                    } catch (IOException e) {
                        e.printStackTrace ();
                    }

                }
            }


        }
    }
}

