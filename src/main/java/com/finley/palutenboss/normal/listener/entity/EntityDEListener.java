package com.finley.palutenboss.normal.listener.entity;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.other.util.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDEListener implements Listener {

    public EntityDEListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
    }

    @EventHandler
    public void handle(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        Entity entity = event.getEntity();

        EntityType entityType = entity.getType();
        EntityType zombieType = EntityType.ZOMBIE;
        PotionEffectType potionEffectType = PotionEffectType.FIRE_RESISTANCE;

        String customName = entity.getCustomName();
        String bossName = PalutenBoss.getInstance().getBossName();

        if (entity.getType() != EntityType.ZOMBIE || customName == null) {
            return;
        }

        if (player == null) {
            ItemStack itemStack = new ItemBuilder(Material.DIRT).setDisplayName("").build();
            event.getDrops().clear();
            event.getDrops().add(itemStack);
            return;
        }

        if (entityType == zombieType) {
            if (customName.equalsIgnoreCase(bossName)) {
                try {
                    PalutenBoss.getInstance().getLoader().getItemManager().createPalutenBossSword(player, entity);
                    event.getDrops().clear();
                    event.getDrops().add(PalutenBoss.getInstance().getLoader().getItemManager().getItemStack(player, entity));
                    event.setDroppedExp(500);
                    player.addPotionEffect(new PotionEffect(potionEffectType, 30, 255, false));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
