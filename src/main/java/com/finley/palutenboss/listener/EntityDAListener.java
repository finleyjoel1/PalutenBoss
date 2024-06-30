package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

public class EntityDAListener implements Listener {

    public EntityDAListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
    }

    @EventHandler
    public void handle(EntityDamageByEntityEvent event) {
        Entity target = event.getEntity();
        Entity damager = event.getDamager();
        String customName = target.getCustomName();
        EntityType entityType = target.getType();
        EntityType zombieType = EntityType.ZOMBIE;

        if (customName == null) {
            return;
        }

        if (entityType == zombieType && customName.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getBossName()))) {
            if (damager instanceof Player player) {
                if (player.getItemInHand().getItemMeta() == null) return;
                if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getBossName()) + " §c§lSword")) {
                    event.setDamage(1.5D);
                    return;
                }
                event.setDamage(0.5D);
            }
        }

        if (entityType == EntityType.PLAYER) {
            if (damager instanceof Zombie && Objects.requireNonNull(damager.getCustomName()).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getBossName()))) {
                event.setDamage(2.5D);
            }
        }
    }

    @EventHandler
    public void handle(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Zombie palutenBoss)) {
            return;
        }

        String customName = palutenBoss.getCustomName();

        if (customName == null) {
            return;
        }

        String palutenBossName = ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getBossName());
        EntityDamageEvent.DamageCause damageCause = event.getCause();
        EntityDamageEvent.DamageCause attackCause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

        if (customName.equalsIgnoreCase(palutenBossName) && damageCause == attackCause) {
            Player player = PalutenBoss.getInstance().getEntityManager().findNearbyPlayer(palutenBoss);

            if (player == null) {
                return;
            }

            if (palutenBoss.getHealth() < 100) {
                palutenBoss.teleport(player);
            }

            palutenBoss.setVisualFire(false);
            palutenBoss.setFireTicks(0);
        }
    }


}