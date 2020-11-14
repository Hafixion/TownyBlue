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
    protected static Runnable Update = TownyBlue::Update;

    // async update
    public static void Update() {
        BlueMapAPI.getInstance().ifPresent(blueMapAPI -> {
            // task to get rid of town markerset
            try {
                blueMapAPI.getMarkerAPI().load();
                if (blueMapAPI.getMarkerAPI().getMarkerSets() != null)
                    for (MarkerSet set : blueMapAPI.getMarkerAPI().getMarkerSets())
                        if (set.getId().equals("towns"))
                            blueMapAPI.getMarkerAPI().removeMarkerSet(set);
                MarkerSet set = blueMapAPI.getMarkerAPI().createMarkerSet("towns");
                TownyBlueTask task = new TownyBlueTask(set);
                task.runTask(plugin);
                BlueMapAPI.getInstance().get().getMarkerAPI().save();
            } catch (IOException e) {e.printStackTrace();}
        });
    }

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

                    BlueMapAPI.getMarkerAPI().load();
                    if (BlueMapAPI.getMarkerAPI().getMarkerSets() != null) {
                        for (MarkerSet set : BlueMapAPI.getMarkerAPI().getMarkerSets()) {
                            if (set.getId().equals("towns"))
                                BlueMapAPI.getMarkerAPI().removeMarkerSet(set);
                        }
                    }
                    MarkerSet set = BlueMapAPI.getMarkerAPI().createMarkerSet("towns");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                MarkerAPI api = BlueMapAPI.getInstance().get().getMarkerAPI();
                api.load();
                if (api.getMarkerSets() != null) {
                    for (MarkerSet set : api.getMarkerSets()) {
                        if (set.getId().equals("towns"))
                            api.removeMarkerSet(set);
                    }
                }
                MarkerSet set = api.createMarkerSet("towns");
            } catch (IOException e) {e.printStackTrace();}
        }

        if (config.getBoolean("updating")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, Update, 0, config.getLong("update") * 20);
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
