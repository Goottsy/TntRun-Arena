package com.goottsy;

import com.goottsy.commands.MainCMD;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;

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

    private boolean isCountdownStarted = false;

    public void addPlayer(Player player) {
        if (isRunning) {
            player.sendMessage("El juego ya está en curso...");
            return;
        }

        if (players.contains(player)) {
            player.sendMessage(redwarn()+red("Ya estás en la partida..."));
            return;
        }

        player.teleport(genLoc(1, 86, 0));
        players.add(player);
        player.sendMessage(greenwarn()+green("Esperando a más jugadores..."));

        if (players.size() >= MIN_PLAYERS && !isCountdownStarted) {
            isCountdownStarted = true;

            new BukkitRunnable() {
                int count = 10;

                @Override
                public void run() {
                    if (count > 5) {
                        Bukkit.broadcastMessage(red("El juego comienza en " + count + " segundos..."));
                    } else if (count > 0) {
                        if (count == 5) {
                            Bukkit.broadcastMessage(green("¡El juego comenzará en 5 segundos!"));
                        }

                        titleall("", String.valueOf(count), 0, 20, 10);
                        playSoundall(Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    } else {
                        cancel();
                        startGame();
                    }

                    count--;
                }
            }.runTaskTimer(TntRunCore.instance, 0, 20);

        }
    }




    public void startGame() {
        if (isRunning || players.size() < MIN_PLAYERS) {
            return;
        }

        MainCMD.save();
        isRunning = true;
        actionbarAll(greenwarn() + "El Juego Ha comenzando...");

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

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.getInventory().clear();
                if (p.equals(winner)) {
                    p.sendTitle(green("¡Ganaste!"), "", 10, 60, 20);
                } else {
                    p.sendTitle(red("Fin del juego"), green("Ha Ganado: ") + winner.getName(), 10, 60, 20);
                }
            }

            for (int i = 0; i < 3; i++) {
                Bukkit.getScheduler().runTaskLater(TntRunCore.instance, () -> {
                    Firework fw = winner.getWorld().spawn(winner.getLocation(), Firework.class);
                    FireworkMeta meta = fw.getFireworkMeta();
                    meta.addEffect(FireworkEffect.builder()
                            .withColor(Color.LIME)
                            .withFade(Color.WHITE)
                            .with(FireworkEffect.Type.BALL_LARGE)
                            .trail(true)
                            .flicker(true)
                            .build());
                    meta.setPower(1);
                    fw.setFireworkMeta(meta);
                }, i * 20L);
            }
            MainCMD.restore();

        } else {
            actionbarAll(redwarn() + "El juego ha terminado...");
        }

        players.clear();
    }


    public static ItemStack getJumpItem() {
        ItemStack jumpItem = new ItemStack(Material.GRAY_DYE,3);
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
                if (!isRunning || players.isEmpty()) {
                    cancel();
                    return;
                }

                List<Player> toRemove = new ArrayList<>();

                for (Player player : players) {
                    if (player.getLocation().getY() < 6) {
                        player.sendMessage(redwarn() + "¡Has sido eliminado!");
                        toRemove.add(player);
                        continue;
                    }

                    Block block1 = getBlockUnderPlayer(player.getLocation().subtract(0,1,0));
                    Block block2 = getBlockUnderPlayer(player.getLocation().subtract(0,2,0));

                    if (block1 != null && blocks.contains(block1.getType())) {
                        block1.setType(Material.AIR);
                        block2.setType(Material.AIR);
                    }
                }


                for (Player eliminated : toRemove) {
                    players.remove(eliminated);
                    eliminated.setGameMode(org.bukkit.GameMode.SPECTATOR);
                    eliminated.teleport(world.getSpawnLocation());
                }

                if (players.size() == 1) {
                    endGame();
                    cancel();
                }
            }
        }.runTaskTimer(TntRunCore.instance, 0, 1);
    }


    private Block getBlockUnderPlayer(Location location) {
        int y = location.getBlockY();
        PlayerPosition loc = new PlayerPosition(location.getX(), y, location.getZ());
        Block b11 = loc.getBlock(location.getWorld(), +0.3, -0.3);
        if (b11.getType() != Material.AIR && b11.getType() != Material.LIGHT) {
            return b11;
        }
        Block b12 = loc.getBlock(location.getWorld(), -0.3, +0.3);
        if (b12.getType() != Material.AIR && b12.getType() != Material.LIGHT) {
            return b12;
        }
        Block b21 = loc.getBlock(location.getWorld(), +0.3, +0.3);
        if (b21.getType() != Material.AIR && b21.getType() != Material.LIGHT) {
            return b21;
        }
        Block b22 = loc.getBlock(location.getWorld(), -0.3, -0.3);
        if (b22.getType() != Material.AIR && b22.getType() != Material.LIGHT) {
            return b22;
        }
        return null;
    }

    private static class PlayerPosition {

        private double x;
        private int y;
        private double z;

        public PlayerPosition(double x, int y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Block getBlock(World world, double addx, double addz) {
            return world.getBlockAt(NumberConversions.floor(x + addx), y, NumberConversions.floor(z + addz));
        }
    }



}
