package com.finley.palutenboss.util.manager;

import com.finley.palutenboss.PalutenBoss;
import com.finley.palutenboss.util.Loader;
import com.finley.palutenboss.util.builders.FileBuilder;

import java.io.File;
import java.io.IOException;

public class FileManager {

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
