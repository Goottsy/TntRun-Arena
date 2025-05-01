package com.goottsy;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.List;

import static com.goottsy.Utils.Tools.*;

public class TntGame {
    private World world;
    @Getter
    private List<Player> players;
    private boolean isRunning;
    private static final int GAME_DURATION = 600;
    private static final int MIN_PLAYERS = 2;
    private static final int WAIT_TIME = 10*20;

    public TntGame(World world) {
        this.world = world;
        this.players = new ArrayList<>();
        this.isRunning = false;
    }

    public void addPlayer(Player player) {
        if (isRunning) {
            player.sendMessage("¡El juego ya está en curso!");
            return;
        }

        players.add(player);
        player.sendMessage("Te has unido. Esperando a más jugadores...");

        if (players.size() >= MIN_PLAYERS) {
            player.sendMessage("El juego comenzará en 10 segundos...");

            new BukkitRunnable() {
                @Override
                public void run() {
                    startGame();
                }
            }.runTaskLater(TntRunCore.instance, WAIT_TIME);
        }
    }


    public void startGame() {
        if (isRunning || players.size() < MIN_PLAYERS) {
            return;
        }

        isRunning = true;
        actionbarAll(greenwarn() + "¡El Juego Ha comenzando!");

        for(Player player:getPlayers()){
            player.getInventory().addItem(getJumpItem());
        }

        startBlockRemoving();

        new BukkitRunnable() {
            @Override
            public void run() {
                endGame();
            }
        }.runTaskLater(TntRunCore.instance, GAME_DURATION * 20L);
    }

    public void endGame() {
        isRunning = false;

        if (players.size() == 1) {
            Player winner = players.get(0);
            winner.sendMessage(greenwarn() + "¡Has ganado!");
        } else {
            actionbarAll(redwarn() + "El juego ha terminado...");

        }

        players.clear();
    }

    public static ItemStack getJumpItem() {
        ItemStack jumpItem = new ItemStack(Material.ENDER_PEARL);
        jumpItem.getItemMeta().setCustomModelData(3);
        return jumpItem;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private static final List<Material> blocks = List.of(
            Material.TNT,
            Material.SAND,
            Material.SANDSTONE,
            Material.GRAVEL,
            Material.RED_SANDSTONE,
            Material.RED_SAND
    );

    public void startBlockRemoving() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : players) {
                    if (!isRunning) cancel();

                    if (players.isEmpty()) cancel();

                    for (Block block : getRemovableBlocks(player)) {
                        if (blocks.contains(block.getType().name())) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }.runTaskTimer(TntRunCore.instance, 0, 1);
    }

    private List<Block> getRemovableBlocks(Player user) {
        List<Block> removableBlocks = new ArrayList<>();
        Location loc = user.getLocation();
        int scanDepth = user.isOnGround() ? 1 : 2;
        int y = loc.getBlockY();

        for (int i = 0; i <= scanDepth; i++) {
            Block block = new Location(loc.getWorld(), loc.getX(), y--, loc.getZ()).getBlock();
            if (block != null) {
                removableBlocks.add(block);
            }
        }

        return removableBlocks;
    }

}
