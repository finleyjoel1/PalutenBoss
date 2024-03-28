package com.finley.palutenboss.util.manager.player;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeManager {

    public void registerRecipe() {

        NamespacedKey key = new NamespacedKey(PalutenBoss.getInstance(), "pumpkin_sword");

        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack pumpkinSword = new ItemStack(PalutenBoss.getInstance().getLoader().getItemManager().getItemStack(player, player));
            ShapedRecipe recipe = new ShapedRecipe(key, pumpkinSword);

            recipe.shape(" # ", " # ", " * ");
            recipe.setIngredient('#', Material.PUMPKIN);
            recipe.setIngredient('*', Material.STICK);
            try {
                Bukkit.getServer().addRecipe(recipe);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }

    }
}
