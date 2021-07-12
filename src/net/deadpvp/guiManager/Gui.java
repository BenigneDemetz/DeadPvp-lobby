package net.deadpvp.guiManager;

import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Gui implements InventoryHolder {

    protected Inventory inv;
    protected PlayerGuiUtils playerGuiUtils;

    protected abstract int getSlots();
    protected abstract String getName();
    public abstract void EventHandler(InventoryClickEvent e);
    protected abstract void setItems();

    public void fillGlass(){
        for (int i = 0; i < inv.getSize (); ++i) {
            if (inv.getItem (i) == null) {
                inv.setItem (i, UtilityFunctions.voidItem (DyeColor.BROWN));
            }
        }
    }

    public void openInv(Player p){
        inv = Bukkit.createInventory(this, getSlots(), getName());

        this.setItems();
        fillGlass();

        p.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
