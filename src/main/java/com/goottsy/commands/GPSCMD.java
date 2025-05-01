package com.goottsy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.goottsy.PluginTemplate;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

import static com.goottsy.Utils.Tools.*;

@CommandAlias("anty")
@Subcommand("gps")
@CommandPermission("admin.perm")
public class GPSCMD extends BaseCommand {
    private static PluginTemplate instance;
    private Location DestinyLocation;
    public GPSCMD(PluginTemplate instance) {
        this.instance = instance;
    }
    @Subcommand("remove")
    public void remove(CommandSender sender) {
        var game = instance.getGame();
        game.getCompassGui().destroyBossbars();

        sender.sendMessage(redwarn()+ "Objetivo cancelado");
        DestinyLocation = null;
    }

    @Subcommand("set")
    @CommandCompletion("@players|@a <x> <y> <z>")
    public void set(CommandSender sender, String target, int x, int y, int z) {
        var game = instance.getGame();
        game.getCompassGui().destroyBossbars();
        game.setTarget(false);
        DestinyLocation = new Location(Bukkit.getWorld("world"), x, y, z);

        if (target.equals("@a")) {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            game.getCompassGui().startTravel(players, DestinyLocation);
        } else {
            Player player = Bukkit.getPlayer(target);
            if (player != null) {
                game.getCompassGui().startTravel(Collections.singletonList(player), DestinyLocation);
            } else {
                sender.sendMessage(redwarn() + "¡El jugador " + target + " no está en línea!");
                return;
            }
        }

        sender.sendMessage(greenwarn() + "Objetivo creado para " + target + " " + x + " " + y + " " + z);
    }

    @Subcommand("target")
    @CommandCompletion("player|uuid @players")
    public void target(CommandSender sender, String type, String target) {
        var game = instance.getGame();
        game.getCompassGui().destroyBossbars();
        game.setTarget(true);

        if (type.equals("player")) {
            Player player = Bukkit.getPlayer(target);
            if (player != null) {
                Location destinyLocation = player.getLocation();
                game.getCompassGui().setPlayer(player);
                List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                game.getCompassGui().startTravel(players, destinyLocation);
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false, false));
            } else {
                sender.sendMessage(redwarn() + "¡El jugador " + target + " no está en línea!");
                return;
            }
        } else if (type.equals("uuid")) {
            try {
                UUID uuid = UUID.fromString(target);
                Entity entity = Bukkit.getEntity(uuid);

                if (entity != null) {
                    Location destinyLocation = entity.getLocation();
                    game.getCompassGui().setPlayer(entity);
                    List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
                    game.getCompassGui().startTravel(players, destinyLocation);
                } else {
                    sender.sendMessage(redwarn() + "¡No se encontró la entidad con UUID " + target + "!");
                    return;
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage(redwarn() + "¡UUID " + target + " no es válido!");
                return;
            }
        } else {
            sender.sendMessage(redwarn() + "Tipo de objetivo no válido. Usa 'player' o 'uuid'.");
            return;
        }

        sender.sendMessage(greenwarn() + "Objetivo creado para " + target);
    }


    @Subcommand("teleport")
    public void teleport(CommandSender sender) {
        var game = instance.getGame();
        Player player = (Player) sender;

        if(DestinyLocation != null){
            player.teleport(DestinyLocation);
            sender.sendMessage(greenwarn()+ "Has sido teletrasportado");
        }else {
            sender.sendMessage(redwarn()+"No hay ubicacion disponible");
        }
    }

    @Subcommand("text")
    @CommandCompletion("<texto>")
    public void text(CommandSender sender, String string) {
        var game = instance.getGame();
        instance.getGame().setTextGPS(legacyParse(string));
        sender.sendMessage(greenwarn()+green("Texto Cambiado a: ")+legacyParse(string));
    }

    public static String legacyParse(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}