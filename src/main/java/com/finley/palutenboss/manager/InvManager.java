package com.finley.palutenboss.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builder.ItemBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.*;

public class InvManager {

    private final String prefix = PalutenBoss.getInstance().getPrefix() + "§a§l";
    private final String permission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("settingsPermission");

    public void createMainMenu(Player player, String name) {
        if (player.hasPermission(permission)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().loadMessages();
            PalutenBoss.getInstance().getLoader().getMessageManager().saveMessages();

            Inventory inventory = Bukkit.createInventory(null, 45, prefix + name);
            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            List<String> shortNames = List.of("health", "effect", "spawn", "language", "config", "team", "clean");
            Map<String, ItemStack> items = new HashMap<>();

            for (String shortName : shortNames) {
                String configName = PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item." + shortName + "Item");
                String itemName = ChatColor.translateAlternateColorCodes('&', configName);

                ItemStack item = switch (shortName) {
                    case "health" -> new ItemBuilder(Material.REDSTONE).setDisplayName(itemName).build();
                    case "language" -> getLanguageItem(player, itemName);
                    case "config" -> new ItemBuilder(Material.PAPER).setDisplayName(itemName).build();
                    case "team" -> getTeamColor(itemName);
                    case "clean" -> new ItemBuilder(Material.PUMPKIN).setDisplayName(itemName).build();
                    case "spawn" -> new ItemBuilder(Material.ENDER_PEARL).setDisplayName(itemName).build();
                    case "effect" -> getEffectItem(itemName);
                    default -> new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();
                };

                items.put(shortName, item);
            }

            ItemStack bossHealth = items.get("health");
            ItemStack languageItem = items.get("language");
            ItemStack paper = items.get("config");
            ItemStack teamColor = items.get("team");
            ItemStack cleanPaluten = items.get("clean");
            ItemStack spawnBoss = items.get("spawn");
            ItemStack effect = items.get("effect");

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            inventory.setItem(0, bossHealth);
            inventory.setItem(8, effect);
            inventory.setItem(13, spawnBoss);
            inventory.setItem(21, languageItem);
            inventory.setItem(22, paper);
            inventory.setItem(23, teamColor);
            inventory.setItem(31, cleanPaluten);

            player.openInventory(inventory);
        }
    }

    public void createWorldChooseInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(null, 45, prefix + name);

            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            int count = 11;

            for (World world : Bukkit.getWorlds()) {
                String worldName = world.getName();

                if (worldName.endsWith("_nether")) {
                    worldName = "&c" + worldName;
                } else if (worldName.endsWith("the_end")) {
                    worldName = "&d" + worldName;
                } else {
                    worldName = "&a" + worldName;
                }

                String messageLore = PalutenBoss.getInstance().getLoader().getMessageManager().getMessage("gui.teleportLore", PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player));
                ItemStack worldItem = new ItemBuilder(Material.GRASS_BLOCK).setDisplayName(ChatColor.translateAlternateColorCodes('&', worldName)).setLore(List.of(ChatColor.translateAlternateColorCodes('&', messageLore.replace("%world%", worldName)))).build();

                if (count == 16) {
                    count += 4;
                } else if (count == 25) {
                    break;
                }

