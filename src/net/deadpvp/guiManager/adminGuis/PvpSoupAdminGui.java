package net.deadpvp.guiManager.adminGuis;

import net.deadpvp.guiManager.Gui;
import net.deadpvp.guiManager.PlayerGuiUtils;
import org.bukkit.DyeColor;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;

public class PvpSoupAdminGui extends Gui {

    public PvpSoupAdminGui(PlayerGuiUtils playerGuiUtils) {
        super(playerGuiUtils);
    }

    @Override
    protected int getSlots() {
        return 9;
    }

    @Override
    protected String getName() {
        return "§cMenu Admin > §c§lPvPSoup";
    }

    @Override
    public DyeColor color() {
        return DyeColor.WHITE;
    }

    @Override
    public void EventHandler(InventoryClickEvent e) throws IOException {
        AdminGuiUtils.adminGuiEvent(e,"pvpsoup", "pvpsoup_server");
    }

    @Override
    protected void setItems() {
        AdminGuiUtils.adminGuiItems(inv, "pvpsoup");
    }
}
