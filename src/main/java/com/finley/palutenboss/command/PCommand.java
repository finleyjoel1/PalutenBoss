package com.finley.palutenboss.command;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.other.WoolColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PCommand implements CommandExecutor, TabCompleter {

    private final String spawnPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("spawnPermission");
    private final String reloadPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("reloadPermission");
    private final String settingsPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("settingsPermission");
    private final String languagePermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("languagePermission");
    private final String cleanPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("cleanPermission");
    private final String colorPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("colorPermission");
    private final String effectPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("effectPermission");
    private final String healthPermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("healthPermission");
    private final String defaultLanguagePermission = PalutenBoss.getInstance().getLoader().getPermissionFile().getString("defaultLanguagePermission");

    private final List<String> argsNullList = List.of("spawn", "reload", "clear", "opengui", "settings");
    private final List<String> argsTwoList = List.of("language", "color", "effect", "health", "motd", "showhealth", "defaultlanguage");
    private final List<String> colorList = List.of("gray", "red", "yellow", "green", "blue", "purple");
    private final List<String> languageList = List.of("de", "en", "ru", "es", "lt", "zh", "ja", "pl");
    private final List<String> effectsList = List.of("flame", "water_wake", "campfire_signal_smoke");
    private final List<String> healthList = List.of("25", "50", "75", "100", "125", "150", "175", "200");
    private final List<String> booleanList = List.of("true", "false");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (PalutenBoss.getInstance().isDebugMode()) {
            Bukkit.getLogger().info("args length: " + args.length);
        }

        if (spawnPermission == null || reloadPermission == null || settingsPermission == null || languagePermission == null || cleanPermission == null || colorPermission == null || effectPermission == null || healthPermission == null) {
            return true;
        }

        if (!(sender instanceof Player player)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendNoPlayerMessage();
            return true;
        }

        if (!player.hasPermission(spawnPermission) || !player.hasPermission(settingsPermission)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendNoPermissionMessage(player);
            return true;
        }

        String bossName = PalutenBoss.getInstance().getBossName();

        if (args.length < 1) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§8--------------" + PalutenBoss.getInstance().getPrefixName() + "§8---------------");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7opengui§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7spawn§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7settings§8> §8<§7language§8/§7effect§8/§7color§8/§7health§8/§7motd§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7clean§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7reload§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§8-----------------------------------------");
            return true;
        }


        switch (args[0]) {
            case "reload":
                if (player.hasPermission(reloadPermission)) {
                    PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
                    PalutenBoss.getInstance().getFileUtil().setLanguage();
                    PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();
                    PalutenBoss.getInstance().getLoader().getPermissionFile().reload();
                    PalutenBoss.getInstance().getLoader().getPlayerLanguagesBuilder().reload();
                    PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "reloadSuccess");
                    return true;
                }
                break;
            case "clear":
                if (player.hasPermission(cleanPermission)) {
                    PalutenBoss.getInstance().getLoader().getInvManager().createWorldChooseInventory(player, "Choose World");
                    return true;
                }
                break;
            case "opengui":
                if (player.hasPermission(settingsPermission)) {
                    PalutenBoss.getInstance().getLoader().getInvManager().createMainMenu(player, "Settings");
                    return true;
                }
                break;
            case "spawn":
                if (player.hasPermission(spawnPermission)) {
                    Location location;

                    if (args.length < 2) {
                        location = player.getLocation();
                    } else {
                        if (args.length < 4) {
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "argumentError");
                            return true;
                        }
                        double xPos = Double.parseDouble(args[1]);
                        double yPos = Double.parseDouble(args[2]);
                        double zPos = Double.parseDouble(args[3]);
                        location = new Location(player.getWorld(), xPos, yPos, zPos);
                    }

                    PalutenBoss.getInstance().getEntityManager().spawnEntity(player, location, bossName, PalutenBoss.getInstance().getLoader().getConfigBuilder().getDouble("entity.health"));
                    return true;
                }
                break;
            case "settings":
                if (args.length < 3) {
                    PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "argumentError");
                    return true;
                } else if (args.length > 3) {
                    return true;
                }
                switch (args[1]) {
                    case "color":
                        if (player.hasPermission(colorPermission)) {
                            if (WoolColor.isValidColor(args[2].toUpperCase())) {
                                String fixedColorName = WoolColor.getFixedColorName(args[2].toUpperCase());
                                if (PalutenBoss.getInstance().isDebugMode()) {
                                    PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, fixedColorName);
                                }
                                PalutenBoss.getInstance().getFileUtil().setConfigFilePath("entity.teamColor", fixedColorName);
                                PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "teamSuccess");
                                return true;
                            } else {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            }

                            return false;
                        }
                        break;
                    case "health":
                        if (player.hasPermission(healthPermission)) {
                            try {
                                if (Double.parseDouble(args[2]) > 2048 || Double.parseDouble(args[2]) < 1) {
                                    return true;
                                }
                                PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("entity.health", Double.parseDouble(args[2]));
                            } catch (NumberFormatException e) {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§c" + e);
                                return true;
                            }
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "healthSuccess");
                        }
                        break;
                    case "motd":
                        boolean motd = Boolean.parseBoolean(args[2]);
                        try {
                            PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("motd", motd);
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "motdSuccess");
                        } catch (IllegalArgumentException e) {
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§c" + e);
                        }
                        break;
                    case "showhealth":
                        boolean showHealth = Boolean.parseBoolean(args[2]);
                        try {
                            PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("entity.showHealth", showHealth);
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "showHealthSuccess");
                        } catch (IllegalArgumentException e) {
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§c" + e);
                        }
                        break;
                    case "effect":
                        if (player.hasPermission(effectPermission)) {
                            if (PalutenBoss.getInstance().getLoader().getRegisterManager().isValidParticle(args[2].toUpperCase())) {
                                PalutenBoss.getInstance().getFileUtil().setConfigFilePath("entity.auraEffect", args[2].toUpperCase());
                                PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
                            } else {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            }

                            return true;
                        }
                        break;
                    case "language":
                        if (player.hasPermission(languagePermission)) {
                            String playerLanguage = PalutenBoss.getInstance().getLoader().getMessageManager().getLanguageFromPlayer(player);

                            if (args[2].equalsIgnoreCase(playerLanguage)) {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "alreadyLanguage");
                                return true;
                            }

                            if (PalutenBoss.getInstance().getLoader().getMessageManager().isValidLanguage(args[2])) {
                                PalutenBoss.getInstance().getLoader().getMessageManager().getPlayersLanguages().put(player.getUniqueId().toString(), args[2]);
                                PalutenBoss.getInstance().getLoader().getPlayerLanguagesBuilder().setPath("players", PalutenBoss.getInstance().getLoader().getMessageManager().getPlayersLanguages());
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
                            } else {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            }
                            return true;
                        }
                        break;
                    case "defaultlanguage":
                        if (player.hasPermission(defaultLanguagePermission)) {
                            if (args[2].equalsIgnoreCase(PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("language"))) {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "alreadyLanguage");
                                return true;
                            }

                            if (PalutenBoss.getInstance().getLoader().getMessageManager().isValidLanguage(args[2])) {
                                PalutenBoss.getInstance().getFileUtil().setConfigFilePath("language", args[2]);
                                PalutenBoss.getInstance().getFileUtil().setLanguage();
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "changeLanguage");
                            } else {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            }
                            return true;
                        }
                        break;
                }
        }
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();
        Location location = player.getLocation();

        String xPos = String.valueOf(Math.round(location.getX()));
        String yPos = String.valueOf(Math.round(location.getY()));
        String zPos = String.valueOf(Math.round(location.getZ()));

        switch (args.length) {
            case 1:
                completions.addAll(argsNullList);
                if (args[0].equals("clear")) {
                    for (World world : Bukkit.getWorlds()) {
                        completions.add(world.getName());
                    }
                }
                break;
            case 2:
                if (args[0].equals("settings")) {
                    if (player.hasPermission(settingsPermission)) {
                        completions.addAll(argsTwoList);
                    }
                }
                if (args[0].equals("spawn")) {
                    completions.add(xPos);
                }
                break;
            case 3:
                if (args[0].equals("spawn")) {
                    completions.add(yPos);
                }
                if (player.hasPermission(settingsPermission)) {
                    switch (args[1].toLowerCase()) {
                        case "effect":
                            completions.addAll(effectsList);
                            break;
                        case "language", "defaultlanguage":
                            completions.addAll(languageList);
                            break;
                        case "color":
                            completions.addAll(colorList);
                            break;
                        case "health":
                            completions.addAll(healthList);
                            break;
                        case "motd", "showhealth":
                            completions.addAll(booleanList);
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 4:
                if (args[0].equals("spawn")) {
                    completions.add(zPos);
                }
                break;
        }
        if (!completions.isEmpty()) {
            String currentArg = args[args.length - 1].toLowerCase();
            completions.removeIf(c -> !c.toLowerCase().startsWith(currentArg));
        }
        return completions;
    }
}