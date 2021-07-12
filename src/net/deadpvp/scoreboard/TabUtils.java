package net.deadpvp.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class TabUtils {

    public static Team admin;
    public static Team dev;
    public static Team modo;
    public static Team builder;
    public static Team joueur;

    public static void settab(Player pl){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = pl.getScoreboard();
        admin = board.getTeam("001-Admin");
        if(admin == null){
            admin = board.registerNewTeam("001-Admin");
            admin.setPrefix("§4[Admin] ");
        }

        dev = board.getTeam("002-Dev");
        if(dev == null){
            dev = board.registerNewTeam("002-Dev");
            dev.setPrefix("§c[Développeur] ");
        }

        modo = board.getTeam("003-modo");
        if(modo == null){
            modo = board.registerNewTeam("003-modo");
            modo.setPrefix("§6[Modérateur] ");
        }

        builder = board.getTeam("004-builder");
        if(builder == null){
            builder = board.registerNewTeam("004-builder");
            builder.setPrefix("§9[Builder] ");
        }

        joueur = board.getTeam("005-joueur");
        if(joueur == null){
            joueur = board.registerNewTeam("005-joueur");
            joueur.setPrefix("§7");
        }

        if(pl.hasPermission("chat.admin")){
            pl.setPlayerListName(admin.getPrefix()+pl.getPlayer().getName());
            admin.addPlayer(pl);
            admin.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.dev")){
            pl.setPlayerListName(dev.getPrefix()+pl.getPlayer().getName());
            dev.addPlayer(pl);
            dev.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.modo")){
            pl.setPlayerListName(modo.getPrefix()+pl.getPlayer().getName());
            modo.addPlayer(pl);
            modo.addEntry(pl.getName());
        }else if (pl.hasPermission("chat.builder")){
            pl.setPlayerListName(builder.getPrefix()+pl.getPlayer().getName());
            builder.addPlayer(pl);
            builder.addEntry(pl.getName());
        }else{
            pl.setPlayerListName(joueur.getPrefix()+pl.getPlayer().getName());
            joueur.addPlayer(pl);
            joueur.addEntry(pl.getName());
        }
        pl.setScoreboard(board);




    }
}
