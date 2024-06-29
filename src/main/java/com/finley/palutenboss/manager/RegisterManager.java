package com.finley.palutenboss.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.other.WoolColor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class RegisterManager {

    public void registerMainMenu(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§a§lLanguage")) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createLanguageInventory(player, "Language");
        } else if (itemName.equalsIgnoreCase("§b§lReload §f§lConfig")) {
            PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "reloadSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase("§d§lTeam Color")) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createTeamInventory(player, "Team Color");
        } else if (itemName.equalsIgnoreCase("§c§lClean " + PalutenBoss.getInstance().getBossName() + "es")) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createWorldChooseInventory(player, "Choose World");
        } else if (itemName.equalsIgnoreCase("§a§lSpawn " + PalutenBoss.getInstance().getBossName())) {
            closeInventory(player);
            PalutenBoss.getInstance().getEntityManager().spawnEntity(player, player.getLocation(), PalutenBoss.getInstance().getBossName(), PalutenBoss.getInstance().getLoader().getConfigBuilder().getInteger("health"));
        } else if (itemName.equalsIgnoreCase("§6§lEffect")) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createEffectInventory(player, "Effect");
        } else if (itemName.equalsIgnoreCase("§c§lHealth")) {
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getInvManager().createHealthInventory(player, "Health");
        }
    }

    public void registerBack(Player player, String itemName, String titleName) {
        if (itemName.equalsIgnoreCase("§c§lBack")) {
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
        DyeColor woolColor = getWoolColor(itemName);
        List<String> noWoolNames = Arrays.asList("§b", "§c§lBack");

        for (String noWoolName : noWoolNames) {
            if (itemName.equalsIgnoreCase(noWoolName)) {
                return;
            }
        }

        String woolColorName = itemName.toUpperCase().replace(" WOOL", "");
        String fixedColorName = WoolColor.getNewWoolColorName(woolColorName);

        PalutenBoss.getInstance().getFileUtil().setConfigFilePath("teamColor", fixedColorName);
        PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "teamSuccess");
        closeInventory(player);

        /*if (woolColor != null) {
        } else {
            System.out.println("Wolle color not found for item : " + itemName);
        }

         */
    }

    private DyeColor getWoolColor(String itemName) {
        try {
            String strippedName = ChatColor.stripColor(itemName);
            String colorName = strippedName.split(" ")[0].toUpperCase();

            return DyeColor.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void registerLanguages(Player player, String itemName) {
        if (itemName.equalsIgnoreCase(ChatColor.DARK_GREEN + "German")) {
            setPath("language", "de");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.BLUE + "English")) {
            setPath("language", "en");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.DARK_BLUE + "Russian")) {
            setPath("language", "ru");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.YELLOW + "Spanish")) {
            setPath("language", "es");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.RED + "Lithuanian")) {
            setPath("language", "lt");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Chinese")) {
            setPath("language", "zh");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.AQUA + "Japanese")) {
            setPath("language", "ja");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        } else if (itemName.equalsIgnoreCase(ChatColor.AQUA + "Turkish")) {
            setPath("language", "tr");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
        }

        closeInventory(player);
    }

    public boolean isValidParticle(String particleName) {
        try {
            Particle.valueOf(particleName.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void registerEffect(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§cFire")) {
            setPath("auraEffect", "FLAME");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase("§dSmoke")) {
            setPath("auraEffect", "CAMPFIRE_SIGNAL_SMOKE");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
        } else if (itemName.equalsIgnoreCase("§bWater")) {
            setPath("auraEffect", "WATER_WAKE");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
            closeInventory(player);
        }
    }

    public void registerChoose(Player player, String itemName) {
        if (itemName.equalsIgnoreCase("§a§lOverworld")) {
            PalutenBoss.getInstance().getEntityManager().cleanPalutenBosses("world");
        } else if (itemName.equalsIgnoreCase("§c§lNether")) {
            PalutenBoss.getInstance().getEntityManager().cleanPalutenBosses("world_nether");
        } else if (itemName.equalsIgnoreCase("§d§lEnd")) {
            PalutenBoss.getInstance().getEntityManager().cleanPalutenBosses("world_the_end");
        } else {
            return;
        }

        closeInventory(player);
        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "cleanSuccess");
    }

    public void registerHealth(Player player, String itemName) {
        try {
            String newName = itemName.replace("§f§o", "");
            PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("health", Double.valueOf(newName));
            closeInventory(player);
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "healthSuccess");
        } catch (NumberFormatException ignored) {
        }
    }

}
