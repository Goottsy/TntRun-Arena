package com.goottsy.Utils;

import com.goottsy.Utils.Builders.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public static String yellow(String text) {
        return ChatColor.YELLOW + text + ChatColor.RESET;
    }
    public static String gold(String text) {
        return ChatColor.GOLD + text + ChatColor.RESET;
    }
    public static String blue(String text) {
        return ChatColor.BLUE + text + ChatColor.RESET;
    }

    public static String darkblue(String text) {
        return ChatColor.DARK_BLUE + text + ChatColor.RESET;
    }

    public static String aqua(String text) {
        return ChatColor.AQUA + text + ChatColor.RESET;
    }

    public static String darkaqua(String text) {
        return ChatColor.DARK_AQUA + text + ChatColor.RESET;
    }

    public static String white(String text) {
        return ChatColor.WHITE + text + ChatColor.RESET;
    }

    public static String gray(String text) {
        return ChatColor.GRAY + text + ChatColor.RESET;
    }

    public static String darkgray(String text) {
        return ChatColor.DARK_GRAY + text + ChatColor.RESET;
    }

    public static String purple(String text) {
        return ChatColor.LIGHT_PURPLE + text + ChatColor.RESET;
    }

    public static String darkpurple(String text) {
        return ChatColor.DARK_PURPLE + text + ChatColor.RESET;
    }

    public static void actionbar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new net.md_5.bungee.api.chat.TextComponent(message));
    }

    public static void actionbarAll(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new net.md_5.bungee.api.chat.TextComponent(message));
        });
    }
    public static void playSoundall(String sound, float volume, float pitch) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Location loc = player.getLocation();
            player.playSound(loc, sound, SoundCategory.AMBIENT, volume, pitch);
        });
    }

    public static void playSoundsingle(Player player, Sound sound, float volume, float pitch) {
        Location loc = player.getLocation();
        player.playSound(loc, sound, SoundCategory.AMBIENT, volume, pitch);
    }


    public static void stopSound(String Sound){
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.stopSound(Sound,SoundCategory.AMBIENT);
        });
    }

    public static void titleall(String title, String subtitle, int fadein, int stay, int fadeout){
        for(Player pall : Bukkit.getOnlinePlayers()){
            pall.sendTitle(title, subtitle, fadein, stay, fadeout);
        }
    }

    public static void centeredMessage(Player player,String actionLine){
        player.sendMessage(CenteredMessage(actionLine));
    }

    public static void centeredMessageAll(String actionLine){
        for(Player player:Bukkit.getOnlinePlayers()) {
            player.sendMessage(CenteredMessage(actionLine));
        }
    }

    public static String CenteredMessage(String message){
        int CENTER_PX = 154;
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        return (sb.toString() + message);
    }


    public static ItemStack CustomItem(Material material, String itemName, String lore, int command) {
        ItemStack itemStack = (new ItemBuilder(material))
                .name(itemName)
                .lore(lore)
                .cmd(command)
                .build();
        return itemStack;
    }

    public static boolean isInCube(Location pos1, Location pos2, Location point) {

        var cX = pos1.getX() < pos2.getX();
        var cY = pos1.getY() < pos2.getY();
        var cZ = pos1.getZ() < pos2.getZ();

        var minX = cX ? pos1.getX() : pos2.getX();
        var maxX = cX ? pos2.getX() : pos1.getX();

        var minY = cY ? pos1.getY() : pos2.getY();
        var maxY = cY ? pos2.getY() : pos1.getY();

        var minZ = cZ ? pos1.getZ() : pos2.getZ();
        var maxZ = cZ ? pos2.getZ() : pos1.getZ();

        if (point.getX() < minX || point.getY() < minY || point.getZ() < minZ)
            return false;
        if (point.getX() > maxX || point.getY() > maxY || point.getZ() > maxZ)
            return false;

        return true;
    }

    public static void consolecommand(String string) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
    }

    public static void playerEffect(Player player, PotionEffectType effectType, int duration, int amplifier){
        player.addPotionEffect(new PotionEffect(effectType, duration, amplifier, false, false, false));
    }

    public static void fill(Location loc1, Location loc2, Material material) {
        var locations = getBlocksInsideCube(loc1, loc2);
        locations.forEach(loc -> {
            loc.getBlock().setType(material);
        });
    }

    public static Location genLoc(int x, int y, int z){
        return new Location(Bukkit.getWorld("world"), x,y,z);
    }

    public static Location genLoc(String world, int x, int y, int z){
        return new Location(Bukkit.getWorld(world), x,y,z);
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

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static String translateHexColorCodes(final String message) {
        final char colorChar = ChatColor.COLOR_CHAR;
        final Matcher matcher = HEX_PATTERN.matcher(message);
        final StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {
            final String group = matcher.group(1);

            matcher.appendReplacement(buffer, colorChar + "x"
                    + colorChar + group.charAt(0) + colorChar + group.charAt(1)
                    + colorChar + group.charAt(2) + colorChar + group.charAt(3)
                    + colorChar + group.charAt(4) + colorChar + group.charAt(5));
        }

        return matcher.appendTail(buffer).toString();
    }
}
