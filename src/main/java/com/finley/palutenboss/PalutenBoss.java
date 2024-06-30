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

    private final String prefixName = "§x§F§6§8§9§1§0§lP§x§F§5§9§5§0§E§la§x§F§3§A§1§0§D§ll§x§F§2§A§C§0§B§lu§x§F§0§B§8§0§A§lt§x§E§F§C§4§0§8§le§x§E§D§D§0§0§6§ln§x§E§C§D§C§0§5§lB§x§E§A§E§7§0§3§lo§x§E§9§F§3§0§2§ls§x§E§7§F§F§0§0§ls";
    private final String prefix = prefixName + " §8| ";
    private final String bossName = "&6&lPaluten&e&lBoss";
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