package com.finley.palutenboss.util.other;

import org.bukkit.ChatColor;

public class WoolColor {

    public static boolean isValidColor(String color) {
        try {
            ChatColor.valueOf(color.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String getNewWoolColorName(String input) {
        return switch (input) {
            case "BLUE" -> "DARK_BLUE";
            case "PURPLE" -> "DARK_PURPLE";
            case "YELLOW" -> "GOLD";
            default -> input;
        };
    }
}