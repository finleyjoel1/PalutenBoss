package com.finley.palutenboss;

import com.finley.palutenboss.util.Loader;
import com.finley.palutenboss.util.manager.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PalutenBoss extends JavaPlugin {

    public static String prefix = "§6§lPaluten§e§lBoss §8» ";
    public static String bossName = "§6§lPaluten§e§lBoss";
    public static String permissionName = "palutenboss.";

    private static PalutenBoss instance;
    private static FileManager fileManager;
    public Loader loader;

    public static PalutenBoss getInstance() {
        return instance;
    }

    public Loader getLoader() {
        return loader;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public String getBossName() {
        return bossName;
    }

    public String getPrefix() {
        return prefix;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        loader = new Loader();
        fileManager = new FileManager();
        loader.registerAll();
    }

    @Override
    public void onDisable() {
        instance = null;
        fileManager = null;
        loader = null;
    }
}