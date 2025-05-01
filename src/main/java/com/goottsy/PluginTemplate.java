package com.goottsy;

import co.aikar.commands.PaperCommandManager;
import com.goottsy.Utils.Placeholders;
import com.goottsy.Utils.Types;
import com.goottsy.commands.MainCMD;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static com.goottsy.Utils.Tools.*;

public final class PluginTemplate extends JavaPlugin {

    private static PluginTemplate instance;
    public static PaperCommandManager commandManager;
	private Game game;
    @Override
    public void onEnable() {
        instance = this;
        commandManager = new PaperCommandManager(this);
		game = new Game(this);
        new Types(this);
        commandManager.registerCommand(new MainCMD(this));

        //registerListener(new MainListeners(this));

        registerPlaceholders();
        getLogger().info( green(instance.getName() + " ha sido Activado 1.0"));
    }

    @Override
    public void onDisable() {
        getLogger().info(red(instance.getName() + " ha sido Desactivado"));
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }
	public Game getGame() {
        return this.game;
    }
    public void registerPlaceholders() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new Placeholders(this).register();
            getLogger().info( green("GoottsyTools: Placeholders Activadas"));}

        else {getLogger().info( red("GoottsyTools: Placeholders Desactivadas"));}
    }

}
