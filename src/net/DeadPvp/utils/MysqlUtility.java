package net.DeadPvp.utils;

import com.mysql.jdbc.DatabaseMetaData;
import net.DeadPvp.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.*;

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
            Connection con = null;
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                con = DriverManager.getConnection("jdbc:odbc:HY_FLAT");

                DatabaseMetaData meta = (DatabaseMetaData) con.getMetaData();
                ResultSet res = meta.getTables(null, null, null,
                        new String[] {"TABLE"});
                System.out.println("List of tables: ");
                while (res.next()) {
                    System.out.println(
                            "   "+res.getString("TABLE_CAT")
                                    + ", "+res.getString("TABLE_SCHEM")
                                    + ", "+res.getString("TABLE_NAME")
                                    + ", "+res.getString("TABLE_TYPE")
                                    + ", "+res.getString("REMARKS"));
                }
                res.close();

                con.close();

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
        } catch (Exception e) {
            System.err.println("Exception: "+e.getMessage());
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
