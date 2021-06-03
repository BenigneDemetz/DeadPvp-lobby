package net.DeadPvp.utils;

import net.DeadPvp.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MysqlUtility {

    private static Main plugin;

    public static boolean playerExists(String p) {
    try {

        PreparedStatement statement = plugin.getConnection()
                .prepareStatement("SELECT * FROM " + plugin.viptable + " WHERE PLAYER=?");
        statement.setString(1, p);

        ResultSet results = statement.executeQuery();
        if (results.next()) {
            plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Player Found");
            return true;
        }
        plugin.getServer().broadcastMessage(ChatColor.RED + "Player NOT Found");

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public static void createPlayer(final Player player) {
        try {
            String name = player.getName();
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.viptable + " WHERE PSEUDO=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();
            System.out.print(1);
            if (playerExists(name) != true) {
                PreparedStatement insert = plugin.getConnection()
                        .prepareStatement("INSERT INTO " + plugin.viptable + " (PSEUDO,DEBUT) VALUES (?,?)");
                insert.setString(1, name);
                insert.setDate(2, Date.valueOf(LocalDate.now()));
                insert.executeUpdate();

                plugin.getServer().broadcastMessage(ChatColor.GREEN + "Player Inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCoins(String name) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("UPDATE " + plugin.viptable + " SET DEBUT=? WHERE PLAYER=?");
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getCoins(String name) {
        try {
            PreparedStatement statement = plugin.getConnection()
                    .prepareStatement("SELECT * FROM " + plugin.viptable + " WHERE UUID=?");
            statement.setString(1, name);
            ResultSet results = statement.executeQuery();
            results.next();

            System.out.print(results.getInt("COINS"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
