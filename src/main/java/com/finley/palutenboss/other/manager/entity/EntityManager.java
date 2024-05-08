package com.finley.palutenboss.other.manager.entity;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.other.manager.player.ItemManager;
import com.finley.palutenboss.other.util.builder.ItemBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class EntityManager {
    private final Color ORANGE = Color.ORANGE;
    private final Color BLACK = Color.BLACK;
    private final Color GRAY = Color.GRAY;

    public void spawnEntity(Player target, Location location, String name, double health) {
        World world = Bukkit.getWorld(location.getWorld().getName());

        if (name == null || world == null) {
            return;
        }

        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.ZOMBIE);
        ItemStack netheriteSword = new ItemBuilder(Material.NETHERITE_SWORD).build();
        EntityEquipment entityEquipment = entity.getEquipment();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Paluten");

        if (team == null) {
            team = scoreboard.registerNewTeam("Paluten");
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(scoreboard);
        }

        location.getWorld().setDifficulty(Difficulty.NORMAL);

        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD).build();
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        itemMeta.setOwner("Paluten");
        itemStack.setItemMeta(itemMeta);

        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS).build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).build();
        ItemStack palutenSword = new ItemManager().createPalutenBossSword(target, entity);
        LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

        chestplateMeta.setColor(ORANGE);
        leggingsMeta.setColor(BLACK);
        bootsMeta.setColor(GRAY);

        chestplate.setItemMeta(chestplateMeta);
        leggings.setItemMeta(leggingsMeta);
        boots.setItemMeta(bootsMeta);

        entity.setAI(true);
        entity.setGlowing(true);
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);
        entity.setVisualFire(false);
        entity.setRemoveWhenFarAway(false);
        entity.setFireTicks(0);

        if (entityEquipment != null) {
            entityEquipment.setHelmet(itemStack);
            entityEquipment.setChestplate(chestplate);
            entityEquipment.setLeggings(leggings);
            entityEquipment.setBoots(boots);
            entityEquipment.setItemInMainHand(palutenSword);
        }

        spawnAura(entity);
        runTimer(entity);
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(PalutenBoss.getInstance().getLoader().getFileBuilder().getInteger("health"));
        entity.setHealth(health);
        entity.setMaxHealth(health);
        team.setColor(ChatColor.valueOf(PalutenBoss.getInstance().getLoader().getFileBuilder().getString("teamColor")));
        team.addEntry(entity.getUniqueId().toString());
        sendAlert(target);

        if (entity instanceof Ageable ageableEntity) {
            if (!ageableEntity.isAdult()) {
                ageableEntity.setAdult();
            }
        } else {
            return;
        }

        if (randomChance() == 0) {
            if (entityEquipment != null) {
                entityEquipment.setItemInHand(netheriteSword);
            }
        }
    }

    private int randomChance() {
        Random random = new Random();
        return random.nextInt(0, 100);
    }

    private void sendAlert(Player target) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (target != null) {
                all.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 10, 255, true));
                PalutenBoss.getInstance().getLoader().getMessageManager().sendTitleToPlayer(all, "bossSpawned");
            }
        }
    }

    public Player findNearbyPlayer(Zombie zombie) {
        Entity target = zombie.getTarget();

        if (target != null) {
            List<Entity> nearbyEntities = target.getNearbyEntities(150, 150, 150);

            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    return (Player) entity;
                }
            }
        }

        return null;
    }

    private void runTimer(Entity entity) {
        int minutes = 20 * 60 * 10;
        new BukkitRunnable() {
            @Override
            public void run() {

                if (entity != null && !entity.isDead()) {
                    entity.remove();
                }

            }
        }.runTaskLater(PalutenBoss.getInstance(), minutes);

    }

    public void cleanPalutenBosses(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            for (Entity entity : world.getEntities()) {
                if (Objects.equals(entity.getCustomName(), PalutenBoss.getInstance().getBossName())) {
                    entity.remove();
                }
            }
        }
    }

    public void spawnAura(Entity entity) {
        new BukkitRunnable() {

            @Override
            public void run() {
                if (entity.isValid()) {

                    World world = entity.getWorld();
                    Location location = entity.getLocation();

                    double radius = 1.0;
                    double yOffset = 1.0;
                    int numParticles = 36;

                    for (int i = 0; i < numParticles; i++) {
                        double angle = 2 * Math.PI * i / numParticles;
                        double x = radius * Math.cos(angle);
                        double z = radius * Math.sin(angle);
                        Location particleLocation = location.clone().add(x, yOffset, z);
                        world.spawnParticle(Particle.valueOf(PalutenBoss.getInstance().getLoader().getFileBuilder().getString("auraEffect")), particleLocation, 1, 0, 0, 0, 0);
                    }

                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(PalutenBoss.getInstance(), 2, 5);
    }


}
