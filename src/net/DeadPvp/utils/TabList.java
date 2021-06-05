package net.DeadPvp.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.Main;
import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class TabList {

    public static void Tab(){

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        new BukkitRunnable() {
            @Override
            public void run() {
                int ping = 0;
                String ping2;

                try {

                    if (Bukkit.getOnlinePlayers().size() != 0){
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            Field a = packet.getClass().getDeclaredField("a");
                            a.setAccessible(true);
                            Field b = packet.getClass().getDeclaredField("b");
                            b.setAccessible(true);

                            try {
                                Object craftPlayer = player;
                                ping = (int) craftPlayer.getClass().getField("ping").get(craftPlayer);
                            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                                System.out.println("error ping");
                            }
                            if (ping == 0){
                                ping2 = "§c§lunknown";
                            }else{
                                ping2 = ping+"";
                            }
                            Object header1 = new ChatComponentText(
                                    "§4§lDead§1§lPvp §r§7- §d§lLobby\n" +
                                            "§r§7Ping : §e§l" + ping2 + "\n");

                            Object footer = new ChatComponentText(""+ChatColor.YELLOW + ChatColor.BOLD+"\n"+
                                    "§bJoueurs connectés: "+ Main.getInstance().playerCount +
                                    "\n§6§lmc.deadpvp.fr\n" );

                            a.set(packet, header1);

                            b.set(packet, footer);

                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                        }
                    }


                } catch (NoSuchFieldException | IllegalAccessException e) {
                    System.out.println("ERREUR TAB");
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(Main.getInstance(),0,20);
        new BukkitRunnable() {
            @Override
            public void run() {

                ByteArrayDataOutput doss = ByteStreams.newDataOutput();
                doss.writeUTF("PlayerCount");
                doss.writeUTF("ALL"); // Le nom du srv
                if(Bukkit.getOnlinePlayers().size() ==0){
                    cancel();
                    return;
                }
                Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                player.sendPluginMessage(Main.getInstance(), "BungeeCord", doss.toByteArray());
            }
        }.runTaskTimer(Main.getInstance(), 0, 100);

    }


}