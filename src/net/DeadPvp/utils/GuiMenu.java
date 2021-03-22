package net.DeadPvp.utils;

import net.DeadPvp.Main;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Skull;

import java.util.ArrayList;

public class GuiMenu {

    public static Inventory headMenu (){
        Inventory inventory = Bukkit.createInventory(null, 6*9, "§bJoueurs");

        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (int i = 0; i < players.size() && i < 5*9; i++){
            ItemStack head = new ItemStack(Material.SKULL_ITEM);
            SkullMeta skull = (SkullMeta) head.getItemMeta();
            skull.setOwner(players.get(i).getName());
            head.setItemMeta(skull);

            ItemBuilder heads = new ItemBuilder(head).setName(players.get(i).getName()).
                    setLore("§dClique pour accèder à la liste des commandes du joueur.");
            heads.setName("§b" + players.get(i).getName());
            inventory.setItem(i,heads.toItemStack());
        }
        ItemBuilder clearLag = new ItemBuilder(Material.TNT).setName("§dClearLag");
        inventory.setItem(5*9+3, clearLag.toItemStack());

        ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dSpec mode");
        inventory.setItem(5*9+5, spec.toItemStack());

        if (players.size() > 5*9-1) {
            ItemBuilder nextPage = new ItemBuilder(Material.ARROW).setName("§dPage suivante");
            inventory.setItem(6 * 9 - 1, nextPage.toItemStack());
        }

        return inventory;
    }

    public static Inventory headMenuPage2 (){

        Inventory inventory = Bukkit.createInventory(null, 6*9, "§bJoueurs");

        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (int i = 6*9; i < players.size() && i < 11*9; i++){
            ItemStack head = new ItemStack(Material.SKULL_ITEM);
            SkullMeta skull = (SkullMeta) head.getItemMeta();
            skull.setOwner(players.get(i).getName());
            head.setItemMeta(skull);

            ItemBuilder heads = new ItemBuilder(head).setName(players.get(i).getName()).
                    setLore("§dClique pour accèder à la liste des commandes du joueur.");
            heads.setName("§b" + players.get(i).getName());
            inventory.setItem(i,heads.toItemStack());
        }
        ItemBuilder clearLag = new ItemBuilder(Material.TNT).setName("§dClearLag");
        inventory.setItem(5*9+3, clearLag.toItemStack());

        ItemBuilder spec = new ItemBuilder(Material.GLASS).setName("§dSpec mode");
        inventory.setItem(5*9+5, spec.toItemStack());

        ItemBuilder lastPage = new ItemBuilder(Material.ARROW).setName("§dPage précédente");
        inventory.setItem(6*9-1, lastPage.toItemStack());

        if (players.size() > 11*9-1) {
            ItemBuilder nextPage = new ItemBuilder(Material.ARROW).setName("§dPage suivante");
            inventory.setItem(6 * 9 - 1, nextPage.toItemStack());
        }

        return inventory;
    }

    public static Inventory playerMenu (Player p) {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "§b" + p.getName());

        ItemBuilder InvSee = new ItemBuilder(Material.EYE_OF_ENDER).setName("§aVoir l'Inventaire de " + p.getName());
        inventory.setItem(0, InvSee.toItemStack());

        ItemBuilder Tp = new ItemBuilder(Material.ENDER_PEARL).setName("§aSe Téléporter à " + p.getName());
        inventory.setItem(1, Tp.toItemStack());

