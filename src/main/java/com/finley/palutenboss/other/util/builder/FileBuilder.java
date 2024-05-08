package com.finley.palutenboss.other.util.builder;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class FileBuilder {

    public static HashMap<String, FileBuilder> fileBuilderCache = new HashMap<>();
    private final File file;
    private final YamlConfiguration yaml;

    public FileBuilder(String path, String file) {
        fileBuilderCache.put(file, this);
        this.file = new File(path, file);

        File filePath = new File(path);
        if (!filePath.exists()) {
            if (!filePath.mkdir()) {
                Bukkit.getLogger().severe("Unable to create Path '" + this.file.getPath() + "'");
            }
        }

        if (!this.file.exists()) {
            try {
                if (!this.file.createNewFile()) {
                    Bukkit.getLogger().severe("Unable to create File '" + this.file.getPath() + this.file.getName() + "'");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        this.yaml = YamlConfiguration.loadConfiguration(this.file);
    }

    public static FileBuilder getFileBuilder(String path, String file) {
        if (!fileBuilderCache.containsKey(file)) {
            return new FileBuilder(path, file);
        }

        return fileBuilderCache.get(file);
    }

    public void load() {
        try {
            this.yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            this.yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        try {
            this.yaml.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String setDefaultOrGetStringValue(String path, String value) {
        if (!this.yaml.contains(path)) {
            this.yaml.set(path, value);
            save();
            return value;
        }
        return this.yaml.getString(path);
    }

    public List<String> setDefaultOrGetStringListValue(String path, List<String> value) {
        if (!this.yaml.contains(path)) {
            this.yaml.set(path, value);
            save();
            return value;
        }
        return this.yaml.getStringList(path);
    }

    public void setPathIfEmpty(String path, Object value) {
        if (!this.yaml.contains(path)) {
            this.yaml.set(path, value);
            save();
        }
    }

    public void setPath(String path, Object value) {
        this.yaml.set(path, value);
        save();
    }

    public boolean contains(String path) {
        return this.yaml.contains(path);
    }

    public String getString(String path) {
        if (!contains(path)) {
            return "Path '" + path + "' in '" + this.file.getPath() + this.file.getName() + "'";
        }
        return this.yaml.getString(path);
    }

    public boolean getBoolean(String path) {
        if (!contains(path)) {
            return false;
        }

        return this.yaml.getBoolean(path);
    }

    public int getInteger(String path) {
        if (!contains(path)) {
            return -1;
        }

        return this.yaml.getInt(path);
    }

    public long getLong(String path) {
        if (!contains(path)) {
            return -1;
        }

        return this.yaml.getLong(path);
    }

    public double getDouble(String path) {
        if (!contains(path)) {
            return -1;
        }

        return this.yaml.getDouble(path);
    }

    public List<String> getStringList(String path) {
        if (!contains(path)) {
            return new ArrayList<>();
        }

        return this.yaml.getStringList(path);
    }

    public Collection<String> getConfigurationSection(String path) {
        if (!contains(path)) {
            return new ArrayList<>();
        }

        return this.yaml.getConfigurationSection(path).getKeys(false);
    }
}
