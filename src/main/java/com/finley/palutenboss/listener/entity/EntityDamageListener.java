package com.finley.palutenboss.listener.entity;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

public class EntityDamageListener implements Listener {

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

        if (entityType == zombieType && customName.equalsIgnoreCase(PalutenBoss.getInstance().getBossName())) {

            if (damager instanceof Player) {
                Player player = (Player) damager;
                if (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(PalutenBoss.getInstance().getBossName() + " §c§lSword")) {
                    event.setDamage(1.5D);
                    return;
                }
                event.setDamage(0.5D);
            }

        }

        if (entityType == EntityType.PLAYER) {

            if (damager instanceof Zombie && Objects.requireNonNull(damager.getCustomName()).equalsIgnoreCase(PalutenBoss.getInstance().getBossName())) {
                event.setDamage(2.5D);
            }

        }
    }

    @EventHandler
    public void handle(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Zombie)) {
            return;
        }

        Zombie palutenBoss = (Zombie) entity;
        String customName = palutenBoss.getCustomName();

        if (customName == null) {
            return;
        }

        String palutenBossName = PalutenBoss.getInstance().getBossName();
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