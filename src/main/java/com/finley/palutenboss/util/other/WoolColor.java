package com.finley.palutenboss.util.other;

import com.finley.palutenboss.PalutenBoss;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class WoolColor {

    public static boolean isValidColor(String color) {
        try {
            ChatColor.valueOf(color.toUpperCase());
            return true;
        } catch (IllegalArgumentException ignored) {
            if (PalutenBoss.getInstance().isDebugMode()) {
                Bukkit.getLogger().severe("color not found (invalid argument)");
            }
            return false;
        }
    }

    public static String getFixedColorName(String input) {
        return switch (input) {
            case "BLUE" -> "DARK_BLUE";
            case "PURPLE" -> "DARK_PURPLE";
            case "YELLOW" -> "GOLD";
            default -> input;
        };
    }
}