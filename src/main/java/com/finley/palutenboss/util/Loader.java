package com.finley.palutenboss.util;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.command.PCommand;
import com.finley.palutenboss.listener.*;
import com.finley.palutenboss.manager.*;
import com.finley.palutenboss.util.builder.FileBuilder;
import com.finley.palutenboss.util.other.RecipeUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter

public class Loader {

    public final PermissionManager permissionManager;
    public final InvManager invManager;
    public final ItemManager itemManager;
    public final RegisterManager registerManager;
    public final MessageManager messageManager;
    public final RecipeUtil recipeUtil;

    private FileBuilder permissionFile;
    private FileBuilder messageBuilder;
    private FileBuilder configBuilder;
    private String language;

    public Loader() {
        PalutenBoss.getInstance().setLoader(this);
        configBuilder = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
        messageBuilder = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "messages.yml");
        permissionFile = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "permissions.yml");
        messageManager = new MessageManager();

        PalutenBoss.getInstance().getLoader().getConfigBuilder().save();
        PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();

        manageFile();
        PalutenBoss.getInstance().getLoader().getMessageManager().saveMessages();
        PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();

        recipeUtil = new RecipeUtil();
        itemManager = new ItemManager();
        invManager = new InvManager();
        registerManager = new RegisterManager();
        permissionManager = new PermissionManager();

        registerEvents();
        registerCommands();
        getRecipeUtil().registerRecipe();
    }

    public FileBuilder getConfigBuilder() {
        if (configBuilder == null) {
            configBuilder = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();
            PalutenBoss.getInstance().getLoader().getPermissionFile().reload();
        }
        return configBuilder;
    }

    private void manageFile() {
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("language", "en");
        language = getConfigBuilder().getString("language");
        PalutenBoss.getInstance().getLoader().getMessageManager().saveMessages();
        PalutenBoss.getInstance().getLoader().getMessageManager().loadMessages();

        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("motd", false);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("auraEffect", "FLAME");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("teamColor", "GOLD");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("health", 750);

    }

    private void registerCommands() {
        Objects.requireNonNull(PalutenBoss.getInstance().getCommand("palutenboss")).setExecutor(new PCommand());
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