package com.github.Jena;

import com.flowpowered.math.vector.Vector3d;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyWorld;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.POIMarker;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.ShapeMarker;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class TownyBlueUpdater {

    public static void CompleteUpdate(MarkerSet set) {
        if (BlueMapAPI.getInstance().isPresent()) {
            for (BlueMapWorld world1 : BlueMapAPI.getInstance().get().getWorlds()) {
                for (BlueMapMap map : world1.getMaps()) {
                    World world = Bukkit.getWorld(map.getWorld().getUuid());
                    TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(world.getName());

                    try {
                        for (TownBlock townBlock : townyWorld.getTownBlocks()) {
                            double xvalue = townBlock.getCoord().getX() * 16;
                            double zvalue = townBlock.getCoord().getZ() * 16;

                            Shape shape = Shape.createRect(xvalue, zvalue, xvalue + 16, zvalue + 16);
                            ShapeMarker marker = set.createShapeMarker(townBlock.getTown().getName() + "_" + xvalue/16 + "_" + zvalue/16, map, shape, TownyBlue.config.getInt("y-height"));
                            marker.setLabel(getHTMLforTown(townBlock.getTown()));
                            marker.setBorderColor(marker.getFillColor());

                            if (townBlock.isHomeBlock()) {
                                Vector3d vector = new Vector3d();
                                vector.add(xvalue + 8, TownyBlue.config.getInt("y-height"), zvalue + 8);
                                POIMarker home = set.createPOIMarker(townBlock.getTown().getName() + "_" + xvalue/16 + "_" + zvalue/16, map, vector);
                                home.setLabel(getHTMLforTown(townBlock.getTown()));
                            }
                        }
                    } catch (NotRegisteredException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public static String getHTMLforTown(Town town) {
        String result = null;
        // get the main html string
        String Html = TownyBlue.config.getString("html");
        String stringresidents = getResidents(town);

        // placeholders
        Html = Html.replace("%name%", town.getName());
        if (town.hasNation()) {
            try {
                Html = Html.replace("%nation%", town.getNation().getName());
            } catch (NotRegisteredException e) {
                e.printStackTrace();
            }
        } else {Html = Html.replace("%nation%", "");}
        Html = Html.replace("%mayor%", town.getMayor().getName());
        Html = Html.replace("%residents%", stringresidents);

        if (Html != null) {result = Html;}

        return result;
    }

    private static String getResidents(Town town) {
        String result = "";
        StringBuilder resultBuilder = new StringBuilder(result);
        resultBuilder.append(town.getMayor().getName());

        for (Resident resident : town.getResidents()) {
            if (resident.getName() != null) {
                if (!resident.isMayor()) {
                    resultBuilder.append(", ");
                    resultBuilder.append(resident.getName());
                }
            }
        }
        result = resultBuilder.toString();

        return result;
    }
}
/*
*
* */
