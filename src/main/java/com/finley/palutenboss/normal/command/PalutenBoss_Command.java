package com.finley.palutenboss.normal.command;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.other.util.other.WoolColor;
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

public class PalutenBoss_Command implements CommandExecutor, TabCompleter {

    private final String spawnPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("spawnPermission");
    private final String reloadPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("reloadPermission");
    private final String settingsPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("settingsPermission");
    private final String languagePermission = PalutenBoss.getInstance().getLoader().getPermission().getString("languagePermission");
    private final String cleanPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("cleanPermission");
    private final String colorPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("colorPermission");
    private final String effectPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("effectPermission");

    private final List<String> argsNullList = List.of("spawn", "reload", "clear", "opengui", "settings");
    private final List<String> argsTwoList = List.of("language", "color", "effect");
    private final List<String> colorList = List.of("gray", "black", "red", "yellow", "green", "blue", "purple");
    private final List<String> languageList = List.of("de", "en", "ru", "es", "lt", "zh", "ja");
    private final List<String> effectsList = List.of("flame", "water_wake", "campfire_signal_smoke");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendNoPlayerMessage();
            return true;
        }

        Location location = player.getLocation();
        String bossName = PalutenBoss.getInstance().getBossName();

        if (!player.hasPermission(spawnPermission) || !player.hasPermission(settingsPermission)) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendNoPermissionMessage(player);
            return true;
        }

        if (args.length < 1) {
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§8--------------" + PalutenBoss.getInstance().getBossName() + "§8---------------");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7opengui§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7spawn§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7settings§8> §8<§7language§8/§7effect§8/§7color§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7clean§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§7/" + cmd.getName() + " §8<§7reload§8>");
            PalutenBoss.getInstance().getLoader().getMessageManager().sendMessage(player, "§8------------------------------------------");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (player.hasPermission(reloadPermission)) {
                PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
                PalutenBoss.getInstance().getFileUtil().setLanguage();
                PalutenBoss.getInstance().getLoader().getMessages().reload();
                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "reloadSuccess");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("clean") || args[0].equalsIgnoreCase("clear")) {
            if (player.hasPermission(cleanPermission)) {
                PalutenBoss.getInstance().getLoader().getGuiManager().createWorldChooseInventory(player, "Choose World");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("opengui")) {
            if (player.hasPermission(settingsPermission)) {
                PalutenBoss.getInstance().getLoader().getGuiManager().createInventory(player, "Settings");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("summon")) {
            if (player.hasPermission(spawnPermission)) {
                PalutenBoss.getInstance().getEntityManager().spawnEntity(player, location, bossName, PalutenBoss.getInstance().getLoader().getFileBuilder().getInteger("health"));
                PalutenBoss.getInstance().getLoader().getMessageManager().sendTitleToPlayer(player, "alert");
                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "spawnSuccess");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("settings")) {

            if (args.length < 3) {
                PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "argumentError");
                return true;
            }

            if (args[1].equalsIgnoreCase("color") || args[1].equalsIgnoreCase("teamcolor")) {
                if (player.hasPermission(colorPermission)) {

                    if (WoolColor.isValidColor(args[2].toUpperCase())) {
                        PalutenBoss.getInstance().getFileUtil().setConfigFilePath("teamColor", args[2].toUpperCase());
                        PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
                        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "teamSuccess");
                        return true;
                    } else {
                        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                    }

                    return false;
                }
            }

            if (args[1].equalsIgnoreCase("effect")) {
                if (player.hasPermission(effectPermission)) {

                    if (PalutenBoss.getInstance().getLoader().getMenuManager().isValidParticle(args[2].toUpperCase())) {
                        PalutenBoss.getInstance().getFileUtil().setConfigFilePath("auraEffect", args[2].toUpperCase());
                        PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
                        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "effectSuccess");
                    } else {
                        PalutenBoss.getInstance().getLoader().getMessageManager().sendMessageToPlayer(player, "notFound");
                    }

                    return true;
                }
            }

            if (args[1].equalsIgnoreCase("language")) {
                if (player.hasPermission(languagePermission)) {

                    if (args[2].equalsIgnoreCase(PalutenBoss.getInstance().getLoader().getFileBuilder().getString("language"))) {
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

        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(argsNullList);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("settings") && player.hasPermission(settingsPermission)) {
            completions.addAll(argsTwoList);
        } else if (args.length == 3 && player.hasPermission(colorPermission)) {
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
                default:
                    break;
            }
        }

        String currentArg = args[args.length - 1].toLowerCase();
        completions.removeIf(c -> !c.toLowerCase().startsWith(currentArg));

        //Gebe completions wieder zurück
        return completions;
    }


}
