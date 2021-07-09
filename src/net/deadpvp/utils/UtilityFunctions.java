package net.deadpvp.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.Main;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UtilityFunctions {
    
    public static void tpToServ(Player p, String s) {
        
        ByteArrayOutputStream bite = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bite);
        ByteArrayDataOutput doss = ByteStreams.newDataOutput();
        
        doss.writeUTF("Connect");
        doss.writeUTF(s); // Le nom du srv
        Player player = Bukkit.getPlayerExact(p.getName());
        player.sendPluginMessage(Main.getInstance(), "BungeeCord", doss.toByteArray());
    }
    
    public static void initLobby(Player player) {
        ItemBuilder compass = new ItemBuilder(Material.COMPASS).setName("§2§lSelection du mode de jeu");
        player.getInventory().setItem(4, compass.toItemStack());
        
        ItemBuilder hidePlayers = new ItemBuilder(Material.EYE_OF_ENDER).setName("§2§lCacher les joueurs");
        player.getInventory().setItem(7, hidePlayers.toItemStack());
        
        ItemBuilder trails = new ItemBuilder(Material.BLAZE_POWDER).setName("§2§lTrails");
        player.getInventory().setItem(1, trails.toItemStack());
    }
    
    public static ItemStack voidItem(DyeColor color) {
        ItemBuilder glasspane = new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(color).setName(" ")
                .setLore(" ");
        glasspane.setDyeColor(color);
        return UtilityFunctions.iSDeleteDatas(glasspane.toItemStack());
    }
    
    public static ItemStack iSDeleteDatas(ItemStack iS) {
        ItemMeta iM = iS.getItemMeta();
        iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iM.addItemFlags(ItemFlag.HIDE_DESTROYS);
        iM.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        iM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        iM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iS.setItemMeta(iM);
        return iS;
    }

    
    public static void showHidePlayers(Player p) {
        if (p.getInventory().getItem(7) != null) {
            if (p.getInventory().getItem(7).getType().equals(Material.ENDER_PEARL)) {
                p.getInventory().getItem(7).setType(Material.EYE_OF_ENDER);
                ItemMeta iM = p.getInventory().getItem(7).getItemMeta();
                iM.setDisplayName("§2§lCacher les joueurs");
                p.getInventory().getItem(7).setItemMeta(iM);
                Main.getInstance ().hidePlayerOn.remove (p);
                Bukkit.getOnlinePlayers().forEach(target -> p.showPlayer(target));
            } else if (p.getInventory().getItem(7).getType().equals(Material.EYE_OF_ENDER)) {
                p.getInventory().getItem(7).setType(Material.ENDER_PEARL);
                ItemMeta iM = p.getInventory().getItem(7).getItemMeta();
                iM.setDisplayName("§2§lAfficher les joueurs");
                p.getInventory().getItem(7).setItemMeta(iM);
                Main.getInstance ().hidePlayerOn.add (p);
                Bukkit.getOnlinePlayers().forEach(target -> p.hidePlayer(target));
            }
        }
    }
    
    public static int getmaxco(){
        String temp;
        int maxco = 0;
        try {
            Scanner sc = new Scanner(new File ("/home/ubuntu/server/lobby_serv/plugins/DEADPVP/maxco.txt"));
            String[] tab = sc.nextLine().split(",");
            temp = tab[0];
            maxco = Integer.parseInt(temp);
            sc.close();
            
        }catch (IOException e){
            Bukkit.getConsoleSender().sendMessage("§c§lWarning : le fichier maxco.txt n'éxiste plus ! Merci de le créer ici : /home/ubuntu/server/lobby_serv/plugins/DEADPVP/");
        }
        return maxco;
    }

    public static void setAI(LivingEntity entity, boolean hasAi) {
        EntityLiving handle = ((CraftLivingEntity) entity).getHandle();
        handle.getDataWatcher().watch(15, (byte) (hasAi ? 0 : 1));
    }

    public static ItemStack getItemStack(Material material, String customName, Boolean enchantement) {
        ItemStack it = new ItemStack(material, 1);
        ItemMeta itM = it.getItemMeta();
        if(customName != null) itM.setDisplayName(customName);
        if(enchantement == true) {
            itM.addEnchant(Enchantment.ARROW_FIRE, 1 ,false);
            itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        it.setItemMeta(itM);
        return it;
    }
}