package com.finley.palutenboss.listener;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        String bossName = ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getBossName());

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
                } catch (IllegalArgumentException e) {
                    System.out.println(" ");
                    System.out.println("Item not found!");
                    System.out.println(e.getMessage());
                    return;
                }
            }
            return;
        }

        if (entityType == zombieType) {
            if (customName.equalsIgnoreCase(bossName)) {
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
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "Item == null");
                            return;
                        }
                    }

                    event.setDroppedExp(500);
                    player.addPotionEffect(new PotionEffect(potionEffectType, 30, 255, false));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
