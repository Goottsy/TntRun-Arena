package com.goottsy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import static com.goottsy.Utils.Tools.greenwarn;

public class MainListeners implements Listener {
    private TntRunCore instance;

    public MainListeners(TntRunCore instance) {
        this.instance = instance;
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (instance.getGame().isRunning()) {
            if (event.getMaterial() == Material.ENDER_PEARL) {
                player.setVelocity(player.getVelocity().add(new Vector(0, 1, 0)));
                player.sendMessage(greenwarn() + "¡Usaste tu salto!");
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (instance.getGame().isRunning() && event.getCause()== EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }
}
