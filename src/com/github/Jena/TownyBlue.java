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
    public static Logger logger;
    public static MarkerAPI api;

    @Override
    public void onEnable() {
        setPlugin();
        setConfig();
        saveDefaultConfig();
        setLogger();

        // initialization
        if (!BlueMapAPI.getInstance().isPresent()) {
            BlueMapAPI.onEnable(BlueMapAPI -> {
                try {
                    api = BlueMapAPI.getMarkerAPI();

                    api.load();
                    if (api.getMarkerSets() != null) {
                        for (MarkerSet set : api.getMarkerSets()) {
                            if (set.getId().equals("towns"))
                            api.removeMarkerSet(set);
                        }
                    }

                    MarkerSet set = api.createMarkerSet("towns");
                    TownyBlueUpdater.CompleteUpdate(set);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                api = BlueMapAPI.getInstance().get().getMarkerAPI();

                api.load();
                if (api.getMarkerSets() != null) {
                    for (MarkerSet set : api.getMarkerSets()) {
                        if (set.getId().equals("towns"))
                            api.removeMarkerSet(set);
                    }
                }

                MarkerSet set = api.createMarkerSet("towns");
                TownyBlueUpdater.CompleteUpdate(set);
            } catch (IOException e) {e.printStackTrace();}
        }

        if (config.getBoolean("updating")) {
            //noinspection deprecation
            Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, TownyBlueUpdater.Update, 0, config.getLong("update") * 20);
        }
    }

    public void setLogger() {
        logger = getLogger();
    }

    public void setPlugin() {
        plugin = this;
    }

    public void setConfig() {
        config = this.getConfig();
    }
}
