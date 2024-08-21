package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builder.ItemBuilder;
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

//DEATH LISTENER
public class EntityDEListener implements Listener {

    public EntityDEListener() {
        Bukkit.getPluginManager().registerEvents(this, PalutenBoss.getInstance());
    }

    @EventHandler
    public void handle(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        Entity entity = event.getEntity();
        PotionEffectType potionEffectType = PotionEffectType.FIRE_RESISTANCE;

        EntityType entityType = entity.getType();
        EntityType zombieType = EntityType.ZOMBIE;

        String customName = entity.getCustomName();
        String bossName = PalutenBoss.getInstance().getBossName();

        if (entity.getType() != EntityType.ZOMBIE || customName == null) {
            return;
        }

        if (player == null) {
            event.getDrops().clear();
            if (PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("drops.dropItemIfPlayerNotExist").equalsIgnoreCase("palutenboss_sword")) {
                event.getDrops().add(PalutenBoss.getInstance().getLoader().getItemManager().getItemStack(null, null));
            } else {
                try {
                    Material material = Material.valueOf(PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("drops.dropItemIfPlayerNotExist").toUpperCase());
                    ItemStack itemStack = new ItemBuilder(material).setDisplayName("").build();
                    event.getDrops().add(itemStack);
                } catch (IllegalArgumentException ignored) {
                    if (PalutenBoss.getInstance().isDebugMode()) {
                        Bukkit.getLogger().severe("item was not found (invalid argument)");
                    }
                    return;
                }
            }
            return;
        }

        if (entityType == zombieType) {
            if (customName.contains(bossName)) {
                try {
                    PalutenBoss.getInstance().getLoader().getItemManager().createPalutenBossSword(player, entity);
                    event.getDrops().clear();

                    if (PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("drops.dropItemIfPlayerExist").equalsIgnoreCase("palutenboss_sword")) {
                        event.getDrops().add(PalutenBoss.getInstance().getLoader().getItemManager().getItemStack(player, entity));
                    } else {
                        try {
                            Material material = Material.valueOf(PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("drops.dropItemIfPlayerExist").toUpperCase());
                            ItemStack itemStack = new ItemBuilder(material).setDisplayName("").build();
                            event.getDrops().add(itemStack);
                        } catch (IllegalArgumentException ignored) {
                            if (PalutenBoss.getInstance().isDebugMode()) {
                                Bukkit.getLogger().severe("item is null");
                            }
                            return;
                        }
                    }

                    event.setDroppedExp(500);
                    player.addPotionEffect(new PotionEffect(potionEffectType, 30, 255, false));

                } catch (Exception ignored) {
                }
            }
        }
    }
}
