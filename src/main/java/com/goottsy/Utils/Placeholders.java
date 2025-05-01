package com.goottsy.Utils;

import com.goottsy.PluginTemplate;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;



public class Placeholders extends PlaceholderExpansion {

    private PluginTemplate plugin;

    public Placeholders(PluginTemplate plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "plugin";
    }

    @Override
    public String getAuthor() {
        return "com.goottsy";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        return null;
    }

}
