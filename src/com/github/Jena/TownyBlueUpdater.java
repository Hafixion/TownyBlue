package com.github.Jena;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyWorld;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.Shape;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class TownyBlueUpdater {

    public static void CompleteUpdate(MarkerSet set) {
        if (BlueMapAPI.getInstance().isPresent()) {
            for (BlueMapWorld world1 : BlueMapAPI.getInstance().get().getWorlds()) {
                for (BlueMapMap map : world1.getMaps()) {
                    World world = Bukkit.getWorld(map.getWorld().getUuid());
                    TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(world.getName());

                    for (Town town : townyWorld.getTowns().values()) {
                        try {
                            double xvalue = town.getHomeBlock().getCoord().getX() * 16;
                            double zvalue = town.getHomeBlock().getCoord().getZ() * 16;

                            Shape home = Shape.createRect(xvalue, zvalue, xvalue + 15, zvalue + 15);
                            set.createShapeMarker(town.getName(), map, home, 64);
                            set.setLabel(town.getName());
                        } catch (TownyException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
