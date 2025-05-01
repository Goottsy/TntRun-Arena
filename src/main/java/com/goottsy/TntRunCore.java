package com.goottsy;

import co.aikar.commands.PaperCommandManager;
import com.goottsy.Utils.Placeholders;
import com.goottsy.Utils.Types;
import com.goottsy.commands.MainCMD;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static com.goottsy.Utils.Tools.*;

public final class TntRunCore extends JavaPlugin {

    public static TntRunCore instance;
    public static PaperCommandManager commandManager;
	@Getter
    private Game game;
    private TntGame tntGame;
    @Override
    public void onEnable() {
        instance = this;
        commandManager = new PaperCommandManager(this);
		game = new Game(this);
        new Types(this);
        World world = getServer().getWorlds().get(0);
        tntGame = new TntGame(world);
        commandManager.registerCommand(new MainCMD(this));

        registerListener(new MainListeners(this));

        registerPlaceholders();
        getLogger().info( green(instance.getName() + " ha sido Activado 1.0"));
    }

    @Override
    public void onDisable() {
        getLogger().info(red(instance.getName() + " ha sido Desactivado"));

        if (tntGame != null && tntGame.isRunning()) {
            tntGame.endGame();
        }
    }

    public TntGame getGame() {
        return tntGame;
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

    public void registerPlaceholders() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Placeholders(this).register();
            getLogger().info( green("GoottsyTools: Placeholders Activadas"));}

        else {getLogger().info( red("GoottsyTools: Placeholders Desactivadas"));}
    }

}
