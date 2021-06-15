package net.deadpvp.runnable;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.deadpvp.Main;
import net.deadpvp.events.EventListeners;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TImerTaskUpdate extends BukkitRunnable {
    @Override
    public void run() {
        
        if (Main.getInstance().playerCount >= UtilityFunctions.getmaxco()){
            try {
                
                String content = Main.getInstance().playerCount+"";
                File file = new  File("/home/ubuntu/server/lobby_serv/plugins/DEADPVP/maxco.txt");
                
                if (!file.exists()) {
                    file.createNewFile();
                }
                
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        if(Bukkit.getOnlinePlayers().size()!=0){
            ByteArrayDataOutput doss = ByteStreams.newDataOutput();
            doss.writeUTF("PlayerCount");
            doss.writeUTF("ALL"); // Le nom du srv
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            player.sendPluginMessage(Main.getInstance(), "BungeeCord", doss.toByteArray());
            for(Player player2 : Bukkit.getOnlinePlayers()){
                EventListeners.updateScoreBoard(player2);
            }
            
        }
        
        
        
    }
}