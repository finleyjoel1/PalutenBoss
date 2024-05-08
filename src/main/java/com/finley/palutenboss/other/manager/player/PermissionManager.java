package com.finley.palutenboss.other.manager.player;

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

    private void addPermission(String permissionName, String permission) {
        setPermissionPath(permissionName, permission);
    }

    private void setPermissionPath(String path, String value) {
        PalutenBoss.getInstance().getLoader().getPermission().setPathIfEmpty(path, value);
    }
}
