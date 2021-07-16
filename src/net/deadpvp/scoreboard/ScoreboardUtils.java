package net.deadpvp.scoreboard;

import net.deadpvp.Main;
import net.deadpvp.utils.UtilityFunctions;
import net.deadpvp.utils.sqlUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;

public class ScoreboardUtils {

    public static void setScoreBoard(Player player) {
        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 59-date.getMinutes();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("§4§lDEAD§1§lPVP", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score score15 = obj.getScore("§c§l§r");
        Score score14 = obj.getScore("§b§lVOTRE PROFIL");
        Score score11 = obj.getScore("§f§2 ");
        Score score10 = obj.getScore("§c§lSERVEUR");
        Score score6 = obj.getScore("§f§l§c");

        Score score3 = obj.getScore("§7§l§c");
        //Score score3 = obj.getScore("§dEn cas de bug faites");
        //Score score2 = obj.getScore("§d/bug <votre bug> !");
        Score score1 = obj.getScore("§c§b§l---------------§r");
        Score score0 = obj.getScore("§b§lmc.deadpvp.com");

        score0.setScore(0);
        score1.setScore(1);
        //score2.setScore(2);
        //score3.setScore(3);
        score6.setScore(6);

        score3.setScore(3);
        score11.setScore(11);
        score15.setScore(15);

        score10.setScore(10);

        score14.setScore(14);

        Team Mystiques = board.registerNewTeam("Mystiques");
        Mystiques.addEntry(ChatColor.GOLD + "" + ChatColor.GOLD);
        Mystiques.setPrefix(ChatColor.WHITE+"§f≫ Mystique");
        try {
            Object mystiqueint = sqlUtilities.getData("moneyserv","player",player.getName(),"mystiques","Int");
            Mystiques.setSuffix("§fs: §d"+ mystiqueint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        obj.getScore(ChatColor.GOLD + "" + ChatColor.GOLD).setScore(13);


        Team Karma = board.registerNewTeam("Karma");
        Karma.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.DARK_PURPLE);
        Karma.setPrefix(ChatColor.WHITE+"§f≫ Karma: ");
        try {
            Object karmaint = sqlUtilities.getData("moneyserv","player",player.getName(),"karma","Int");
            Karma.setSuffix("§d"+karmaint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        };
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.DARK_PURPLE).setScore(12);

        Team onlineCounter = board.registerNewTeam("onlineCounter");
        onlineCounter.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC);
        onlineCounter.setPrefix(ChatColor.WHITE+"≫ Global: ");
        onlineCounter.setSuffix("§6"+Main.getInstance().playerCount+"");
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC).setScore(9);

        Team maxco = board.registerNewTeam("maxco");
        maxco.addEntry(ChatColor.BLACK + "" + ChatColor.WHITE);
        maxco.setPrefix(ChatColor.WHITE+"≫ Connexions ");
        maxco.setSuffix("§fmax: §c§l"+UtilityFunctions.getmaxco());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.WHITE).setScore(5);


        Team creacounter = board.registerNewTeam("creacounter");
        creacounter.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.GOLD);
        creacounter.setPrefix(ChatColor.WHITE+"≫ Creatif: ");
        creacounter.setSuffix("§6"+Main.getInstance().creatifcount+"");
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.GOLD).setScore(8);

        Team pvpsoupcounter = board.registerNewTeam("pvpsoupcounter");
        pvpsoupcounter.addEntry(ChatColor.LIGHT_PURPLE + "" + ChatColor.RED);
        pvpsoupcounter.setPrefix(ChatColor.WHITE+"≫ PvpSoup: ");
        pvpsoupcounter.setSuffix("§6"+Main.getInstance().pvpsoupcount+"");
        obj.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.RED).setScore(7);




        player.setScoreboard(board);
    }

    public static void updateScoreBoard(Player player) {


        TimeZone tz = TimeZone.getTimeZone("Europe/Paris");
        Date date = new Date();
        int x = (22-date.getHours())-1;
        int y = 59-date.getMinutes();
        String y2;
        if (y < 10){
            int temp = y;
            y2 = "0"+y;
        }else{
            y2 = ""+y;
        }
        int players = Main.getInstance().playerCount;
        Scoreboard board = player.getScoreboard();
        int max = UtilityFunctions.getmaxco();
        //board.getTeam("onlineCounter").setPrefix(ChatColor.WHITE+"≫ Global : ");
        board.getTeam("onlineCounter").setSuffix("§6§l"+Main.getInstance().playerCount);
        if(board.getTeam("onlineCounter").getName().length() >=16){
            board.getTeam("onlineCounter").setPrefix("§4Erreur");
        }
        try {
            Object karmaint = sqlUtilities.getData("moneyserv","player",player.getName(),"karma","Int");
            board.getTeam("Karma").setSuffix("§d"+karmaint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        Object mystiqueint = null;
        try {
            mystiqueint = sqlUtilities.getData("moneyserv","player",player.getName(),"mystiques","Int");
            board.getTeam("Mystiques").setSuffix("§fs: §d"+ mystiqueint.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        board.getTeam("maxco").setSuffix("§fmax: §c§l"+UtilityFunctions.getmaxco());
        board.getTeam("pvpsoupcounter").setSuffix("§6§l"+Main.getInstance().pvpsoupcount+"");
        board.getTeam("creacounter").setSuffix("§6§l"+Main.getInstance().creatifcount+"");



    }
}
