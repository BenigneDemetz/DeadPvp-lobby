package net.deadpvp.commands;

import net.deadpvp.utils.UtilityFunctions;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

public class FixVote implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender Sender, Command cmd, String s, String[] args) {
        if(Sender.hasPermission("dev.chat")){
            Location loca = new Location(Bukkit.getWorld("Lobby"),7.5,49,7.5,135,0);
            Villager villager = (Villager) Bukkit.getWorld("Lobby").spawnEntity(loca, EntityType.VILLAGER);
            villager.setCustomNameVisible(true);
            villager.setCustomName("§2§lVOTE");
            Entity znms = ((CraftEntity) villager).getHandle();
            NBTTagCompound ztag = new NBTTagCompound();
            znms.c(ztag);
            ztag.setBoolean("Silent", true);
            ztag.setString("CustomName", "§b§lNEW §2§lVote");
            znms.f(ztag);
            villager.setCustomNameVisible(true);
            villager.setCustomName("§b§lNEW §2§lVote");
            villager.setProfession(Villager.Profession.FARMER);
            UtilityFunctions.setAI(villager,false);
            villager.teleport(loca);
            villager.setTarget(villager);
            villager.setVelocity(new Vector(0,5,0));
            villager.teleport(loca);


        }
        return false;
    }
}
