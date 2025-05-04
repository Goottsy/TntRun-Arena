package com.goottsy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

import static com.goottsy.Utils.Tools.greenwarn;

public class MainListeners implements Listener {
    private TntRunCore instance;

    public MainListeners(TntRunCore instance) {
        this.instance = instance;
    }


    private final Map<Player, Long> lastUsedTime = new HashMap<>();
    @EventHandler
    public void onItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (instance.getGame().isRunning()) {
            if (event.getMaterial() == Material.GRAY_DYE) {

                long currentTime = System.currentTimeMillis();
                if (lastUsedTime.containsKey(player)) {
                    long timeElapsed = currentTime - lastUsedTime.get(player);

                    if (timeElapsed < 10*1000) {
                        long remainingTime = (10*1000 - timeElapsed) / 1000;
                        player.sendMessage(greenwarn() + "¡Debes esperar " + remainingTime + " segundos para usar el salto de nuevo!");
                        return;
                    }
                }

                lastUsedTime.put(player, currentTime);

                Vector direction = player.getLocation().getDirection().normalize();
                player.setVelocity(player.getVelocity().add(direction.multiply(1.5).setY(1)));
                player.sendMessage(greenwarn() + "¡Usaste tu salto!");

                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(null);
                }
            }
        }
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (instance.getGame().isRunning() && event.getCause()== EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (instance.getGame().isRunning()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (instance.getGame().isRunning() && event.getWhoClicked() instanceof Player) {
            event.setCancelled(true);
        }
    }
}
