package com.goottsy.Utils.Builders;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * new FireworkBuilder()
 *     .type(FireworkEffect.Type.STAR)
 *     .color(Color.BLUE)
 *     .fade(Color.YELLOW)
 *     .flicker(true)
 *     .trail(true)
 *     .power(2)
 *     .launch(player.getLocation());
 */
public class FireworkBuilder {
    private FireworkEffect.Type type = FireworkEffect.Type.BALL;
    private Color color = Color.RED;
    private Color fade = Color.WHITE;
    private boolean flicker = false;
    private boolean trail = false;
    private int power = 1;

    public FireworkBuilder type(FireworkEffect.Type type) {
        this.type = type;
        return this;
    }

    public FireworkBuilder color(Color color) {
        this.color = color;
        return this;
    }

    public FireworkBuilder fade(Color fade) {
        this.fade = fade;
        return this;
    }

    public FireworkBuilder flicker(boolean flicker) {
        this.flicker = flicker;
        return this;
    }

    public FireworkBuilder trail(boolean trail) {
        this.trail = trail;
        return this;
    }

    public FireworkBuilder power(int power) {
        this.power = Math.max(1, Math.min(3, power)); // Power entre 1 y 3
        return this;
    }

    public void launch(Location location) {
        World world = location.getWorld();
        if (world == null) return;

        Firework firework = world.spawn(location, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().with(type).withColor(color).withFade(fade).flicker(flicker).trail(trail).build());
        meta.setPower(power);
        firework.setFireworkMeta(meta);
    }
}
