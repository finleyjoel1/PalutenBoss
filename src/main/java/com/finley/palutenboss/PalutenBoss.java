package com.finley.palutenboss;

import com.finley.palutenboss.other.manager.entity.EntityManager;
import com.finley.palutenboss.other.util.Loader;
import com.finley.palutenboss.other.util.other.FileUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class PalutenBoss extends JavaPlugin {

    @Getter
    public static PalutenBoss instance;
    private final String prefix = "§6§lPaluten§e§lBoss §8» ";
    private final String bossName = "§6§lPaluten§e§lBoss";
    @Setter
    private Loader loader;
    @Getter
    private String permissionName = "palutenboss.";
    private FileUtil fileUtil;
    private EntityManager entityManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        entityManager = new EntityManager();
        loader = new Loader();
        fileUtil = new FileUtil();

        loader.registerAll();
    }

    @Override
    public void onDisable() {
        instance = null;
        fileUtil = null;
        loader = null;
    }
}