package com.goottsy.Utils;

import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class Tools {

    public static String redwarn() { return darkred("( ")+red("!")+darkred(" ) ");}

    public static String greenwarn() {return darkgreen("( ")+green("!")+darkgreen(" ) ");}

    public static String red(String text) {
        return ChatColor.RED + text + ChatColor.RESET;
    }

    public static String darkred(String text) {
        return ChatColor.DARK_RED + text + ChatColor.RESET;
    }
    public static String green(String text) {
        return ChatColor.GREEN + text + ChatColor.RESET;
    }

    public static String darkgreen(String text) {
        return ChatColor.DARK_GREEN + text + ChatColor.RESET;
    }

    public static void actionbarAll(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new net.md_5.bungee.api.chat.TextComponent(message));
        });
    }

    public static void playSoundall(Sound sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Location loc = player.getLocation();
            player.playSound(loc, sound, SoundCategory.AMBIENT, volume, pitch);
        });
    }


    public static void titleall(String title, String subtitle, int fadein, int stay, int fadeout){
        for(Player pall : Bukkit.getOnlinePlayers()){
            pall.sendTitle(title, subtitle, fadein, stay, fadeout);
        }
    }

    public static Location genLoc(int x, int y, int z){
        return new Location(Bukkit.getWorld("world"), x,y,z);
    }

    public static List<Location> getBlocksInsideCube(Location loc1, Location loc2) {
        List<Location> locations = new ArrayList<>();

        var xa = (int) (loc1.getX() > loc2.getX() ? loc1.getX() : loc2.getX());
        var ya = (int) (loc1.getY() > loc2.getY() ? loc1.getY() : loc2.getY());
        var za = (int) (loc1.getZ() > loc2.getZ() ? loc1.getZ() : loc2.getZ());

        var xi = (int) (loc1.getX() < loc2.getX() ? loc1.getX() : loc2.getX());
        var yi = (int) (loc1.getY() < loc2.getY() ? loc1.getY() : loc2.getY());
        var zi = (int) (loc1.getZ() < loc2.getZ() ? loc1.getZ() : loc2.getZ());

        for (int x = xi; x <= xa; x++) {
            for (int y = yi; y <= ya; y++) {
                for (int z = zi; z <= za; z++) {
                    locations.add(new Location(loc1.getWorld(), x, y, z));
                }
            }
        }

        return locations;
    }
}
