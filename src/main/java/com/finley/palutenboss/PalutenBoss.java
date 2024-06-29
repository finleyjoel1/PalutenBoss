package com.finley.palutenboss;

import com.finley.palutenboss.manager.EntityManager;
import com.finley.palutenboss.util.Loader;
import com.finley.palutenboss.util.other.FileUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PalutenBoss extends JavaPlugin {

    @Getter
    private static PalutenBoss instance;

    private final String prefix = "§6§lPaluten§e§lBoss §8» ";
    private final String bossName = "§6§lPaluten§e§lBoss";
    private final String permissionName = "palutenboss.";

    @Setter
    private Loader loader;
    private FileUtil fileUtil;
    private EntityManager entityManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        entityManager = new EntityManager();
        fileUtil = new FileUtil();
        loader = new Loader();
    }

    @Override
    public void onDisable() {
        instance = null;
        fileUtil = null;
        loader = null;
    }
}