package com.finley.palutenboss.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.other.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterManager {

    public void registerMainMenu(Player player, String itemName) {
        List<String> itemMessages = List.of(
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.healthItem"),
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.effectItem"),
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.spawnItem"),
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.languageItem"),
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.configItem"),
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.teamItem"),
                PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.cleanItem")
        );

        List<String> messages = new ArrayList<>();

        for (String message : itemMessages) {
            messages.add(message.replace("&", "§"));
        }

        if (itemName.equalsIgnoreCase(messages.get(3))) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createLanguageInventory(player, "Language");
        } else if (itemName.equalsIgnoreCase(messages.get(4))) {
            PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "reloadSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase(messages.get(5))) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createWoolInventory(player, "Team Color");
        } else if (itemName.equalsIgnoreCase(messages.getLast())) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createWorldChooseInventory(player, "Choose World");
        } else if (itemName.equalsIgnoreCase(messages.get(2))) {
            closeInventory(player);
            PalutenBoss.getInstance().getEntityManager().spawnEntity(player, player.getLocation(), PalutenBoss.getInstance().getBossName(), PalutenBoss.getInstance().getLoader().getConfigBuilder().getDouble("entity.health"));
        } else if (itemName.equalsIgnoreCase(messages.get(1))) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createEffectInventory(player, "Effect");
        } else if (itemName.equalsIgnoreCase(messages.getFirst())) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createHealthInventory(player, "Health");
        }
    }

    public void registerBack(Player player, String itemName, String titleName) {
        String backName = PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem");

        if (itemName.contains(backName.replaceAll("&", "§")) || itemName.contains(ChatColor.translateAlternateColorCodes('&', backName))) {
            List<String> names = Arrays.asList("Team Color", "Language", "Effect", "Choose World", "Health");

            for (String name : names) {
                if (titleName.equalsIgnoreCase(name)) {
                    closeInventory(player);
                    PalutenBoss.getInstance().getLoader().getInvManager().createMainMenu(player, "Settings");
                }
            }
        }
    }

    private void closeInventory(Player player) {
        player.closeInventory();
    }

    private void setPath(String path, String value) {
        PalutenBoss.getInstance().getFileUtil().setConfigFilePath(path, value);
    }

    public void registerWools(Player player, String itemName) {
        List<String> noWoolNames = Arrays.asList("", ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem")));

        for (String noWoolName : noWoolNames) {
            if (itemName.equalsIgnoreCase(noWoolName)) {
                return;
            }
        }

        String woolColorName = itemName.toUpperCase()
                .replace(" WOOL", "")
                .replaceAll("§[a-zA-Z0-9]", "");

        String fixedColorName = WoolColor.getFixedColorName(woolColorName);

        PalutenBoss.getInstance().getFileUtil().setConfigFilePath("entity.teamColor", fixedColorName);
        PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "teamSuccess");

        closeInventory(player);
        changeSettings(player);
    }

    public void registerLanguages(Player player, String itemName) {
        //changeSettings(player);
        if (itemName.contains("German")) {
            changeLanguage(player, "de", false);
            changeSettings(player);
        } else if (itemName.contains("English")) {
            changeLanguage(player, "en", false);
            changeSettings(player);
        } else if (itemName.contains("Russian")) {
            changeLanguage(player, "ru", false);
            changeSettings(player);
        } else if (itemName.contains("Spanish")) {
            changeLanguage(player, "es", false);
            changeSettings(player);
        } else if (itemName.contains("Lithuanian")) {
            changeLanguage(player, "lt", false);
            changeSettings(player);
        } else if (itemName.contains("Chinese")) {
            changeLanguage(player, "zh", false);
            changeSettings(player);
        } else if (itemName.contains("Japanese")) {
            changeLanguage(player, "ja", false);
            changeSettings(player);
        } else if (itemName.contains("Turkish")) {
            changeLanguage(player, "tr", false);
            changeSettings(player);
        } else if (itemName.contains("Polnish")) {
            changeLanguage(player, "pl", false);
            changeSettings(player);
        }
    }

    public void changeLanguage(Player player, String language, boolean global) {
        String playerLanguage = PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player);

        if (global) {
            setPath("language", language);
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            changeSettings(player);
        } else {
            if (!(playerLanguage.equalsIgnoreCase(language))) {
                PalutenBoss.getInstance().getLoader().getMessageManager().getPlayersLanguages().put(player.getUniqueId().toString(), language);
                PalutenBoss.getInstance().getLoader().getPlayerLanguagesBuilder().setPath("players", PalutenBoss.getInstance().getLoader().getMessageManager().getPlayersLanguages());
            }
            changeSettings(player);
        }

        if (!(playerLanguage.equalsIgnoreCase(language))) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "alreadyLanguage");
        }

        closeInventory(player);
    }

    public boolean isValidParticle(String particleName) {
        try {
            Particle.valueOf(particleName.toUpperCase());
            return true;
        } catch (IllegalArgumentException ignored) {
            return false;
        }
    }

    public void registerEffect(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§cFire")) {
            setPath("entity.auraEffect", "FLAME");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
            changeSettings(player);
        } else if (itemName.equalsIgnoreCase("§dSmoke")) {
            setPath("entity.auraEffect", "CAMPFIRE_SIGNAL_SMOKE");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
            changeSettings(player);
        } else if (itemName.equalsIgnoreCase("§bWater")) {
            setPath("entity.auraEffect", "WATER_WAKE");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
            changeSettings(player);
        }
    }

    public void registerChoose(Player player, String itemName) {
        String backName = PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem");

        if (itemName.contains(backName.replaceAll("&", "§")) || itemName.contains(ChatColor.translateAlternateColorCodes('&', backName))) {
            return;
        }

        String cleanedItemName = itemName.replaceAll("§.", "").replaceAll("&.", "");
        PalutenBoss.getInstance().getEntityManager().cleanPalutenBosses(cleanedItemName);
        closeInventory(player);
        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "cleanSuccess");
        changeSettings(player);
    }

    public void registerHealth(Player player, String itemName) {
        try {
            String newName = itemName.replace("§l", "");
            PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("entity.health", Double.parseDouble(newName));
            closeInventory(player);
            changeSettings(player);
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "healthSuccess");
        } catch (NumberFormatException ignored) {}
    }

    public void changeSettings(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);

        String successType = PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("success.successType");

        switch (successType) {
            case "actionbar":
                PalutenBoss.getInstance().getLoader().getMessageManager().sendActionBar(player, "changedSuccess");
                break;
            case "chat":
                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changedSuccess");
                break;
            default:
                PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("success.successType", "actionbar");
                if(PalutenBoss.getInstance().isDebugMode()) {
                    Bukkit.getConsoleSender().sendMessage("§a§lSuccess §7Type §c§lnot §7found. (default set to actionbar)");
                }
                break;
        }
    }

}
