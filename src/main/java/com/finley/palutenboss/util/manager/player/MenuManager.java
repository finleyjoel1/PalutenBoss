package com.finley.palutenboss.util.manager.player;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.manager.entity.EntityManager;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MenuManager {

    public static void registerMainMenu(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§a§lLanguage")) {
            closeInventory(player);
            GuiManager.createLanguageInventory(player, "Language");
        } else if (itemName.equalsIgnoreCase("§b§lReload §f§lConfig")) {
            PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessages().reload();
            MessageManager.sendMessageToPlayer(player, "reloadSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase("§d§lTeam Color")) {
            closeInventory(player);
            GuiManager.createTeamInventory(player, "Team Color");
        } else if (itemName.equalsIgnoreCase("§c§lClean " + PalutenBoss.getInstance().getBossName() + "es")) {
            closeInventory(player);
            GuiManager.createWorldChooseInventory(player, "Choose World");
        } else if (itemName.equalsIgnoreCase("§a§lSpawn " + PalutenBoss.getInstance().getBossName())) {
            closeInventory(player);
            EntityManager.spawnEntity(player, player.getLocation(), PalutenBoss.getInstance().getBossName());
            MessageManager.sendMessageToPlayer(player, "spawnSuccess");
        } else if (itemName.equalsIgnoreCase("§6§lEffect")) {
            closeInventory(player);
            GuiManager.createEffectInventory(player, "Effect");
        }else if (itemName.equalsIgnoreCase("§c§lHealth")) {
            closeInventory(player);
            GuiManager.createHealthInventory(player, "Health");
        }

    }

    public static void registerBack(Player player, String itemName, String titleName) {
        if (itemName.equalsIgnoreCase("§c§lBack")) {
            List<String> names = Arrays.asList("Team Color", "Language", "Effect", "Choose World");

            for (String name : names) {
                if (titleName.equalsIgnoreCase(name)) {
                    closeInventory(player);
                    GuiManager.createInventory(player, "Settings");
                }
            }
        }
    }

    private static void closeInventory(Player player) {
        player.closeInventory();
    }

    private static void setPath(String path, String value) {
        PalutenBoss.getInstance().getFileManager().setConfigFilePath(path, value);
    }

    public static void registerWools(Player player, String itemName) {

        DyeColor woolColor = getWoolColor(itemName);

        List<String> noWoolNames = Arrays.asList("§b", "§c§lBack");

        for (String noWoolName : noWoolNames) {
            if (itemName.equalsIgnoreCase(noWoolName)) {
                return;
            }
        }

        if (woolColor != null) {
            ChatColor chatColor = GuiManager.getChatColor(woolColor);
            setPath("teamColor", chatColor.name());
            MessageManager.sendMessageToPlayer(player, "teamSuccess");
            closeInventory(player);
        } else {
            System.out.println("Wolle color not found for item : " + itemName);
        }
    }

    private static DyeColor getWoolColor(String itemName) {
        try {
            String strippedName = ChatColor.stripColor(itemName);
            String colorName = strippedName.split(" ")[0].toUpperCase();

            return DyeColor.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    public static void registerLanguages(Player player, String itemName) {
        if (itemName.equalsIgnoreCase(ChatColor.DARK_GREEN + "German")) {
            setPath("language", "de");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.BLUE + "English")) {
            setPath("language", "en");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.DARK_BLUE + "Russian")) {
            setPath("language", "ru");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.YELLOW + "Spanish")) {
            setPath("language", "es");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.RED + "Lithuanian")) {
            setPath("language", "lt");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Chinese")) {
            setPath("language", "zh");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.AQUA + "Japanese")) {
            setPath("language", "ja");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.AQUA + "Turkish")) {
            setPath("language", "tr");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            MessageManager.sendMessageToPlayer(player, "changeLanguage");
        }

        closeInventory(player);

    }

    public static boolean isValidParticle(String particleName) {
        try {
            Particle.valueOf(particleName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static void registerEffect(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§cFire")) {
            setPath("auraEffect", "FLAME");
            MessageManager.sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase("§dSmoke")) {
            setPath("auraEffect", "CAMPFIRE_SIGNAL_SMOKE");
            MessageManager.sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase("§bWater")) {
            setPath("auraEffect", "WATER_WAKE");
            MessageManager.sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
        }
    }

    public static void registerChoose(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§a§lOverworld")) {
            EntityManager.cleanPalutenBosses("world");
        } else if (itemName.equalsIgnoreCase("§c§lNether")) {
            EntityManager.cleanPalutenBosses("world_nether");
        } else if (itemName.equalsIgnoreCase("§d§lEnd")) {
            EntityManager.cleanPalutenBosses("world_the_end");
        } else {
            return;
        }

        closeInventory(player);
        MessageManager.sendMessageToPlayer(player, "cleanSuccess");
    }

    public static void registerHealth(Player player, String itemName) {
        player.sendMessage(itemName);
    }

}
