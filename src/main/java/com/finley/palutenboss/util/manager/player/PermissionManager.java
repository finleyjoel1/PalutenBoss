package com.finley.palutenboss.util.manager.player;

import com.finley.palutenboss.PalutenBoss;

public class PermissionManager {

    public PermissionManager() {
        addPermission("spawnPermission", PalutenBoss.getInstance().getPermissionName() + "spawn");
        addPermission("reloadPermission", PalutenBoss.getInstance().getPermissionName() + "reload");
        addPermission("settingsPermission", PalutenBoss.getInstance().getPermissionName() + "settings");
        addPermission("languagePermission", PalutenBoss.getInstance().getPermissionName() + "language");
        addPermission("cleanPermission", PalutenBoss.getInstance().getPermissionName() + "clean");
        addPermission("colorPermission", PalutenBoss.getInstance().getPermissionName() + "color");
    }

    public void addPermission(String permissionName, String permission) {
        setPermissionPath(permissionName, permission);
    }

    public void setPermissionPath(String path, String value) {
        PalutenBoss.getInstance().getLoader().getPermission().setPathIfEmpty(path, value);
    }
}
