package com.finley.palutenboss.command;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.other.WoolColor;
import org.bukkit.Location;
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

    private final List<String> argsNullList = List.of("spawn", "reload", "clear", "opengui", "settings");
    private final List<String> argsTwoList = List.of("language", "color", "effect", "health", "motd");
    private final List<String> colorList = List.of("gray", "black", "red", "yellow", "green", "blue", "purple");
    private final List<String> languageList = List.of("de", "en", "ru", "es", "lt", "zh", "ja");
    private final List<String> effectsList = List.of("flame", "water_wake", "campfire_signal_smoke");
    private final List<String> healthList = List.of("25", "50", "75", "100", "125", "150", "175", "200");
    private final List<String> booleanList = List.of("true", "false");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendNoPlayerMessage();
            return true;
        }

        if (!player.hasPermission(spawnPermission) || !player.hasPermission(settingsPermission)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendNoPermissionMessage(player);
            return true;
        }

        Location location = player.getLocation();
        String bossName = PalutenBoss.getInstance().getBossName();

        if (args.length < 1) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§8--------------" + PalutenBoss.getInstance().getBossName() + "§8---------------");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7opengui§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7spawn§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7settings§8> §8<§7language§8/§7effect§8/§7color§8/§7health§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7clean§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7reload§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§8------------------------------------------");
            return true;
        }

        switch (args[0]) {
            case "reload":
            case "rl":
                if (player.hasPermission(reloadPermission)) {
                    PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
                    PalutenBoss.getInstance().getFileUtil().setLanguage();
                    PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();
                    PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "reloadSuccess");
                    return true;
                }
            case "clean":
            case "clear":
                if (player.hasPermission(cleanPermission)) {
                    PalutenBoss.getInstance().getLoader().getInvManager().createWorldChooseInventory(player, "Choose World");
                    return true;
                }
            case "opengui":
            case "gui":
                if (player.hasPermission(settingsPermission)) {
                    PalutenBoss.getInstance().getLoader().getInvManager().createMainMenu(player, "Settings");
                    return true;
                }
            case "spawn":
            case "summon":
                if (player.hasPermission(spawnPermission)) {
                    PalutenBoss.getInstance().getEntityManager().spawnEntity(player, location, bossName, PalutenBoss.getInstance().getLoader().getConfigBuilder().getInteger("health"));
                    return true;
                }
            case "settings":
                if (args.length < 3) {
                    PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "argumentError");
                    return true;
                } else if (args.length > 3) {
                    return true;
                }

                switch (args[1]) {
                    case "color":
                    case "teamcolor":
                        if (player.hasPermission(colorPermission)) {
                            if (WoolColor.isValidColor(args[2].toUpperCase())) {
                                String fixedColorName = WoolColor.getNewWoolColorName(args[2].toUpperCase());
                                PalutenBoss.getInstance().getFileUtil().setConfigFilePath("teamColor", fixedColorName);
                                PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "teamSuccess");
                                return true;
                            } else {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            }

                            return false;
                        }
                    case "health":
                        if (player.hasPermission(healthPermission)) {
                            try {
                                if (Double.parseDouble(args[2]) >= 2048) {
                                    return true;
                                }
                                PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("health", Double.valueOf(args[2]));
                            } catch (NumberFormatException e) {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§c" + e);
                                return true;
                            }

                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "healthSuccess");
                        }
                        break;
                    case "motd":
                        boolean motd = Boolean.valueOf(args[2]);
                        try {
                            PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath("motd", motd);
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "motdSuccess");
                        } catch (IllegalArgumentException e) {
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§c" + e);
                        }
                        break;
                    case "effect":
                        if (player.hasPermission(effectPermission)) {
                            if (PalutenBoss.getInstance().getLoader().getRegisterManager().isValidParticle(args[2].toUpperCase())) {
                                PalutenBoss.getInstance().getFileUtil().setConfigFilePath("auraEffect", args[2].toUpperCase());
                                PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
                            } else {
                                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                            }

                            return true;
                        }
                    case "language":
                        if (player.hasPermission(languagePermission)) {
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

        switch (args.length) {
            case 1:
                completions.addAll(argsNullList);
                if (args[0].equals("clear")) {
                    completions.add("world");
                    completions.add("world_nether");
                    completions.add("world_the_end");
                }
                break;
            case 2:
                if (args[0].equals("settings")) {
                    if (player.hasPermission(settingsPermission)) {
                        completions.addAll(argsTwoList);
                    }
                }
                break;
            case 3:
                if (player.hasPermission(colorPermission)) {
                    switch (args[1].toLowerCase()) {
                        case "effect":
                            completions.addAll(effectsList);
                            break;
                        case "language":
                            completions.addAll(languageList);
                            break;
                        case "color":
                            completions.addAll(colorList);
                            break;
                        case "health":
                            completions.addAll(healthList);
                            break;
                        case "motd":
                            completions.addAll(booleanList);
                            break;
                        default:
                            break;
                    }
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