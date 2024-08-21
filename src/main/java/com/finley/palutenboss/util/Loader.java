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

    private InvManager invManager;
    private ItemManager itemManager;
    private PermissionManager permissionManager;
    private RegisterManager registerManager;
    private MessageManager messageManager;
    private EntityManager entityManager;
    private RecipeUtil recipeUtil;
    private FileBuilder permissionFile;
    private FileBuilder messageBuilder;
    private FileBuilder configBuilder;
    private FileBuilder playerLanguagesBuilder;
    private String defaultLanguage;

    public Loader() {
        PalutenBoss.getInstance().setLoader(this);
        configBuilder = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml");
        messageBuilder = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "messages.yml");
        permissionFile = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "permissions.yml");
        playerLanguagesBuilder = FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "playerLanguages.yml");
        messageManager = new MessageManager(playerLanguagesBuilder);

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
        entityManager = new EntityManager();

        registerEvents();
        registerCommands();
        getRecipeUtil().registerRecipe();
    }

    public FileBuilder getConfigBuilder() {
        if (configBuilder == null) {
            setConfigBuilder(FileBuilder.getFileBuilder(PalutenBoss.getInstance().getDataFolder().getPath(), "config.yml"));
            PalutenBoss.getInstance().getFileUtil().setLanguage();
            PalutenBoss.getInstance().getLoader().getConfigBuilder().reload();
            PalutenBoss.getInstance().getLoader().getMessageBuilder().reload();
            PalutenBoss.getInstance().getLoader().getPermissionFile().reload();
            PalutenBoss.getInstance().getLoader().getPlayerLanguagesBuilder().reload();
        }
        return configBuilder;
    }

    private void manageFile() {
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("language", "en");
        defaultLanguage = getConfigBuilder().getString("language");
        getMessageManager().saveMessages();
        getMessageManager().loadMessages();
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("debug", true);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("motd.toggled", true);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("motd.updateOnPing", false);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("entity.showHealth", true);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("entity.auraEffect", "FLAME");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("entity.teamColor", "GOLD");
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("entity.health", 750);
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("success.successType", "actionbar"); //actionbar or chat
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("drops.dropItemIfPlayerExist", "palutenboss_sword".toUpperCase());
        PalutenBoss.getInstance().getFileUtil().setConfigFilePathIfEmpty("drops.dropItemIfPlayerNotExist", "dirt".toUpperCase());
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