package net.DeadPvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grades {
    public static void updateGrade(Player p, String grade, int date) {

        File playerFile = new File("/home/ubuntu/server/caca/plugins/DeadPvp/players/" + grade + "/" + p.getName() + ".yml");

        try {
            if (!playerFile.exists()) playerFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(playerFile.getAbsolutePath()));
            String line = reader.readLine();
            PrintWriter out = new PrintWriter(playerFile);
            if (line == null || line.isEmpty() || line == "") out.print(date);
            else {
                int jours = Integer.valueOf(line);
                out.print(jours + date);
            }
            out.close();
        } catch (IOException e1) {
            Bukkit.getLogger().severe(ChatColor.RED + "Upgrade de grade failed, " + grade + ", " + p.getName());
            e1.printStackTrace();
        }

    }

    public static boolean hasGrade(Player p, String grade) {
        File playerFile = new File("/home/ubuntu/server/caca/plugins/DeadPvp/players/" + grade + "/" + p.getName() + ".yml");
        if (playerFile.exists()) return true;
        return false;
    }

    public static void reduceFromOneDayAllSubs() {
        try {
            File directoryPath = new File("/home/ubuntu/server/caca/plugins/DeadPvp/players/");
            //List of all files and directories
            String contents[] = directoryPath.list();
            if (contents == null) return;
            if (contents.length == 0) return;

            for (String grade : contents) {
                File grades = new File("/home/ubuntu/server/caca/plugins/DeadPvp/players/" + grade + "/");
                String gradeslist[] = grades.list();
                for (String players : gradeslist) {
                    File player = new File("/home/ubuntu/server/caca/plugins/DeadPvp/players/"+grade+"/" + players);
                    BufferedReader reader = new BufferedReader(new FileReader(player));
                    String line = reader.readLine();

                    while (line != null)
                    {
                        try {
                            int jours = Integer.parseInt(line);
                            if (jours == 1){
                                player.delete();
                                return;
                            }
                            PrintWriter writer = new PrintWriter(new File (player.getAbsolutePath()));
                            writer.print(jours-1);
                            writer.close();
                        }
                        catch (Exception e){ System.out.println(e);}
                        line = reader.readLine();
                    }
                    reader.close();
                }
            }
        }
        catch (Exception ee) {
            System.out.println(ee);
        }

    }
}