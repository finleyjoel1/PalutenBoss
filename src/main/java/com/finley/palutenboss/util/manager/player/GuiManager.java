package com.finley.palutenboss.util.manager.player;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.builders.ItemBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GuiManager {

    private static final String prefix = PalutenBoss.getInstance().getPrefix() + "§a§l";
    private static final String permission = PalutenBoss.getInstance().getLoader().getPermission().getString("settingsPermission");

    public void createInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(null, 45, prefix + name);

            ItemStack bossHealth = new ItemBuilder(Material.REDSTONE).setDisplayName("§c§lHealth").build();
            ItemStack languageItem = getLanguageItem();
            ItemStack paper = new ItemBuilder(Material.PAPER).setDisplayName("§b§lReload §f§lConfig").build();
            ItemStack teamColor = getTeamColor();
            ItemStack cleanPaluten = new ItemBuilder(Material.PUMPKIN).setDisplayName("§c§lClean " + PalutenBoss.getInstance().getBossName() + "es").build();
            ItemStack spawnBoss = new ItemBuilder(Material.ENDER_PEARL).setDisplayName("§a§lSpawn " + PalutenBoss.getInstance().getBossName()).build();
            ItemStack effect = getEffectItem();

            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

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

            ItemStack overWorld = new ItemBuilder(Material.GRASS_BLOCK).setDisplayName("§a§lOverworld").build();
            ItemStack nether = new ItemBuilder(Material.NETHERRACK).setDisplayName("§c§lNether").build();
            ItemStack end = new ItemBuilder(Material.END_PORTAL_FRAME).setDisplayName("§d§lEnd").build();

            inventory.setItem(12, overWorld);
            inventory.setItem(13, nether);
            inventory.setItem(14, end);

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lBack").build();
            inventory.setItem(31, back);

            player.openInventory(inventory);
        }
    }

    public void createTeamInventory(Player player, String name) {
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

                if (color != null) {
                    itemMeta.setDisplayName(ChatColor.valueOf(color.name()) + " Wool");
                }

                wool.setItemMeta(itemMeta);
                inventory.setItem(i, wool);
            }

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lBack").build();
            inventory.setItem(22, back);

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
            inventory.setItem(12, createLanguageItem("Russian", ChatColor.DARK_BLUE, getLanguageUrl("ru")));
            inventory.setItem(13, createLanguageItem("Spanish", ChatColor.YELLOW, getLanguageUrl("es")));
            inventory.setItem(14, createLanguageItem("Lithuanian", ChatColor.RED, getLanguageUrl("lt")));
            inventory.setItem(15, createLanguageItem("Chinese", ChatColor.LIGHT_PURPLE, getLanguageUrl("zh")));
            inventory.setItem(16, createLanguageItem("Japanese", ChatColor.AQUA, getLanguageUrl("ja")));
            inventory.setItem(19, createLanguageItem("Turkish", ChatColor.AQUA, getLanguageUrl("tr")));

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lBack").build();
            inventory.setItem(31, back);

            player.openInventory(inventory);
        }
    }

    public ItemStack getTeamColor() {
        String teamColor = PalutenBoss.getInstance().getLoader().getFileBuilder().getString("teamColor");

        ItemStack itemStack = null;

        switch (teamColor) {
            case "GRAY":
                itemStack = new ItemBuilder(Material.GRAY_WOOL).setDisplayName(ChatColor.GRAY + "GRAY Wool").build();
                break;
            case "BLACK":
                itemStack = new ItemBuilder(Material.BLACK_WOOL).setDisplayName(ChatColor.BLACK + "BLACK Wool").build();
                break;
            case "RED":
                itemStack = new ItemBuilder(Material.RED_WOOL).setDisplayName(ChatColor.RED + "RED Wool").build();
                break;
            case "GOLD":
                itemStack = new ItemBuilder(Material.YELLOW_WOOL).setDisplayName(ChatColor.YELLOW + "YELLOW Wool").build();
                break;
            case "GREEN":
                itemStack = new ItemBuilder(Material.GREEN_WOOL).setDisplayName(ChatColor.GREEN + "GREEN Wool").build();
                break;
            case "DARK_BLUE":
                itemStack = new ItemBuilder(Material.BLUE_WOOL).setDisplayName(ChatColor.BLUE + "BLUE Wool").build();
                break;
            case "DARK_PURPLE":
                itemStack = new ItemBuilder(Material.PURPLE_WOOL).setDisplayName(ChatColor.LIGHT_PURPLE + "PURPLE Wool").build();
                break;
        }

        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§d§lTeam Color");
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public ItemStack getEffectItem() {
        String effect = PalutenBoss.getInstance().getLoader().getFileBuilder().getString("auraEffect");
        ItemStack itemStack = null;

        switch (effect) {
            case "CAMPFIRE_SIGNAL_SMOKE":
                itemStack = new ItemStack(Material.SMOKER);
                break;
            case "FLAME":
                itemStack = new ItemStack(Material.FIRE_CHARGE);
                break;
            case "WATER_WAKE":
                itemStack = new ItemStack(Material.LIGHT_BLUE_TERRACOTTA);
                break;
        }

        if (itemStack != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("§6§lEffect");
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    public ItemStack getLanguageItem() {
        String language = PalutenBoss.getInstance().getLoader().getFileBuilder().getString("language");
        String url = getLanguageUrl(language);

        ItemStack itemStack = createLanguageItem(language, ChatColor.GREEN, url);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§a§lLanguage");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private String getLanguageUrl(String language) {
        String url = null;
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
                url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUzZDgyOTc0MTk3NmQ4MzMwZDY5NDdkNmVlYTY0ZWU1N2FlZDJmMjYyODZmZjYzODQ0ZTkwODkzNjdjMTEifX19=";
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
            default:
                url = "";
        }
        ;
        return url;
    }

    public ItemStack createLanguageItem(String language, ChatColor color, String url) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);

        return head.clone();
    }


    public DyeColor getWoolColor(ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() == Material.WHITE_WOOL && itemStack.getData() != null) {
            return DyeColor.getByWoolData(itemStack.getData().getData());
        }
        return null;
    }

    public List<ItemStack> getItemStacks() {
        return Collections.unmodifiableList(Arrays.asList(
                createColoredWool(DyeColor.GRAY),
                createColoredWool(DyeColor.BLACK),
                createColoredWool(DyeColor.RED),
                createColoredWool(DyeColor.YELLOW),
                createColoredWool(DyeColor.GREEN),
                createColoredWool(DyeColor.BLUE),
                createColoredWool(DyeColor.PURPLE)
        ));
    }

    private ItemStack createColoredWool(DyeColor color) {
        ChatColor chatColor = getChatColor(color);
        return new ItemBuilder(Material.valueOf(color.name() + "_WOOL"))
                .setDisplayName(chatColor + color.name() + " Wool")
                .build();
    }

    public void createEffectInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(null, 45, prefix + name);

            List<Material> materials = Arrays.asList(Material.FIRE_CHARGE, Material.LIGHT_BLUE_TERRACOTTA, Material.SMOKER);

            ItemStack glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§b").build();

            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, glassPane);
            }

            ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Back").build();
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

    public ChatColor getChatColor(DyeColor color) {
        switch (color) {
            case GRAY:
                return ChatColor.GRAY;
            case BLACK:
                return ChatColor.BLACK;
            case RED:
                return ChatColor.RED;
            case YELLOW:
                return ChatColor.GOLD;
            case GREEN:
                return ChatColor.GREEN;
            case BLUE:
                return ChatColor.DARK_BLUE;
            case PURPLE:
                return ChatColor.DARK_PURPLE;
            default:
                return ChatColor.RESET;
        }
    }


    public void createHealthInventory(Player player, String name) {
        if (player.hasPermission(permission)) {
            Inventory inventory = Bukkit.createInventory(player, InventoryType.ANVIL, prefix + name);

            ItemStack inputItem = new ItemBuilder(Material.NAME_TAG)
                    .setDisplayName("first item")
                    .build();

            inventory.setItem(0, inputItem);

            player.openInventory(inventory);
        }
    }
}
