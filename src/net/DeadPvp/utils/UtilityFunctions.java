package net.DeadPvp.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.DeadPvp.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

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

    public static void initLobby(Event e, Player player) {
        TextComponent msg = new TextComponent("§eSite web |");
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le site").create()));
        msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/"));
        TextComponent msg1 = new TextComponent(" §dBoutique §e|");
        msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir la boutique").create()));
        msg1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/shop"));

        TextComponent msg2 = new TextComponent(" §eForum |");
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Forum").create()));
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://deadpvp.fr/???"));
        TextComponent msg3 = new TextComponent(" §9Discord");
        msg3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Ouvrir le Discord").create()));
        msg3.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/ugfsjrmA5j"));

        player.sendMessage("§6\n"
                + "\n§6§l   Bienvenue sur §cDEAD§9PVP   \n§c "
                + "\n§b§l            LIEN");
        TextComponent espace = new TextComponent("   ");
        player.spigot().sendMessage(msg, msg1, msg2, msg3);
        player.sendMessage("\n");
        ItemBuilder compass = new ItemBuilder(Material.COMPASS).setName("§2§lSelection du mode de jeu");
        player.getInventory().setItem(4, compass.toItemStack());

        ItemBuilder hidePlayers = new ItemBuilder(Material.EYE_OF_ENDER).setName("§2§lCacher les joueurs");
        player.getInventory().setItem(7, hidePlayers.toItemStack());

        ItemBuilder trails = new ItemBuilder(Material.BLAZE_POWDER).setName("§2§lTrails");
        player.getInventory().setItem(1, trails.toItemStack());
    }

    public static ItemStack voidItem() {
        ItemBuilder glasspane = new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.WHITE).setName(" ")
                .setLore(" ");
        return UtilityFunctions.iSDeleteDatas(glasspane.toItemStack());
    }

    public static ItemStack iSDeleteDatas(ItemStack iS) {
        iS.getItemMeta().removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        iS.getItemMeta().removeItemFlags(ItemFlag.HIDE_DESTROYS);
        iS.getItemMeta().removeItemFlags(ItemFlag.HIDE_PLACED_ON);
        iS.getItemMeta().removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        iS.getItemMeta().removeItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        iS.getItemMeta().removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return iS;
    }

    public static void showHidePlayers(Player p) {
        if (p.getInventory().getItem(7) != null) {
            if (p.getInventory().getItem(7).getType().equals(Material.ENDER_PEARL)) {
                p.getInventory().getItem(7).setType(Material.EYE_OF_ENDER);
                ItemMeta iM = p.getInventory().getItem(7).getItemMeta();
                iM.setDisplayName("§2§lCacher les joueurs");
                p.getInventory().getItem(7).setItemMeta(iM);
            } else if (p.getInventory().getItem(7).getType().equals(Material.EYE_OF_ENDER)) {
                p.getInventory().getItem(7).setType(Material.ENDER_PEARL);
                ItemMeta iM = p.getInventory().getItem(7).getItemMeta();
                iM.setDisplayName("§2§lAfficher les joueurs");
                p.getInventory().getItem(7).setItemMeta(iM);
            }
        }
    }
}