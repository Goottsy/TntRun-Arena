package com.goottsy.Utils.Builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Uso:
 * new ParticleBuilder(Particle.FLAME)
 *     .count(20)
 *     .offset(0.3, 1, 0.3)
 *     .spawn(player.getWorld(), player.getLocation());
 *
 *     new ParticleBuilder(Particle.FLAME)
 *     .count(20)
 *     .offset(0.3, 1, 0.3)
 *     .spawn(player.getWorld(), player.getLocation());
 *
 * // Llamada con color (REDSTONE)
 * new ParticleBuilder(Particle.REDSTONE)
 *     .color(Color.fromRGB(255, 0, 0)) // Rojo
 *     .count(5)
 *     .spawn(world, location);
 *
 * // Partícula visible solo para un jugador
 * ParticleBuilder.of(Particle.HEART)
 *     .count(3)
 *     .offset(0.2, 0.5, 0.2)
 *     .spawnFor(player, player.getLocation());
 */

public class ParticleBuilder {
    private final Particle particle;
    private int count = 10;
    private double offsetX = 0.5, offsetY = 0.5, offsetZ = 0.5;
    private double extra = 0;
    private Object data = null; // Para partículas con datos (ej. bloques, ítems, color)

    public ParticleBuilder(Particle particle) {
        this.particle = particle;
    }

    public static ParticleBuilder of(Particle particle) {
        return new ParticleBuilder(particle);
    }

    public ParticleBuilder count(int count) {
        this.count = count;
        return this;
    }

    public ParticleBuilder offset(double x, double y, double z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
        return this;
    }

    public ParticleBuilder extra(double extra) {
        this.extra = extra;
        return this;
    }

    // 🎨 Para partículas REDSTONE
    public ParticleBuilder color(Color color) {
        if (particle == Particle.REDSTONE) {
            this.data = new Particle.DustOptions(color, 1);
        }
        return this;
    }

    // 🏗️ Para partículas BLOCK_CRACK, BLOCK_DUST
    @SuppressWarnings("deprecation")
    public ParticleBuilder blockData(MaterialData materialData) {
        if (particle == Particle.BLOCK_CRACK || particle == Particle.BLOCK_DUST) {
            this.data = materialData;
        }
        return this;
    }

    // 🎒 Para partículas ITEM_CRACK
    public ParticleBuilder itemData(ItemStack item) {
        if (particle == Particle.ITEM_CRACK) {
            this.data = item.getData();
        }
        return this;
    }

    // 🌍 Spawn en el mundo
    public void spawn(World world, Location location) {
        world.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }

    // 👀 Spawn solo para un jugador
    public void spawnFor(Player player, Location location) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra, data);
    }
}
