package com.finley.palutenboss.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builder.ItemBuilder;
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
        List<String> lore;

        if (player == null && entity == null) {
            String fakeName = "Unexpected";
            lore = Arrays.asList("", String.format("§7The Boss got killed by §e%s§7.", fakeName), String.format("§7Signed from §a%s §7on §c%s.", PalutenBoss.getInstance().getBossName(), getCurrentTimeStamp()));
        } else {
            lore = Arrays.asList("", String.format("§7The Boss got killed by §e%s§7.", player.getName()), String.format("§7Signed from §a%s §7on §c%s.", PalutenBoss.getInstance().getBossName(), getCurrentTimeStamp()));
        }

        return new ItemBuilder(Material.NETHERITE_SWORD)
                .setDisplayName(PalutenBoss.getInstance().getBossName() + " §c§lSword")
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .addEnchantment(Enchantment.SHARPNESS, 5)
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
