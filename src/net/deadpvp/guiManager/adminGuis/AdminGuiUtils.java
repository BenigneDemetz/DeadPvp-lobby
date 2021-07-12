package net.deadpvp.guiManager.adminGuis;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.deadpvp.Main;
import net.deadpvp.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;


import java.io.*;
import java.net.Socket;

public class AdminGuiUtils {

    public static void adminGuiEvent(InventoryClickEvent e, String serverName, String pathToFolder) {
        Player p =(Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()){
            case BARRIER:

                try {
                    Runtime.getRuntime().exec("sh ../" + pathToFolder + "/stop.sh");
                } catch (IOException ioException) {
                    System.out.println("a");
                }

            case EMERALD:
                e.getWhoClicked().closeInventory();

                break;
            case REDSTONE_TORCH_ON:
                if(!Main.serveurEnMaintenance.contains(serverName)){
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§aLe serveur §f§l" + serverName + " §a est en maintenance.");
                    Main.serveurEnMaintenance.add(serverName);
                } else {
                    e.getWhoClicked().closeInventory();
                    e.getWhoClicked().sendMessage("§cLe serveur §f§l" + serverName + " §c n'est plus maintenance.");
                    Main.serveurEnMaintenance.remove(serverName);
                }
        }
    }

    public static void adminGuiItems(Inventory inv, String serverName) {

        ItemBuilder redstone_torch = new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("&cMaintenance").hideAttributes();
        ItemBuilder barrier = new ItemBuilder(Material.BARRIER).setName("§4Restart le serveur");

        inv.setItem(6, barrier.toItemStack());

        if(Main.serveurEnMaintenance.contains(serverName)){
            redstone_torch.setLore("§lMaintenance : §a§lON");
        } else {
            redstone_torch.setLore("§lMaintenance : §4§lOFF");
        }
        inv.setItem(2, redstone_torch.toItemStack());
    }

}
