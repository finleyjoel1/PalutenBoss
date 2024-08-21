package com.finley.palutenboss.util.other;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipeUtil {

    public void registerRecipe() {
        NamespacedKey key = new NamespacedKey(PalutenBoss.getInstance(), "palutenboss_sword");
        ItemStack pumpkinSword = new ItemStack(PalutenBoss.getInstance().getLoader().getItemManager().getItemStack(null, null));
        ShapedRecipe recipe = new ShapedRecipe(key, pumpkinSword);

        recipe.shape("###", "#*#", "###");
        recipe.setIngredient('#', Material.PUMPKIN);
        recipe.setIngredient('*', Material.NETHERITE_SWORD);

        Bukkit.getServer().addRecipe(recipe);
    }
}
