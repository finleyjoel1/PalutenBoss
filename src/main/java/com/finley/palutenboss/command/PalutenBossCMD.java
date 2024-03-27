package com.finley.palutenboss.command;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.test.Test;
import com.finley.palutenboss.util.manager.WoolColor;
import com.finley.palutenboss.util.manager.entity.EntityManager;
import com.finley.palutenboss.util.manager.player.GuiManager;
import com.finley.palutenboss.util.manager.player.MenuManager;
import com.finley.palutenboss.util.manager.player.MessageManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PalutenBossCMD implements CommandExecutor, TabCompleter {

    private static final String spawnPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("spawnPermission");
    private static final String reloadPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("reloadPermission");
    private static final String settingsPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("settingsPermission");
    private static final String languagePermission = PalutenBoss.getInstance().getLoader().getPermission().getString("languagePermission");
    private static final String cleanPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("cleanPermission");
    private static final String colorPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("colorPermission");
    private static final String effectPermission = PalutenBoss.getInstance().getLoader().getPermission().getString("effectPermission");

    private static List<String> argsNullList = List.of("spawn", "reload", "clear", "opengui", "settings");
    private static List<String> argsTwoList = List.of("language", "color", "effect");
    private static List<String> colorList = List.of("gray", "black", "red", "yellow", "green", "blue", "purple");
    private static List<String> languageList = List.of("de", "en", "ru", "es", "lt", "zh", "ja");
    private static List<String> effectsList = List.of("flame", "water_wake", "campfire_signal_smoke");


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendNoPlayerMessage();
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        String bossName = PalutenBoss.getInstance().getBossName();

        if (!player.hasPermission(spawnPermission) || !player.hasPermission(settingsPermission)) {
            MessageManager.sendNoPermissionMessage(player);
            return true;
        }

        if (args.length < 1) {
            MessageManager.sendMessage(player, "§8--------------" + PalutenBoss.getInstance().getBossName() + "§8---------------");
            MessageManager.sendMessage(player, "§7/" + cmd.getName() + " §8<§7opengui§8>");
            MessageManager.sendMessage(player, "§7/" + cmd.getName() + " §8<§7spawn§8>");
            MessageManager.sendMessage(player, "§7/" + cmd.getName() + " §8<§7settings§8> §8<§7language§8/§7effect§8/§7color§8>");
            MessageManager.sendMessage(player, "§7/" + cmd.getName() + " §8<§7clean§8>");
            MessageManager.sendMessage(player, "§7/" + cmd.getName() + " §8<§7reload§8>");
            MessageManager.sendMessage(player, "§8------------------------------------------");
            return true;
        }

        if(args[0].equalsIgnoreCase("test")) {
            Test test = new Test();
            test.createNPC(player, location);
        }

        if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (player.hasPermission(reloadPermission)) {
                PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
                PalutenBoss.getInstance().getFileManager().setLanguage();
                PalutenBoss.getInstance().getLoader().getMessages().reload();
                MessageManager.sendMessageToPlayer(player, "reloadSuccess");
                return true;
            }
        }


        if (args[0].equalsIgnoreCase("clean") || args[0].equalsIgnoreCase("clear")) {
            if (player.hasPermission(cleanPermission)) {
                GuiManager.createWorldChooseInventory(player, "Choose World");
                return true;
            }
        }


        if (args[0].equalsIgnoreCase("opengui")) {
            if (player.hasPermission(settingsPermission)) {
                GuiManager.createInventory(player, "Settings");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("summon")) {
            if (player.hasPermission(spawnPermission)) {
                EntityManager.spawnEntity(player, location, bossName);
                MessageManager.sendTitleToPlayer(player, "alert");
                MessageManager.sendMessageToPlayer(player, "spawnSuccess");
                return true;
            }
        }

        if (args[0].equalsIgnoreCase("settings")) {

            if (args.length < 3) {
                MessageManager.sendMessageToPlayer(player, "argumentError");
                return true;
            }

            if (args[1].equalsIgnoreCase("color") || args[1].equalsIgnoreCase("teamcolor")) {
                if (player.hasPermission(colorPermission)) {

                    if (WoolColor.isValidColor(args[2].toUpperCase())) {
                        PalutenBoss.getInstance().getFileManager().setConfigFilePath("teamColor", args[2].toUpperCase());
                        PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
                        MessageManager.sendMessageToPlayer(player, "teamSuccess");
                        return true;
                    } else {
                        MessageManager.sendMessageToPlayer(player, "notFound");
                    }

                    return false;
                }
            }

            if (args[1].equalsIgnoreCase("effect")) {
                if (player.hasPermission(effectPermission)) {

                    if (MenuManager.isValidParticle(args[2].toUpperCase())) {
                        PalutenBoss.getInstance().getFileManager().setConfigFilePath("auraEffect", args[2].toUpperCase());
                        PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
                        MessageManager.sendMessageToPlayer(player, "effectSuccess");
                    } else {
                        MessageManager.sendMessageToPlayer(player, "notFound");
                    }

                    return true;
                }
            }

            if (args[1].equalsIgnoreCase("language")) {
                if (player.hasPermission(languagePermission)) {

                    if (args[2].equalsIgnoreCase(PalutenBoss.getInstance().getLoader().getFileBuilder().getString("language"))) {
                        MessageManager.sendMessageToPlayer(player, "alreadyLanguage");
                        return true;
                    }

                    if (MessageManager.isValidLanguage(args[2])) {
                        PalutenBoss.getInstance().getFileManager().setConfigFilePath("language", args[2]);
                        PalutenBoss.getInstance().getFileManager().setLanguage();
                        MessageManager.sendMessageToPlayer(player, "changeLanguage");
                    } else {
                        MessageManager.sendMessageToPlayer(player, "notFound");
                    }

                    return true;
                }
            }

        }

        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player) sender;

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

        return completions;
    }


}
