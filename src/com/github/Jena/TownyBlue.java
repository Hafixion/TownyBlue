package com.github.Jena;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class TownyBlue extends JavaPlugin {
    public static Plugin plugin;
    public static FileConfiguration config;


    @Override
    public void onEnable() {
        register();
        if (!BlueMapAPI.getInstance().isPresent()) {
            BlueMapAPI.onEnable(api -> {
                try {
                    MarkerAPI markerApi = api.getMarkerAPI();
                    MarkerSet set = markerApi.createMarkerSet("towns");
                    TownyBlueUpdater.CompleteUpdate(set);
                    markerApi.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                MarkerAPI markerApi = BlueMapAPI.getInstance().get().getMarkerAPI();
                MarkerSet set = markerApi.createMarkerSet("towns");
                TownyBlueUpdater.CompleteUpdate(set);
                markerApi.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        saveDefaultConfig();
        // commands
        // listeners
        // schedules
    }

    public void setPlugin() {
        plugin = this;
    }

    public void setConfig() {
        config = this.getConfig();
    }
}
