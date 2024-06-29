package com.finley.palutenboss.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builder.ItemBuilder;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class EntityManager {

    public void spawnEntity(Player target, Location location, String name, double health) {
        World world = location.getWorld();
        String bossName = "PalutenBoss";

        if (name == null || world == null) {
            return;
        }

        LivingEntity entity = (LivingEntity) world.spawnEntity(location, EntityType.ZOMBIE);
        ItemStack bossSword = new ItemBuilder(Material.NETHERITE_SWORD).build();
        EntityEquipment entityEquipment = entity.getEquipment();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = scoreboard.getTeam(bossName);
        if (team == null) {
            team = scoreboard.registerNewTeam(bossName);
        }

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(scoreboard);
        }

        world.setDifficulty(Difficulty.NORMAL);

        ItemStack itemStack = new ItemBuilder(Material.PLAYER_HEAD).setHead("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFlZWE5MTBmZDQ3ZDc5OTQwZGFiZjI4MTkzMmFlYTU3NTcxZWQyNWJmYmQ3Nzk1YzJiY2FiZGE0MDU0In19fQ==").build();
        ItemStack chestPlate = new ItemBuilder(Material.LEATHER_CHESTPLATE).build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS).build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).build();
        ItemStack palutenSword;

        if (target == null) {
            List<? extends Player> players = Bukkit.getOnlinePlayers().stream().toList();
            Random random = new Random();
            int randomPlayer = random.nextInt(players.size());
            target = players.get(randomPlayer);
        } else {
            sendAlert(target);
            PalutenBoss.getInstance().getLoader().getMessageManager().sendTitleToPlayer(target, "alert");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(target, "spawnSuccess");
            target.playSound(target.getLocation(), Sound.EVENT_RAID_HORN, 10, 10);
        }

        palutenSword = new ItemManager().createPalutenBossSword(target, entity);

        LeatherArmorMeta chestPlateMeta = (LeatherArmorMeta) chestPlate.getItemMeta();
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

        chestPlateMeta.setColor(Color.ORANGE);
        leggingsMeta.setColor(Color.BLACK);
        bootsMeta.setColor(Color.GRAY);
        chestPlate.setItemMeta(chestPlateMeta);
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
            entityEquipment.setChestplate(chestPlate);
            entityEquipment.setLeggings(leggings);
            entityEquipment.setBoots(boots);
            entityEquipment.setItemInMainHand(palutenSword);
        }

        spawnAura(entity);
        runTimer(entity);

        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(PalutenBoss.getInstance().getLoader().getConfigBuilder().getInteger("health"));

        try {
            entity.setHealth(health);
            entity.setMaxHealth(health);
        } catch (IllegalArgumentException e) {
            System.out.println("health < 1");
            return;
        }

        team.setColor(ChatColor.valueOf(PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("teamColor")));
        team.addEntry(entity.getUniqueId().toString());

        if (entity instanceof Ageable ageEntity && (!ageEntity.isAdult())) {
            ageEntity.setAdult();
        }
        if (randomChance() == 0 && entityEquipment != null) {
            entityEquipment.setItemInHand(bossSword);
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

    private void spawnAura(Entity entity) {
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
                        world.spawnParticle(Particle.valueOf(PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("auraEffect")), particleLocation, 1, 0, 0, 0, 0);
                    }

                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(PalutenBoss.getInstance(), 2, 5);
    }

}
