package net.deadpvp.guiManager.guis;

import net.deadpvp.guiManager.Gui;
import net.deadpvp.guiManager.PlayerGuiUtils;
import net.deadpvp.utils.ItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.awt.*;

public class PnjGui extends Gui {
    public PnjGui(PlayerGuiUtils playerGuiUtils) {
        super(playerGuiUtils);
    }

    @Override
    protected int getSlots() {
        return 27;
    }

    @Override
    protected String getName() {
        return "§2§lVOTE";
    }

    @Override
    public DyeColor color() {
        return DyeColor.GREEN;
    }

    @Override
    public void EventHandler(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        switch (e.getCurrentItem().getType()){
            case NETHER_STAR:
                p.closeInventory();
                net.md_5.bungee.api.chat.TextComponent msg = new TextComponent("§f----------------------------\n§b§l       [Site de vote]\n§f----------------------------");
                msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique pour ouvrir le site de vote !").create()));
                msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://deadpvp.com/vote"));
                p.spigot().sendMessage(msg);
                break;
        }
    }

    @Override
    protected void setItems() {
        ItemBuilder netherstar = new ItemBuilder (Material.NETHER_STAR)
                .setLore ("  §bVoter pour mettre en ", "  §bavant §4§lDEAD§1§lPVP§b et gagner des", "  §brécompenses !").setName ("§6§lSITE DE VOTE").hideAttributes();

        ItemBuilder book = new ItemBuilder (Material.BOOK)
                .setLore ("§bEn votant vous pouvez gagner :", "  §b1 à 5 Karma", "  §b25 à 35 Mystiques","  §bpar vote !").setName ("§c§lRécompenses").hideAttributes();

        inv.setItem (12, book.toItemStack ());
        inv.setItem (14, netherstar.toItemStack ());
    }
}
