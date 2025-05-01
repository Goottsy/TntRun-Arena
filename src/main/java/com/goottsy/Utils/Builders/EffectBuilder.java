package com.goottsy.Utils.Builders;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * EffectBuilder(PotionEffectType.SPEED)
 *                 .duration(seconds * 20)
 *                 .amplifier(level)
 *                 .ambient(true)
 *                 .particles(true)
 *                 .build();
 */
public class EffectBuilder {

    private PotionEffectType type;
    private int duration;
    private int amplifier;
    private boolean ambient;
    private boolean particles;
    private boolean icon;

    public EffectBuilder(PotionEffectType type) {
        this.type = Objects.requireNonNull(type, "Effect type cannot be null");
        this.duration = 200;
        this.amplifier = 0;
        this.ambient = false;
        this.particles = false;
        this.icon = false;
    }

    public static EffectBuilder of(PotionEffectType type) {
        return new EffectBuilder(type);
    }

    public EffectBuilder type(PotionEffectType type) {
        this.type = Objects.requireNonNull(type, "Effect type cannot be null");
        return this;
    }

    public EffectBuilder duration(int duration) {
        this.duration = Math.max(1, duration);
        return this;
    }

    public EffectBuilder amplifier(int amplifier) {
        this.amplifier = Math.max(0, amplifier);
        return this;
    }

    public EffectBuilder ambient(boolean ambient) {
        this.ambient = ambient;
        return this;
    }

    public EffectBuilder particles(boolean particles) {
        this.particles = particles;
        return this;
    }

    public EffectBuilder icon(boolean icon) {
        this.icon = icon;
        return this;
    }

    public EffectBuilder edit(Consumer<EffectBuilder> function) {
        function.accept(this);
        return this;
    }

    public PotionEffect build() {
        return new PotionEffect(type, duration, amplifier, ambient, particles, icon);
    }


}
