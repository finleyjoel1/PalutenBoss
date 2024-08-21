package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

//ENTITY-DAMAGE LISTENER
public class EntityDAListener implements Listener {

    public EntityDAListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
    }

    @EventHandler
    public void handleDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        String customName = entity.getCustomName();
        EntityType entityType = entity.getType();
        EntityType zombieType = EntityType.ZOMBIE;

        if (customName == null) {
            return;
        }

        if (customName.equalsIgnoreCase(PalutenBoss.getInstance().getBossName()) && entity instanceof LivingEntity livingEntity) {
            if (livingEntity.getHealth() == 20) {
                boolean stompAttack = true;

                if (stompAttack) {
                    Location location = livingEntity.getLocation();
                    World world = location.getWorld();
                    Material blockType = location.getBlock().getType();
                    location.getWorld().playSound(location, Sound.ENTITY_DONKEY_ANGRY, 1F, 1F);

                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            for (int z = -1; z <= 1; z++) {
                                if (x != 0 || y != 0 || z != 0) {
                                    int targetX = location.getBlockX() + x;
                                    int targetY = location.getBlockY() + y;
                                    int targetZ = location.getBlockZ() + z;
                                    world.setBlockData(new Location(world, targetX, targetY, targetZ), blockType.createBlockData());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (entityType == zombieType && customName.equalsIgnoreCase(PalutenBoss.getInstance().getBossName())) {
            if (damager instanceof Player player) {
                if (player.getItemInHand().getItemMeta() == null) return;
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
    public void handleEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Zombie palutenBoss)) {
            return;
        }

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