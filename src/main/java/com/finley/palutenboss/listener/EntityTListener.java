package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTransformEvent;

//ENTITY-TRANSFORM LISTENER
public class EntityTListener implements Listener {

    public EntityTListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
    }

    @EventHandler
    public void handle(EntityTransformEvent event) {
        Entity entity = event.getEntity();
        Entity transformedEntity = event.getTransformedEntity();

        EntityTransformEvent.TransformReason reason = event.getTransformReason();
        EntityTransformEvent.TransformReason drownedReason = EntityTransformEvent.TransformReason.DROWNED;

        if (reason == drownedReason) {
            if (entity.getCustomName() != null && entity.getCustomName().equalsIgnoreCase(PalutenBoss.getInstance().getBossName())) {
                event.setCancelled(true);
                transformedEntity.remove();
                entity.remove();
            }
        }
    }

}