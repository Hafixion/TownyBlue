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

import java.awt.*;
import java.io.IOException;

public class TownyBlueUpdater {
    public static Runnable CompleteUpdate = TownyBlueUpdater::CompleteUpdateTask;
    //todo add config values
    public static Color towncolor = new Color(255, 0 , 0, 100);
    public static Color nationcolor = new Color(25, 25, 255, 100);


    public static void CompleteUpdate(MarkerSet set) {
        try {
            if (BlueMapAPI.getInstance().isPresent() && BlueMapAPI.getInstance().get().getMarkerAPI().getMarkerSet("towns").isPresent()) {
                for (BlueMapWorld world1 : BlueMapAPI.getInstance().get().getWorlds()) {
                    for (BlueMapMap map : world1.getMaps()) {
                        World world = Bukkit.getWorld(map.getWorld().getUuid());
                        TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(world.getName());

                        for (TownBlock townBlock : townyWorld.getTownBlocks()) {
                            double xvalue = townBlock.getCoord().getX() * 16;
                            double zvalue = townBlock.getCoord().getZ() * 16;

                            Shape shape = Shape.createRect(xvalue, zvalue, xvalue + 16, zvalue + 16);
                            ShapeMarker marker = set.createShapeMarker(townBlock.getTown().getName() + "_" + xvalue / 16 + "_" + zvalue / 16, map, shape, TownyBlue.config.getInt("y-height"));
                            marker.setLabel(getHTMLforTown(townBlock.getTown()));
                            marker.setMaxDistance(1000);

                            if (townBlock.getTown().hasNation()) {
                                marker.setFillColor(nationcolor);
                            } else {marker.setFillColor(towncolor);}
                            marker.setBorderColor(marker.getFillColor());

                            if (townBlock.isHomeBlock()) {
                                Vector3d vector = new Vector3d(xvalue + 8, TownyBlue.config.getInt("y-height") + 3, zvalue + 8);
                                POIMarker home = set.createPOIMarker(townBlock.getTown().getName() + "_" + xvalue / 16 + "_" + zvalue / 16 + "_icon", map, vector);
                                home.setLabel(getHTMLforTown(townBlock.getTown()));
                                home.setIcon(TownyBlue.config.getString("home-marker"), home.getIconAnchor());
                                home.setMaxDistance(TownyBlue.config.getInt("max-distance"));

                                if (townBlock.getTown().isCapital()) {
                                    home.setIcon(TownyBlue.config.getString("capital-marker"), home.getIconAnchor());
                                }
                            }
                        }
                    }
                }

            }
        } catch (NotRegisteredException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void TownUpdate(Town town, MarkerSet set) {
        try {
            if (BlueMapAPI.getInstance().isPresent()) {
                for (BlueMapWorld world1 : BlueMapAPI.getInstance().get().getWorlds()) {
                    for (BlueMapMap map : world1.getMaps()) {
                        if (map.getWorld().getUuid() == town.getWorld().getUID()) {
                            for (TownBlock townBlock : town.getTownBlocks()) {
                                double xvalue = townBlock.getCoord().getX() * 16;
                                double zvalue = townBlock.getCoord().getZ() * 16;

                                Shape shape = Shape.createRect(xvalue, zvalue, xvalue + 16, zvalue + 16);
                                ShapeMarker marker = set.createShapeMarker(townBlock.getTown().getName() + "_" + xvalue / 16 + "_" + zvalue / 16, map, shape, TownyBlue.config.getInt("y-height"));
                                marker.setLabel(getHTMLforTown(townBlock.getTown()));
                                marker.setMaxDistance(1000);

                                if (townBlock.getTown().hasNation()) {
                                    marker.setFillColor(nationcolor);
                                } else {marker.setFillColor(towncolor);}
                                marker.setBorderColor(marker.getFillColor());

                                if (townBlock.isHomeBlock()) {
                                    Vector3d vector = new Vector3d(xvalue + 8, TownyBlue.config.getInt("y-height") + 3, zvalue + 8);
                                    POIMarker home = set.createPOIMarker(townBlock.getTown().getName() + "_icon", map, vector);
                                    home.setLabel(getHTMLforTown(townBlock.getTown()));
                                    home.setIcon(TownyBlue.config.getString("home-marker"), home.getIconAnchor());
                                    home.setMaxDistance(TownyBlue.config.getInt("max-distance"));

                                    if (townBlock.getTown().isCapital()) {
                                        home.setIcon(TownyBlue.config.getString("capital-marker"), home.getIconAnchor());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (NotRegisteredException e) {
            e.printStackTrace();
        }
    }

    private static void CompleteUpdateTask() {
        MarkerSet set = TownyBlue.set;
        if (set != null) {
            CompleteUpdate(set);
        }
    }

    public static String getHTMLforTown(Town town) {
        String Html = TownyBlue.config.getString("html");
        if (Html != null) {
            String stringresidents = getResidents(town);

            // placeholders
            Html = Html.replace("%name%", town.getName());
            if (town.hasNation()) {
                try {
                    Html = Html.replace("%nation%", town.getNation().getName());
                } catch (NotRegisteredException e) {
                    e.printStackTrace();
                }
            } else {
                Html = Html.replace("%nation%", "");
            }
            Html = Html.replace("%mayor%", town.getMayor().getName());
            Html = Html.replace("%residents%", stringresidents);
            Html = Html.replace("%residentcount%", String.valueOf(town.getResidents().stream().toArray().length));
            // add extra placeholders here, just Html = Html.replace("%yourplaceholder%", thing);

        } else {Html = "";}

        return Html;
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
