package com.github.Jena;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.event.*;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;

public class TownyBlueListener implements Listener {

    @EventHandler
    public static void onTownClaim(TownClaimEvent event) throws NotRegisteredException {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTownBlock().getTown(), set);
        }
    }

    @EventHandler
    public static void onTownUnclaim(TownUnclaimEvent event) {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownAddResident(TownAddResidentEvent event) {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownRemoveResident(TownRemoveResidentEvent event) {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownChange(TownPreRenameEvent event) {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownChange(NationAddTownEvent event) {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownChange(NationRemoveTownEvent event) {
        if (BlueMapAPI.getInstance().isPresent()) {
            MarkerSet set = getSet();
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    // debug

    @EventHandler
    public static void onCommand(PlayerCommandPreprocessEvent event) {
        if (TownyBlue.config.getBoolean("debug")) {
            MarkerSet set = getSet();
            if (event.getMessage().contains("/townyblue")) {
                if (event.getMessage().contains("updatec")) {
                    TownyBlueUpdater.CompleteUpdate(set);
                } else if (event.getMessage().contains("updatetown")) {
                    try {
                        TownyBlueUpdater.TownUpdate(TownyUniverse.getInstance().getDataSource().getTown("test"), set);
                    } catch (NotRegisteredException e) {
                        e.printStackTrace();
                        event.getPlayer().sendMessage("§cA town called test doesn't exist, please create one");
                    }
                }
            }
        }
    }

    public static MarkerSet getSet() {
        final MarkerSet[] result = {null};

        BlueMapAPI.getInstance().ifPresent(blueMapAPI -> {
            try {
                result[0] = blueMapAPI.getMarkerAPI().getMarkerSet("towns").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return result[0];
    }

}
