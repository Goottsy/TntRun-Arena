package com.goottsy;

import lombok.Data;
import org.bukkit.configuration.file.FileConfiguration;

@Data
public class Game {
    TntRunCore instance;
    private FileConfiguration config;

    public Game(TntRunCore instance) {
        this.instance = instance;
        this.config = instance.getConfig();
    }


}
