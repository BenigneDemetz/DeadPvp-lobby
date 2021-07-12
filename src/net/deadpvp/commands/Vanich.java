package net.deadpvp.commands;

import net.deadpvp.Main;
import net.deadpvp.events.EventListeners;
import net.deadpvp.utils.AdminInv;
import net.deadpvp.utils.ItemBuilder;
import net.deadpvp.utils.UtilityFunctions;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class Vanich implements CommandExecutor {
    public static ArrayList<Player> inVanish = new ArrayList<Player>();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(sender instanceof Player) {
            Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
            Player player = ((Player) sender).getPlayer();
            if (inVanish.contains(player)) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.setAllowFlight(true);
                    player.setFlying(false);

                }
                for (Player p : onlinePlayers) {
                    p.showPlayer(player);
                }
                AdminInv ai = AdminInv.getFromPlayer(player);
                ai.destroy();
                Main.getInstance().staffModePlayers.remove(player);
                ai.giveInv(player);
                player.sendMessage("§bVous n'êtes plus en vanish !");
                player.setAllowFlight(false);
                player.setFlying(false);
                player.setPlayerListName(UtilityFunctions.getPrefix(player) +player.getName());
                inVanish.remove(player);
                return true;

            }

            if (!inVanish.contains(player)) {
                if (player.hasPermission("deadpvp.Vanish")) {
                    for (Player p : onlinePlayers) {
                        if (p.hasPermission("deadpvp.Vanish")) {
                            p.showPlayer(player);
                        } else {
                            p.hidePlayer(player);
                        }
                    }
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.sendMessage("§bVous êtes maintenant en vanish !");
                    player.setPlayerListName("§7§l[VANISHED] "+player.getName());
                    inVanish.add(player);
                    AdminInv ai = new AdminInv(player);
                    ai.init();
                    Main.getInstance().staffModePlayers.add(player);
                    ai.saveInv(player);

                    player.getInventory().setItem(8, new ItemBuilder(Material.COMMAND_MINECART).setName("§cCommande").toItemStack());
                    ItemBuilder kbTester = new ItemBuilder(Material.IRON_HOE).setName("§aKB Tester").addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                    player.getInventory().setItem(0, kbTester.toItemStack());
                    ItemBuilder InvSee = new ItemBuilder(Material.PAPER).setName("§aInvSee");
                    player.getInventory().setItem(1, InvSee.toItemStack());
                    ItemBuilder RandomTp = new ItemBuilder(Material.EGG).setName("§aTp à un joueur aléatoire");
                    player.getInventory().setItem(2, RandomTp.toItemStack());


                    ItemBuilder hammer = new ItemBuilder(Material.GOLD_AXE).setName("§4§lSanction hammer");
                    player.getInventory().setItem(4, hammer.toItemStack());

                    ItemBuilder freeze = new ItemBuilder(Material.ICE).setName("§aFreeze");
                    player.getInventory().setItem(6, freeze.toItemStack());
                    ItemBuilder command = new ItemBuilder(Material.COMMAND).setName("§aCommande");
                    player.getInventory().setItem(7, command.toItemStack());
                    ItemBuilder barrier = new ItemBuilder(Material.BARRIER).setName("§cEXIT");
                    player.getInventory().setItem(8, barrier.toItemStack());











                } else {
                    player.sendMessage("§cVous n'avez pas les permissions de faire cela !");
                }
            } else {
                for (Player p : onlinePlayers) {
                    p.showPlayer(player);
                }
            }
            return true;
        }
        return false;
    }


}