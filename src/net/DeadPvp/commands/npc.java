package net.DeadPvp.commands;

import com.mojang.authlib.GameProfile;
import net.DeadPvp.Main;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.Iterator;
import java.util.UUID;

public class npc implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("deadpvp.npc") || player.hasPermission("deadpvp.npc")) {

                Location location = player.getLocation();
                MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
                WorldServer nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
                GameProfile gameProfile = new GameProfile(UUID.fromString("2b6b47d5-c226-47d2-9da8-49a54dfe062d"), "§a§l" +
                        "Vote");


                EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
                Player npcPlayer = npc.getBukkitEntity().getPlayer();
                npcPlayer.setPlayerListName("");

                npc.setLocation(location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(),
                        player.getLocation().getPitch());

                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
                connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,
                        npc));


            }


//                Entity e = p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
//                Villager v = (Villager) e;
//                v.setCustomNameVisible(true);
//                v.setCustomName("§6Vote");
//                v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 255, false, false));
//
//
//                Main.getInstance().robert = v;
//                Main.getInstance().robertLoc = v.getLocation();
            else sender.sendMessage("§cTu n'as pas la permission d'utiliser cette commande !");
        } else sender.sendMessage("Tu dois etre joueur");


        return false;
    }
}