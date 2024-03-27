package com.finley.palutenboss.listener.entity;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

public class EntityTransformListener implements Listener {

    @EventHandler
    public void handle(EntityTransformEvent event) {
        Entity entity = event.getEntity();
        EntityType entityType = entity.getType();
        Location location = entity.getLocation();
        String bossName = PalutenBoss.getInstance().getBossName();
        String customName = entity.getCustomName();

        event.setCancelled(true);

        /*EntityTransformEvent.TransformReason reason = event.getTransformReason();
        EntityTransformEvent.TransformReason drownedReason = TransformReason.DROWNED;

        if (reason == drownedReason) {
            if (customName != null && customName.equals(bossName)) {
                try {
                    entity.remove();
                    EntityManager.spawnEntity(null, location, bossName, entityType);
                    EntityManager.spawnAura(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
         */
    }

}
