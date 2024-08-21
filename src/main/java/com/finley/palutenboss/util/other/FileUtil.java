package com.finley.palutenboss.util.other;

import com.finley.palutenboss.PalutenBoss;

public class FileUtil {

    public void setLanguage() {
        PalutenBoss.getInstance().getLoader().setDefaultLanguage(PalutenBoss.getInstance().getLoader().getConfigBuilder().getString("language"));
    }

    public void setConfigFilePath(String path, String value) {
        PalutenBoss.getInstance().getLoader().getConfigBuilder().setPath(path, value);
    }

    public void setConfigFilePathIfEmpty(String path, Object value) {
        PalutenBoss.getInstance().getLoader().getConfigBuilder().setPathIfEmpty(path, value);
    }
}