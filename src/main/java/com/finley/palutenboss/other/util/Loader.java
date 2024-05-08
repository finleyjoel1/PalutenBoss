package com.finley.palutenboss.other.util;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.normal.command.PalutenBoss_Command;
import com.finley.palutenboss.normal.listener.entity.EntityDAListener;
import com.finley.palutenboss.normal.listener.entity.EntityDEListener;
import com.finley.palutenboss.normal.listener.entity.EntityTListener;
import com.finley.palutenboss.normal.listener.player.PlayerIListener;
import com.finley.palutenboss.normal.listener.player.PlayerMListener;
import com.finley.palutenboss.other.manager.player.*;
import com.finley.palutenboss.other.util.builder.FileBuilder;
import lombok.Getter;

import java.util.Objects;

public class Loader {
    public static String language;
    private static FileBuilder fb;
    private static FileBuilder permission;
    private static FileBuilder messages;
    public final PalutenBoss palutenBoss;

    @Getter
    public final PermissionManager permissionManager;
    @Getter
    public final GuiManager guiManager;
    @Getter
    public final ItemManager itemManager;
    @Getter
    public final MenuManager menuManager;
    @Getter
    public final MessageManager messageManager;
    @Getter
    public final RecipeManager recipeManager;

    public Loader() {
        PalutenBoss.getInstance().setLoader(this);
        palutenBoss = PalutenBoss.getInstance();
        permission = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "permissions.yml");
        messageManager = new MessageManager();
        recipeManager = new RecipeManager();
        itemManager = new ItemManager();
        guiManager = new GuiManager();
        menuManager = new MenuManager();
        permissionManager = new PermissionManager();
        messages = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "messages.yml");
        fb = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
        language = fb.getString("language");
    }

    public FileBuilder getFileBuilder() {
        if (fb == null) {
            fb = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
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
        getRecipeManager().registerRecipe();
    }

    private void changeFile() {
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("language", "en");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("motd", false);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("auraEffect", "FLAME");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("teamColor", "GOLD");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("health", 750);
        PalutenBoss.getInstance().getLoader().getMessageManager().saveMessages();
        PalutenBoss.getInstance().getLoader().getFileBuilder().reload();
    }

    private void registerCommands() {
        Objects.requireNonNull(palutenBoss.getCommand("palutenboss")).setExecutor(new PalutenBoss_Command());
    }

    private void registerEvents() {
        new PlayerIListener();
        new PlayerMListener();
        new PlayerMListener();
        new EntityDAListener();
        new EntityDEListener();
        new EntityTListener();
    }
}