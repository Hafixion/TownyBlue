package com.github.Jena;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class TownyBlue extends JavaPlugin {
    public static Plugin plugin;
    public static FileConfiguration config;
    public static MarkerSet set;
    public static MarkerAPI api;

    @Override
    public void onEnable() {
        register();
        if (!BlueMapAPI.getInstance().isPresent()) {
            BlueMapAPI.onEnable(BlueMapAPI -> {
                try {
                    MarkerAPI api = BlueMapAPI.getMarkerAPI();

                    if (api.getMarkerSets() != null) {
                        for (MarkerSet set : api.getMarkerSets()) {
                            if (set.getId().equals("towns"))
                            api.removeMarkerSet(set);
                        }
                    }
                    TownyBlue.api = api;
                    set = api.createMarkerSet("towns");
                    TownyBlueUpdater.CompleteUpdate(set);
                    api.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                MarkerAPI api = BlueMapAPI.getInstance().get().getMarkerAPI();
                TownyBlue.api = api;
                if (api.getMarkerSets() != null) {
                    for (MarkerSet set : api.getMarkerSets()) {
                        api.removeMarkerSet(set);
                    }
                }
                set = api.createMarkerSet("towns");
                TownyBlueUpdater.CompleteUpdate(set);
                api.save();
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
        getServer().getPluginManager().registerEvents(new TownyBlueListener(), this);
        // schedules
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, TownyBlueUpdater.CompleteUpdate, 0, 72000);
    }

    public void setPlugin() {
        plugin = this;
    }

    public void setConfig() {
        config = this.getConfig();
    }
}
