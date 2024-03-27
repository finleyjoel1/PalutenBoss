package com.finley.palutenboss.util;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.command.PalutenBossCMD;
import com.finley.palutenboss.listener.entity.EntityDamageListener;
import com.finley.palutenboss.listener.entity.EntityDeathListener;
import com.finley.palutenboss.listener.entity.EntityTransformListener;
import com.finley.palutenboss.listener.player.PlayerInvListener;
import com.finley.palutenboss.listener.player.PlayerMotdListener;
import com.finley.palutenboss.util.builders.FileBuilder;
import com.finley.palutenboss.util.manager.player.MessageManager;
import com.finley.palutenboss.util.manager.player.PermissionManager;
import com.finley.palutenboss.util.manager.player.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Objects;

public class Loader {
    public static String language;
    private static FileBuilder fb;
    private static FileBuilder permission;
    private static FileBuilder messages;
    private final PalutenBoss palutenBoss;
    private final PermissionManager permissionManager;

    public Loader() {
        PalutenBoss.getInstance().loader = this;
        palutenBoss = PalutenBoss.getInstance();
        permission = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "permissions.yml");
        permissionManager = new PermissionManager();
        messages = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "messages.yml");
        fb = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
        language = fb.getString("language");
    }

    public FileBuilder getFileBuilder() {
        if (fb == null) {
            fb = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
            PalutenBoss.getInstance().getFileManager().setLanguage();
            PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessages().reload();
            PalutenBoss.getInstance().getLoader().getPermission().reload();
        }

        return fb;
    }

    public FileBuilder getMessages() {
        if (messages == null) {
            messages = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "messages.yml");
        }
        return messages;
    }

    public FileBuilder getPermission() {
        if (permission == null) {
            permission = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "permissions.yml");
        }
        return permission;
    }

    public void registerAll() {
        changeFile();
        registerEvents();
        registerCommands();
        RecipeManager.registerRecipe();
    }

    private void changeFile() {
        PalutenBoss.getInstance().getFileManager().setConfigFilePathIfEmpty("language", "en");
        PalutenBoss.getInstance().getFileManager().setConfigFilePathIfEmpty("motd", false);
        PalutenBoss.getInstance().getFileManager().setConfigFilePathIfEmpty("auraEffect", "FLAME");
        PalutenBoss.getInstance().getFileManager().setConfigFilePathIfEmpty("teamColor", "GOLD");
        PalutenBoss.getInstance().getFileManager().setConfigFilePathIfEmpty("health", 750);

        MessageManager.saveMessages();
        PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
    }

    private void registerCommands() {
        Objects.requireNonNull(palutenBoss.getCommand("palutenboss")).setExecutor(new PalutenBossCMD());
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerInvListener(), palutenBoss);
        pm.registerEvents(new PlayerMotdListener(), palutenBoss);
        pm.registerEvents(new EntityDamageListener(), palutenBoss);
        pm.registerEvents(new EntityDeathListener(), palutenBoss);
        pm.registerEvents(new EntityTransformListener(), palutenBoss);
    }
}