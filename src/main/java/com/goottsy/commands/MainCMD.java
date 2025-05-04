package com.goottsy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.goottsy.TntGame;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.goottsy.TntRunCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.goottsy.TntGame.getJumpItem;
import static com.goottsy.Utils.Tools.*;

@CommandAlias("tnt")
@CommandPermission("tnt.run")
public class MainCMD extends BaseCommand {
    private TntRunCore instance;

    public MainCMD(TntRunCore instance) {
        this.instance = instance;
    }

    @Subcommand("join")
    public void onJoin(CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        TntGame game = TntRunCore.instance.getGame();
        game.addPlayer(player);
    }


    @Subcommand("start")
    public void onStart(CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        TntGame game = TntRunCore.instance.getGame();
        if (game.isRunning()) {
            player.sendMessage(redwarn() + "El juego ya está en curso...");
            return;
        }

        if (game.getPlayers().size() < 2) {
            player.sendMessage(redwarn() + "Se necesitan al menos 2 jugadores para iniciar...");
            return;
        }

        player.sendMessage(greenwarn() + "Inicio forzado del juego...");
        new BukkitRunnable() {
            int count = 10;

            @Override
            public void run() {
                if (count > 5) {
                    Bukkit.broadcastMessage(redwarn()+ red("El juego comienza en " + count + " segundos..."));
                } else if (count > 0) {
                    if (count == 5) {
                        Bukkit.broadcastMessage(greenwarn()+ green("¡El juego comenzará en 5 segundos!"));
                    }

                    titleall("", String.valueOf(count), 0, 20, 10);
                    playSoundall(Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                } else {
                    cancel();
                    game.startGame();
                }

                count--;
            }
        }.runTaskTimer(TntRunCore.instance, 0, 20);
    }

    @Subcommand("stop")
    public void onStop(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            TntGame game = TntRunCore.instance.getGame();

            if (!game.isRunning()) {
                player.sendMessage(redwarn() + "No hay un juego en curso...");
                return;
            }

            restore();
            Bukkit.broadcastMessage(redwarn() + "El juego ha sido detenido...");
        }
    }

    private static Map<Location, Material> savedBlocks = new HashMap<>();

    @Subcommand("world-save")
    public static void save() {
        savedBlocks.clear();
        World world = Bukkit.getWorld("world");
        List<Location> blockLocations = getBlocksInsideCube(genLoc( -16, 115, 17), genLoc(18, 7, -17));

        for (Location location : blockLocations) {
            Block block = world.getBlockAt(location);
            savedBlocks.put(location, block.getType());
        }
    }

    @Subcommand("world-restore")
    public static void restore() {
        World world = Bukkit.getWorld("world");

        for (Map.Entry<Location, Material> entry : savedBlocks.entrySet()) {
            Location loc = entry.getKey();
            Material material = entry.getValue();

            world.getBlockAt(loc).setType(material);
        }
    }



}
