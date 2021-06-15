package net.deadpvp;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.commands.Creatif;
import net.deadpvp.commands.Pvpsoup;
import net.deadpvp.events.EventListeners;
import net.deadpvp.runnable.TImerTaskUpdate;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;


import java.io.*;
import java.util.ArrayList;


public class Main extends JavaPlugin implements PluginMessageListener {
    
    public boolean stopLag = false;
    public int playerCount;
    public ArrayList<Player> hidePlayerOn = new ArrayList<> ();
    
    private static Main instance;
    
    public static Main getInstance() {
        return instance;
    }
    
    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = Bukkit.getServer ().getPluginManager ();
        pm.registerEvents (new EventListeners (), this);
        getCommand ("creatif").setExecutor (new Creatif ());
        getCommand ("pvpsoup").setExecutor (new Pvpsoup ());
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "Return", this);
        new TImerTaskUpdate ().runTaskTimer(this, 1L, 20L);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord",this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        saveDefaultConfig();
        super.onEnable ();
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
            playerCount = ine.readInt ();
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
            return;
        }
    }
}

