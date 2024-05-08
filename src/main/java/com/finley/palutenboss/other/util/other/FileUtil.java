package com.finley.palutenboss.other.util.other;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.other.util.Loader;

public class FileUtil {

    public void setLanguage() {
        Loader.language = PalutenBoss.getInstance().getLoader().getFileBuilder().getString("language");
    }

    public void setConfigFilePath(String path, String value) {
        PalutenBoss.getInstance().getLoader().getFileBuilder().setPath(path, value);
    }

    public void setConfigFilePathIfEmpty(String path, Object value) {
        PalutenBoss.getInstance().getLoader().getFileBuilder().setPathIfEmpty(path, value);
    }

}
