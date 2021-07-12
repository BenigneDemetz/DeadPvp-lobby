package net.deadpvp.guiManager;

import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

public abstract class Gui implements InventoryHolder {

    protected Inventory inv;

    public Gui(PlayerGuiUtils playerGuiUtils) {
        this.playerGuiUtils = playerGuiUtils;
    }

    protected PlayerGuiUtils playerGuiUtils;

    protected abstract int getSlots();
    protected abstract String getName();
    public abstract DyeColor color();
    public abstract void EventHandler(InventoryClickEvent e) throws IOException;
    protected abstract void setItems();

    public void fillGlass(){

        for (int i = 0; i < inv.getSize (); ++i) {
            if (inv.getItem (i) == null) {
                inv.setItem (i, UtilityFunctions.voidItem (color()));
            }
        }
    }

    public void openInv(){
        inv = Bukkit.createInventory(this, getSlots(), getName());

        this.setItems();
        fillGlass();

        playerGuiUtils.getPlayer().openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
