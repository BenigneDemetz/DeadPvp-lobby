package net.deadpvp.events;

import net.deadpvp.guiManager.Gui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException {
        InventoryHolder holder = e.getClickedInventory().getHolder();

        if(holder instanceof Gui){
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            Gui gui = (Gui)holder;
            gui.EventHandler(e);
        }
    }
}
