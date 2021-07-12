package net.deadpvp.guiManager.guis;

import net.deadpvp.Main;
import net.deadpvp.guiManager.Gui;
import net.deadpvp.guiManager.PlayerGuiUtils;
import net.deadpvp.guiManager.adminGuis.CreaAdminGui;
import net.deadpvp.guiManager.adminGuis.PvpSoupAdminGui;
import net.deadpvp.guiManager.adminGuis.ServeurTest1AdminGui;
import net.deadpvp.guiManager.adminGuis.ServeurTest2AdminGui;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import javax.swing.*;


public class CompassGui extends Gui {
    public CompassGui(PlayerGuiUtils playerGuiUtils) {
        super(playerGuiUtils);
    }

    @Override
    protected int getSlots() {
        return 36;
    }

    @Override
    protected String getName() {
        return "§2§lSelection du mode de jeu";
    }

    @Override
    public DyeColor color() {
        return DyeColor.BROWN;
    }

    @Override
    public void EventHandler(InventoryClickEvent e) {
        switch (e.getCurrentItem().getType()){
            case GRASS:

                if(Main.serveurEnMaintenance.contains("crea") && !e.getWhoClicked().hasPermission("chat.admin")){
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§4Serveur en Maintenance.");
                    return;
                }

                if(e.getClick().equals(ClickType.RIGHT) && playerGuiUtils.getPlayer().hasPermission("chat.admin")){
                    CreaAdminGui creaGui = new CreaAdminGui(Main.getPlayerGuiUtils((Player) e.getWhoClicked()));
                    creaGui.openInv();
                } else {
                    UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "crea");
                }
                break;

            case DIAMOND_SWORD:
                if(Main.serveurEnMaintenance.contains("pvpsoup") && !e.getWhoClicked().hasPermission("chat.admin")){
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§4Serveur en Maintenance.");
                    return;
                }

               if(e.getClick().equals(ClickType.RIGHT) && playerGuiUtils.getPlayer().hasPermission("chat.admin")){
                    PvpSoupAdminGui pvpSoupAdminGui = new PvpSoupAdminGui(Main.getPlayerGuiUtils((Player)e.getWhoClicked()));
                    pvpSoupAdminGui.openInv();
                } else {
                   UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "crea");
               }
                break;

            case REDSTONE_COMPARATOR:

                if(e.getClick().equals(ClickType.LEFT)){
                    UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "caca");
                } else if(e.getClick().equals(ClickType.RIGHT) && playerGuiUtils.getPlayer().hasPermission("chat.admin") || playerGuiUtils.getPlayer().hasPermission("chat.dev")){
                    ServeurTest1AdminGui serveurTest1AdminGui = new ServeurTest1AdminGui(Main.getPlayerGuiUtils((Player)e.getWhoClicked()));
                    serveurTest1AdminGui.openInv();
                }

                    break;
            case DIODE:
                if(e.getClick().equals(ClickType.LEFT) && playerGuiUtils.getPlayer().hasPermission("chat.admin") || playerGuiUtils.getPlayer().hasPermission("chat.dev")){
                    UtilityFunctions.tpToServ(playerGuiUtils.getPlayer(), "prout");
                } else if(e.getClick().equals(ClickType.RIGHT)){
                    ServeurTest2AdminGui serveurTest2AdminGui = new ServeurTest2AdminGui(Main.getPlayerGuiUtils((Player)e.getWhoClicked()));
                    serveurTest2AdminGui.openInv();
                }
                break;

            case PAPER:
                e.getWhoClicked().sendMessage("§2§ldeadpvp.fr");
                e.getWhoClicked().closeInventory();
                break;
            default:
                break;
        }
    }

    @Override
    protected void setItems() {

        //ItemBuilder grass = new ItemBuilder (Material.GRASS).setLore (lorecrea).setName ("§d§lCREATIF §c§l[EN MAINTENANCE]");
        ItemBuilder grass = new ItemBuilder (Material.GRASS)
                .setLore ("§bDescription :", "  §7Construisez seul ou entre amis", "  §7le plot de vos rêves !", "§f ", "§6>>> "+ Main.getInstance().creatifcount+ "joueurs en créatif !")
                .setName ("§d§lCREATIF").hideAttributes();

        ItemBuilder sword = new ItemBuilder (Material.DIAMOND_SWORD).setName ("§c§lPVP§9§lSOUP").addEnchant (Enchantment.ARROW_FIRE, 1)
                .setLore ("§bDescription :", "  §7Un mode de jeu classique de DEADPVP ! ", "  §7Combattez vos ennemis dans une map où les soupes",
                        "  §7peuvent vous sauver la vie !","§f ", "§6>>> "+Main.getInstance().pvpsoupcount+"joueurs en pvpsoup !").hideAttributes();

        ItemBuilder barrier = new ItemBuilder (Material.BARRIER).setName ("§4Maintenance").setLore (" ");
        ItemBuilder paper = new ItemBuilder (Material.PAPER).setName ("§d§lSite de DeadPVP").setLore (" ").hideAttributes();

        if(playerGuiUtils.getPlayer().hasPermission("chat.admin") || playerGuiUtils.getPlayer().hasPermission("chat.dev")){

            sword.addLoreLine("§4Clique droit > Admin Menu");
            grass.addLoreLine("§4Clique droit > Admin Menu");

            ItemBuilder comparator = new ItemBuilder (Material.REDSTONE_COMPARATOR).setName ("§c§lServeur Test 1").setLore ("§7Serveur de test numéro 1", "§4Clique Droit > Admin menu").hideAttributes();
            ItemBuilder diode = new ItemBuilder (Material.DIODE).setName ("§c§lServeur Test 2").setLore ("§7Serveur de test numéro 2", "§4Clique droit > Admin Menu").hideAttributes();

            inv.setItem (34, comparator.toItemStack ());
            inv.setItem (35, diode.toItemStack ());
        }

        if(Main.serveurEnMaintenance.contains("crea")){
            grass.setName("§d§lCREATIF §c§l[EN MAINTENANCE]");
        }
        if(Main.serveurEnMaintenance.contains("pvpsoup")){
            sword.setName("§c§lPVP§9§lSOUP §c§l[EN MAINTENANCE]");
        }

        inv.setItem (11, grass.toItemStack ());
        inv.setItem (13, sword.toItemStack ());
        inv.setItem (15, barrier.toItemStack ());
        inv.setItem (31, paper.toItemStack ());
    }


}
