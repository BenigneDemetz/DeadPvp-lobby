package net.deadpvp.events;

import net.deadpvp.guiManager.Gui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        InventoryHolder holder = e.getClickedInventory().getHolder();

        if(holder instanceof Gui gui){
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            gui.EventHandler(e);
        }
    }
}
