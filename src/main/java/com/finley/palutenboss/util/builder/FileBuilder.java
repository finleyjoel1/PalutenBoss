package com.finley.palutenboss.util.builder;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class FileBuilder {
    public static HashMap<String, FileBuilder> fileBuilderCache = new HashMap<>();
    public final File file;
    public YamlConfiguration yaml;

    public FileBuilder(String path, String file) {
        fileBuilderCache.put(file, this);
        this.file = new File(path, file);
        File filePath = new File(path);

        if (!filePath.exists() && !filePath.mkdir()) {
            Bukkit.getLogger().severe("Unable to create Path '" + this.file.getPath() + "'");
        }

        if (!this.file.exists()) {
            try {
                if (!this.file.createNewFile())
                    Bukkit.getLogger().severe("Unable to create File '" + this.file.getPath() + this.file.getName() + "'");
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

    public Object get(String path) {
        return this.yaml.get(path);
    }

    public void load() {
        try {
            this.yaml.load(this.file);
        } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            this.yaml.save(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        if (!contains(path))
            return "Path '" + path + "' in '" + this.file.getPath() + this.file.getName() + "'";
        return this.yaml.getString(path);
    }

    public boolean getBoolean(String path) {
        if (!contains(path))
            return false;
        return this.yaml.getBoolean(path);
    }

    public int getInteger(String path) {
        if (!contains(path))
            return -1;
        return this.yaml.getInt(path);
    }

    public long getLong(String path) {
        if (!contains(path))
            return -1L;
        return this.yaml.getLong(path);
    }

    public double getDouble(String path) {
        if (!contains(path))
            return -1.0D;
        return this.yaml.getDouble(path);
    }

    public List<String> getStringList(String path) {
        if (!contains(path))
            return new ArrayList<>();
        return this.yaml.getStringList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        if (!contains(path))
            return new ArrayList<>();
        return this.yaml.getMapList(path);
    }

    public Collection<String> getConfigurationSection(String path) {
        if (!contains(path))
            return new ArrayList<>();
        return this.yaml.getConfigurationSection(path).getKeys(false);
    }

    public void reload() {
        load();
        save();
        load();
    }
}