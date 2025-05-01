package com.goottsy.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.goottsy.TntGame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.goottsy.TntRunCore;
import org.bukkit.scheduler.BukkitRunnable;

import static com.goottsy.TntGame.getJumpItem;
import static com.goottsy.Utils.Tools.*;

@CommandAlias("tnt")
@CommandPermission("tnt.run")
public class MainCMD extends BaseCommand {
    private TntRunCore instance;

    public MainCMD(TntRunCore instance) {
        this.instance = instance;
    }

    @Subcommand("start")
    public void onStart(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (TntRunCore.instance.getGame().isRunning()) {
                player.sendMessage(redwarn() + "¡El juego ya está en curso!");
                return;
            }

            TntGame game = TntRunCore.instance.getGame();
            game.addPlayer(player);

            if (game.getPlayers().size() >= 2) {
                player.sendMessage(greenwarn() + "El juego comenzará en 10 segundos...");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        game.startGame();
                    }
                }.runTaskLater(TntRunCore.instance, 200);
            } else {
                player.sendMessage(yellow("Esperando a más jugadores..."));
            }
        }
    }

}
