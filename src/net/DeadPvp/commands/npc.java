package net.DeadPvp.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.DeadPvp.Main;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
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


import java.net.URL;
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
                UUID Id = player.getUniqueId();
                GameProfile gameProfile = new GameProfile(Id, "§a§l" +"Vote");
                gameProfile.getProperties().removeAll("textures");

                String[] name = getSkin(player, player.getName());
                gameProfile.getProperties().put("textures",new Property("textures",name[0], name[1]));


                EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
                Player npcPlayer = npc.getBukkitEntity().getPlayer();
                npcPlayer.setPlayerListName("§7§lNPC");

                npc.setLocation(location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(),
                        player.getLocation().getPitch());

                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
                connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,npc));


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
        } else sender.sendMessage("Tu dois etre un joueur (connard)");


        return false;
    }

    private static String[] getSkin(Player player, String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();

            return new String[]{texture, signature};

        } catch (Exception e) {
            EntityPlayer p = ((CraftPlayer) player).getHandle();
            GameProfile profile = p.getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.getValue();
            String signature = property.getSignature();
            return new String[]{texture, signature};
        }

    }

}