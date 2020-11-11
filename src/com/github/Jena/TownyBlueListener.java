package com.github.Jena;

import com.palmergames.bukkit.towny.event.*;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;

public class TownyBlueListener implements Listener {
    private static MarkerSet set = TownyBlue.set;

    @EventHandler
    public static void onTownClaim(TownClaimEvent event) throws NotRegisteredException, IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTownBlock().getTown(), set);
        }
    }

    @EventHandler
    public static void onTownUnclaim(TownUnclaimEvent event) throws IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownAddResident(TownAddResidentEvent event) throws IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownRemoveResident(TownRemoveResidentEvent event) throws IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownChange(TownPreRenameEvent event) throws IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownChange(NationAddTownEvent event) throws IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

    @EventHandler
    public static void onTownChange(NationRemoveTownEvent event) throws IOException {
        if (BlueMapAPI.getInstance().isPresent()) {
            TownyBlueUpdater.TownUpdate(event.getTown(), set);
        }
    }

}
