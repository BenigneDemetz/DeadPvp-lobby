package net.DeadPvp.utils;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.Main;
import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
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

                try {

                    if (Bukkit.getOnlinePlayers().size() == 0) return;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                    Field a = packet.getClass().getDeclaredField("a");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("b");
                    b.setAccessible(true);

                    try {
                        Object craftPlayer = player;
                        ping = (int) craftPlayer.getClass().getField("ping").get(craftPlayer);
                    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {

                    }

                    Object header1 = new ChatComponentText("§bDeadPvp §7- §dLobby\n" +
                            "ping : " + ping);
                    Object header2 = new ChatComponentText("§aDeadPvp §7- §dLobby\n" +
                            "ping : " + ping);


                    Object footer = new ChatComponentText("§bJoueurs connectés: §e§l" + Main.getInstance().playerCount);
                    if (Main.getInstance().titlechanged) {
                        a.set(packet, header2);
                        Main.getInstance().titlechanged = false;

                    } else {
                        a.set(packet, header1);
                        Main.getInstance().titlechanged = true;
                    }
                    b.set(packet, footer);

                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
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
                Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                player.sendPluginMessage(Main.getInstance(), "BungeeCord", doss.toByteArray());
            }
        }.runTaskTimer(Main.getInstance(), 0, 100);

    }


}