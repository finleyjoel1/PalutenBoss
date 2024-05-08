package com.finley.palutenboss.other.util.other;

import org.bukkit.ChatColor;

public enum WoolColor {
    LIGHT_GRAY, GRAY, BLACK, RED, ORANGE, YELLOW, LIME, GREEN, BLUE, LIGHT_BLUE, PURPLE;

    public static boolean isValidColor(String color) {
        try {
            ChatColor.valueOf(color.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}