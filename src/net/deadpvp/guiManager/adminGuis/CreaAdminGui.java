package net.deadpvp.guiManager.adminGuis;

import net.deadpvp.Main;
import net.deadpvp.guiManager.Gui;
import net.deadpvp.guiManager.PlayerGuiUtils;
import org.bukkit.DyeColor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;

public class CreaAdminGui extends Gui {
    public CreaAdminGui(PlayerGuiUtils playerGuiUtils) {
        super(playerGuiUtils);
    }

    @Override
    protected int getSlots() {
        return 9;
    }

    @Override
    protected String getName() {
        return "§cMenu Admin > §lCréatif";
    }

    @Override
    public DyeColor color() {
        return DyeColor.WHITE;
    }

    @Override
    public void EventHandler(InventoryClickEvent e) throws IOException {
        AdminGuiUtils.adminGuiEvent(e, "crea" , "testcrea");
    }

    @Override
    protected void setItems() {
        AdminGuiUtils.adminGuiItems(inv, "crea");
    }
}
