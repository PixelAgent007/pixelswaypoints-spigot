package org.pixel.pixelswaypoints.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.pixel.pixelswaypoints.Main;

public class ConfigHandler {
    private final Main main;

    public ConfigHandler(Main main) {
        this.main = main;
    }

    // Lang + Config
    private File getConfigAsFile() {
        return new File(this.main.getDataFolder(), "config.yml");
    }
    public YamlConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(this.getConfigAsFile());
    }
    public void saveConfig() {
        try {
            this.getConfig().save(this.getConfigAsFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Data
    private File getDataAsFile() {
        return new File(this.main.getDataFolder(), "waypoints.yml");
    }
    public YamlConfiguration getData() {
        return YamlConfiguration.loadConfiguration(this.getDataAsFile());
    }
    public void saveData() {
        try {
            this.getData().save(this.getDataAsFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
