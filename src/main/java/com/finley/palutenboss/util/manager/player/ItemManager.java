package com.finley.palutenboss.util.manager.player;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ItemManager {

    public ItemStack createPalutenBossSword(Player player, Entity entity) {
        List<String> lore = Arrays.asList("", String.format("§7The Boss got killed by §e%s§7.", player.getName()), String.format("§7Signed from §a%s §7on §c%s.", entity.getName(), getCurrentTimeStamp()));

        return new ItemBuilder(Material.NETHERITE_SWORD)
                .setDisplayName(PalutenBoss.getInstance().getBossName() + " §c§lSword")
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                .setLore(lore)
                .build();
    }


    public ItemStack getItemStack(Player player, Entity entity) {
        return createPalutenBossSword(player, entity).clone();
    }


    public String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        Date now = new Date();
        return sdfDate.format(now);
    }


}