                inventory.setItem(count, worldItem);
                count++;
            }

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem"))).build();
            inventory.setItem(40, back);

            player.openInventory(inventory);
        }
    }

    public void createWoolInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(null, 27, prefix + name);
            List<ItemStack> woolStacks = getItemStacks();

            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            for (int i = 0; i < Math.min(woolStacks.size(), 27); i++) {
                ItemStack wool = woolStacks.get(i);
                ItemMeta itemMeta = wool.getItemMeta();
                DyeColor color = getWoolColor(wool);

                if (color == null) {
                    if (PalutenBoss.getInstance().isDebugMode()) {
                        Bukkit.getLogger().severe("Color not found.");
                    }
                    return;
                }

                String woolName = color.name().toUpperCase();

                if (woolName.contains("PURPLE")) {
                    woolName = "DARK_PURPLE";
                }

                itemMeta.setDisplayName(ChatColor.valueOf(woolName) + "§l" + woolName + " Wool");
                wool.setItemMeta(itemMeta);
                inventory.setItem(i, wool);
            }

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem"))).build();
            inventory.setItem(22, back);

            player.openInventory(inventory);
        }
    }

    private ItemStack createLanguageItem(String language, ChatColor color, String url) {
        ItemStack head = new ItemBuilder(Material.PLAYER_HEAD).build();
        if (url.isEmpty()) return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        if (!language.isEmpty()) {
            headMeta.setDisplayName(color + language);
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            Method metaSetProfileMethod = headMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            metaSetProfileMethod.setAccessible(true);
            metaSetProfileMethod.invoke(headMeta, profile);
        } catch (Exception ignored) {
        }

        head.setItemMeta(headMeta);

        return head.clone();
    }

    public void createEffectInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(null, 45, prefix + name);
            List<Material> materials = Arrays.asList(Material.FIRE_CHARGE, Material.LIGHT_BLUE_TERRACOTTA, Material.SMOKER);
            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            String backName = PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem");
            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.translateAlternateColorCodes('&', backName)).build();
            inventory.setItem(31, back);

            int count = 10;
            for (Material material : materials) {
                String materialName = material.name();
                ItemStack materialStack;
                if (materialName.equalsIgnoreCase("FIRE_CHARGE")) {
                    materialStack = new ItemBuilder(material).setDisplayName("§cFire").build();
                } else if (materialName.equalsIgnoreCase("LIGHT_BLUE_TERRACOTTA")) {
                    materialStack = new ItemBuilder(material).setDisplayName("§bWater").build();
                } else if (materialName.equalsIgnoreCase("SMOKER")) {
                    materialStack = new ItemBuilder(material).setDisplayName("§dSmoke").build();
                } else {
                    materialStack = new ItemStack(material);
                }

                inventory.setItem(count, materialStack);
                count++;
            }

            player.openInventory(inventory);
        }
    }

    private void addWoolColor(Map<Integer, Material> map, Material wool, int... positions) {
        for (int pos : positions) {
            map.put(pos, wool);
        }
    }

    public void createHealthInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(player, 45, prefix + name);

            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem"))).build();
            inventory.setItem(40, back);

            Material redWool = Material.RED_WOOL;
            Material yellowWool = Material.YELLOW_WOOL;
            Material greenWool = Material.GREEN_WOOL;
            Map<Integer, Material> woolColors = new HashMap<>();

            addWoolColor(woolColors, greenWool, 1, 25, 50, 75, 100);
            addWoolColor(woolColors, yellowWool, 125, 150, 175, 200);
            addWoolColor(woolColors, redWool, 300, 350, 400, 450, 500);

            int count = 10;
            List<Map.Entry<Integer, Material>> sortedEntries = new ArrayList<>(woolColors.entrySet());
            sortedEntries.sort(Map.Entry.comparingByKey());

            for (Map.Entry<Integer, Material> entry : sortedEntries) {
                String displayName = "§l" + entry.getKey().toString();
                ItemStack item = new ItemBuilder(entry.getValue()).setDisplayName(displayName).build();

                if (count == 17) {
                    count += 2;
                } else if (count == 26) {
                    count += 2;
                }

                inventory.setItem(count, item);
                count++;
            }

            player.openInventory(inventory);
        }
    }

    public void createLanguageInventory(Player player, String titleName) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(null, 45, prefix + titleName);
            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            inventory.setItem(10, createLanguageItem("German", ChatColor.DARK_GREEN, getLanguageUrl("de")));
            inventory.setItem(11, createLanguageItem("English", ChatColor.BLUE, getLanguageUrl("en")));
            inventory.setItem(12, createLanguageItem("Russian", ChatColor.GREEN, getLanguageUrl("ru")));
            inventory.setItem(13, createLanguageItem("Spanish", ChatColor.YELLOW, getLanguageUrl("es")));
            inventory.setItem(14, createLanguageItem("Lithuanian", ChatColor.RED, getLanguageUrl("lt")));
            inventory.setItem(15, createLanguageItem("Chinese", ChatColor.LIGHT_PURPLE, getLanguageUrl("zh")));
            inventory.setItem(16, createLanguageItem("Japanese", ChatColor.AQUA, getLanguageUrl("ja")));
            inventory.setItem(19, createLanguageItem("Turkish", ChatColor.AQUA, getLanguageUrl("tr")));
            inventory.setItem(20, createLanguageItem("Polnish", ChatColor.GREEN, getLanguageUrl("pl")));

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.translateAlternateColorCodes('&', PalutenBoss.getInstance().getLoader().getMessageBuilder().getString(PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player) + ".gui.item.backItem"))).build();
            inventory.setItem(31, back);

            player.openInventory(inventory);
        }
    }

    private ItemStack getTeamColor(String displayName) {
        String teamColor = PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("entity.teamColor");

        ItemStack itemStack = switch (teamColor) {
            case "GRAY" -> new ItemBuilder(Material.GRAY_WOOL).setDisplayName(ChatColor.GRAY + "GRAY Wool").build();
            case "BLACK" -> new ItemBuilder(Material.BLACK_WOOL).setDisplayName(ChatColor.BLACK + "BLACK Wool").build();
            case "RED" -> new ItemBuilder(Material.RED_WOOL).setDisplayName(ChatColor.RED + "RED Wool").build();
            case "GOLD" ->
                    new ItemBuilder(Material.YELLOW_WOOL).setDisplayName(ChatColor.YELLOW + "YELLOW Wool").build();
            case "GREEN" -> new ItemBuilder(Material.GREEN_WOOL).setDisplayName(ChatColor.GREEN + "GREEN Wool").build();
            case "DARK_BLUE" ->
                    new ItemBuilder(Material.BLUE_WOOL).setDisplayName(ChatColor.BLUE + "BLUE Wool").build();
            case "DARK_PURPLE" ->
                    new ItemBuilder(Material.PURPLE_WOOL).setDisplayName(ChatColor.LIGHT_PURPLE + "PURPLE Wool").build();
            default -> null;
        };

        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    private ItemStack getEffectItem(String displayName) {
        String effect = PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("entity.auraEffect");

        ItemStack itemStack = switch (effect) {
            case "CAMPFIRE_SIGNAL_SMOKE" -> new ItemStack(Material.SMOKER);
            case "FLAME" -> new ItemStack(Material.FIRE_CHARGE);
            case "WATER_WAKE" -> new ItemStack(Material.LIGHT_BLUE_TERRACOTTA);
            default -> null;
        };

        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    private ItemStack getLanguageItem(Player player, String displayName) {
        String playerLanguage = PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player);
        String url = getLanguageUrl(playerLanguage);

        ItemStack itemStack = createLanguageItem(playerLanguage, ChatColor.GREEN, url);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private String getLanguageUrl(String language) {
        String url;

        switch (language) {
            case "de":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWU3ODk5YjQ4MDY4NTg2OTdlMjgzZjA4NGQ5MTczZmU0ODc4ODY0NTM3NzQ2MjZiMjRiZDhjZmVjYzc3YjNmIn19fQ==";
                return url;
            case "en":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVlNWM4NTBhZmJiN2Q4ODQzMjY1YTE0NjIxMWFjOWM2MTVmNzMzZGNjNWE4ZTIxOTBlNWMyNDdkZWEzMiJ9fX0=";
                return url;
            case "ru":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTZlYWZlZjk4MGQ2MTE3ZGFiZTg5ODJhYzRiNDUwOTg4N2UyYzQ2MjFmNmE4ZmU1YzliNzM1YTgzZDc3NWFkIn19fQ==";
                return url;
            case "es":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJkNzMwYjZkZGExNmI1ODQ3ODNiNjNkMDgyYTgwMDQ5YjVmYTcwMjI4YWJhNGFlODg0YzJjMWZjMGMzYThiYyJ9fX0=";
                return url;
            case "lt":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUzZDgyOTc0MTk3NmQ4MzMwZDY5NDdkNmVlYTY0ZWU1N2FlZDJmMjYyODZmZjYzODQ0ZTkwODkzNjdjMTEifX19";
                return url;
            case "zh":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y5YmMwMzVjZGM4MGYxYWI1ZTExOThmMjlmM2FkM2ZkZDJiNDJkOWE2OWFlYjY0ZGU5OTA2ODE4MDBiOThkYyJ9fX0=";
                return url;
            case "ja":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDZjMmNhNzIzODY2NmFlMWI5ZGQ5ZGFhM2Q0ZmM4MjlkYjIyNjA5ZmI1NjkzMTJkZWMxZmIwYzhkNmRkNmMxZCJ9fX0=";
                return url;
            case "tr":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg1MmI5YWJhMzQ4MjM0ODUxNGMxMDM0ZDBhZmZlNzM1NDVjOWRlNjc5YWU0NjQ3Zjk5NTYyYjVlNWY0N2QwOSJ9fX0";
                return url;
            case "pl":
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTIxYjJhZjhkMjMyMjI4MmZjZTRhMWFhNGYyNTdhNTJiNjhlMjdlYjMzNGY0YTE4MWZkOTc2YmFlNmQ4ZWIifX19";
                return url;
            default:
                url = "";
        }
        return url;
    }

    private DyeColor getWoolColor(ItemStack itemStack) {
        if (itemStack != null) {
            Material material = itemStack.getType();
            if (material.name().endsWith("_WOOL")) {
                String colorName = material.name().replace("_WOOL", "");
                return DyeColor.valueOf(colorName);
            }
        }
        return null;
    }

    private List<ItemStack> getItemStacks() {
        return Collections.unmodifiableList(Arrays.asList(
                createColoredWool(DyeColor.GRAY),
                createColoredWool(DyeColor.RED),
                createColoredWool(DyeColor.YELLOW),
                createColoredWool(DyeColor.GREEN),
                createColoredWool(DyeColor.BLUE),
                createColoredWool(DyeColor.PURPLE)
        ));
    }

    private ItemStack createColoredWool(DyeColor color) {
        return new ItemBuilder(Material.valueOf(color.name() + "_WOOL"))
                .setDisplayName(color.name() + " Wool")
                .build();
    }
}