        ItemBuilder freeze = new ItemBuilder(Material.ICE).setName("§aFreeze " + p.getName());
        if (Main.getInstance().freezedPlayers.contains(p)) {
            freeze.setName("§aUnfreeze " + p.getName());
        }
        inventory.setItem(4, freeze.toItemStack());
        ItemBuilder retour = new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.ORANGE).setName("§dRetour Joueurs");
        inventory.setItem(18, retour.toItemStack());

        ItemBuilder mute = new ItemBuilder(Material.INK_SACK).setName("§aMute " + p.getName());
        ItemBuilder kick = new ItemBuilder(Material.PISTON_BASE).setName("§aKick " + p.getName());
        ItemBuilder unmute = new ItemBuilder(Material.PAPER).setName("§aUnmute " + p.getName());
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("LiteBans")) {
            mute.setName("§aMute " + p.getName());
            kick.setName("§aKick " + p.getName());
            unmute.setName("§aUnmute " + p.getName());
        } else {
            mute.setName("§cLiteBans n'est pas actif");
            kick.setName("§cLiteBans n'est pas actif");
            unmute.setName("§cLiteBans n'est pas actif");
        }
        inventory.setItem(2, mute.toItemStack());
        inventory.setItem(3, kick.toItemStack());
        inventory.setItem(11, unmute.toItemStack());

        return inventory;
    }

    public static Inventory Mute (Player target){
            Inventory MuteInventory = Bukkit.createInventory(null, 3 * 9, "§e" + target.getName());

            ItemBuilder Insulte = new ItemBuilder(Material.FIREBALL).setName("§dInsulte");
            ItemBuilder Provoc = new ItemBuilder(Material.IRON_SWORD).setName("§dProvocation");
            ItemBuilder Spam = new ItemBuilder(Material.PAPER).setName("§dSpam");
            ItemBuilder Pub = new ItemBuilder(Material.BOOKSHELF).setName("§dPub");
            ItemBuilder retour = new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.ORANGE).setName("§dRetour Action");
            ItemBuilder unmute = new ItemBuilder(Material.PAPER).addUnsafeEnchantment(Enchantment.DIG_SPEED, 1).setName("Unmute");
            MuteInventory.setItem(0, Spam.toItemStack());
            MuteInventory.setItem(1, Insulte.toItemStack());
            MuteInventory.setItem(2, Provoc.toItemStack());
            MuteInventory.setItem(3, Pub.toItemStack());
            MuteInventory.setItem(18, retour.toItemStack());
            return MuteInventory;

    }

    public static Inventory MuteRaison (Player target, Player p, String Raison){
        Inventory MuteInventoryRaison = Bukkit.createInventory(null, 3*9, "§e" + target.getName() + " : " + Raison);

        ItemBuilder min15 = new ItemBuilder(Material.PAPER).setName("§d15 minutes§a§b§d");
        ItemBuilder min30 = new ItemBuilder(Material.PAPER).setName("§d30 minutes§a§b§d");
        ItemBuilder heure1 = new ItemBuilder(Material.PAPER).setName("§d01 heure§a§b§d");
        ItemBuilder heure2 = new ItemBuilder(Material.PAPER).setName("§d02 heures§a§b§d");
        ItemBuilder heure12 = new ItemBuilder(Material.PAPER).setName("§d12 heures§a§b§d");
        ItemBuilder jour1 = new ItemBuilder(Material.PAPER).setName("§d01 jour§a§b§d");
        ItemBuilder jour15 = new ItemBuilder(Material.PAPER).setName("§d06 heures§a§b§d");
        ItemBuilder jours2 = new ItemBuilder(Material.PAPER).setName("§d02 jours§a§b§d");
        ItemBuilder semaine = new ItemBuilder(Material.PAPER).setName("§d01 semaine§a§b§d");
        ItemBuilder retour = new ItemBuilder(Material.STAINED_GLASS_PANE).setDyeColor(DyeColor.ORANGE).setName("§dRetour Raisons");

        MuteInventoryRaison.setItem(0, min15.toItemStack());
        MuteInventoryRaison.setItem(1, min30.toItemStack());
        MuteInventoryRaison.setItem(2, heure1.toItemStack());
        MuteInventoryRaison.setItem(3, heure2.toItemStack());
        MuteInventoryRaison.setItem(5, heure12.toItemStack());
        MuteInventoryRaison.setItem(6, jour1.toItemStack());
        MuteInventoryRaison.setItem(4, jour15.toItemStack());
        MuteInventoryRaison.setItem(7, jours2.toItemStack());
        MuteInventoryRaison.setItem(8, semaine.toItemStack());
        MuteInventoryRaison.setItem(18, retour.toItemStack());

        return MuteInventoryRaison;

    }

}
