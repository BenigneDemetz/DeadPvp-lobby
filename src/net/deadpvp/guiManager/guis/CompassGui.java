package net.deadpvp.guiManager.guis;

import net.deadpvp.Main;
import net.deadpvp.guiManager.Gui;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.swing.*;


public class CompassGui extends Gui {
    @Override
    protected int getSlots() {
        return 36;
    }

    @Override
    protected String getName() {
        return "§2§lSelection du mode de jeu";
    }

    @Override
    public void EventHandler(InventoryClickEvent e) {
        switch (e.getCurrentItem().getType()){
            case GRASS:
                if(e.getClick().equals(ClickType.LEFT)){
                    UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "creatif");
                }
                break;
            case DIAMOND_SWORD:
                UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "pvpsoup");
                break;
            case REDSTONE_COMPARATOR:
                    UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "caca");
                    break;
            case DIODE:
                    UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(),"prout");
                    break;
            case PAPER:
                    e.getWhoClicked().sendMessage("§2§ldeadpvp.fr");
                    e.getWhoClicked().closeInventory();
        }
    }

    @Override
    protected void setItems() {

        //ItemBuilder grass = new ItemBuilder (Material.GRASS).setLore (lorecrea).setName ("§d§lCREATIF §c§l[EN MAINTENANCE]");
        ItemBuilder grass = new ItemBuilder (Material.GRASS)
                .setLore ("§bDescription :", "  §7Construisez seul ou entre amis", "  §7le plot de vos rêves !", "§f ", "§6>>> "+ Main.getInstance().creatifcount+ "joueurs en créatif !")
                .setName ("§d§lCREATIF §c§l[EN MAINTENANCE]").hideAttributes();

        ItemBuilder sword = new ItemBuilder (Material.DIAMOND_SWORD).setName ("§c§lPVP§9§lSOUP").addEnchant (Enchantment.ARROW_FIRE, 1)
                .setLore ("§bDescription :", "  §7Un mode de jeu classique de DEADPVP ! ", "  §7Combattez vos ennemis dans une map où les soupes",
                        "  §7peuvent vous sauver la vie !","§f ", "§6>>> "+Main.getInstance().pvpsoupcount+"joueurs en pvpsoup !").hideAttributes();

        ItemBuilder barrier = new ItemBuilder (Material.BARRIER).setName ("§4Maintenance").setLore (" ");
        ItemBuilder paper = new ItemBuilder (Material.PAPER).setName ("§d§lSite de DeadPVP").setLore (" ").hideAttributes();

        if(playerGuiUtils.getPlayer().hasPermission("chat.admin") || playerGuiUtils.getPlayer().hasPermission("chat.dev")){

            sword.addLoreLine("&4Clique droit > Admin Menu");
            grass.addLoreLine("&4Clique droit > Admin Menu");

            ItemBuilder comparator = new ItemBuilder (Material.REDSTONE_COMPARATOR).setName ("§c§lServeur Test 1").setLore ("§7Serveur de test numéro 1", "§4Clique Droit > Admin menu").hideAttributes();
            ItemBuilder diode = new ItemBuilder (Material.DIODE).setName ("§c§lServeur Test 2").setLore ("§7Serveur de test numéro 2", "§4Clique droit > Admin Menu").hideAttributes();

            inv.setItem (34, comparator.toItemStack ());
            inv.setItem (35, diode.toItemStack ());
        }

        inv.setItem (11, grass.toItemStack ());
        inv.setItem (13, sword.toItemStack ());
        inv.setItem (15, barrier.toItemStack ());
        inv.setItem (31, paper.toItemStack ());
    }


}
