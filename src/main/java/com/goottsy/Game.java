package com.goottsy;

import com.goottsy.Utils.CompassGui;
import lombok.Data;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static com.goottsy.Utils.Tools.consolecommand;

@Data
public class Game {
    PluginTemplate instance;
    private FileConfiguration config;
    String TextGPS ="";
    CompassGui compassGui;
    boolean target = false;

    //Class class;

    public Game(PluginTemplate instance) {
        this.instance = instance;
        this.config = instance.getConfig();
        this.compassGui = new CompassGui(instance);

        //varconfig = config.getBoolean("config");
        //this.class = new Class(instance);


    }

    public boolean isStaff(Player player) {
        return player.getScoreboardTags().contains("staff");
    }

    public boolean isPlayer(Player player) {
        return player.getScoreboardTags().contains("player");
    }

    public void pasteSchem(String s) {
        consolecommand("/world world");
        consolecommand("/schem load "+s);
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            consolecommand("/pa -oe");
        }, 5L);

    }

}
