package com.github.Jena;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class TownyBlue extends JavaPlugin {
    public static Plugin plugin;
    public static FileConfiguration config;
    public static Optional<BlueMapAPI> api;
    public static MarkerAPI markerAPI;


    @Override
    public void onEnable() {
        register();
        MarkerSet set = TownyBlueUpdater.CompleteUpdate();
        try {
            markerAPI.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Logger getLogger() {
        return this.getServer().getLogger();
    }

    private void register() {
        // vars
        setPlugin();
        setConfig();
        // commands
        // listeners
        // apis
        api = BlueMapAPI.getInstance();
        try {
            markerAPI = api.get().getMarkerAPI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveDefaultConfig();
        // schedules
    }

    public void setPlugin() {
        plugin = this;
    }

    public void setConfig() {
        config = this.getConfig();
    }
}
