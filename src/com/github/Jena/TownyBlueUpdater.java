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
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;
import java.io.IOException;

public class TownyBlueUpdater {
    protected static Runnable Update = TownyBlueUpdater::Update;
    private static final FileConfiguration config = TownyBlue.config;
    //todo add config values
    public static Color towncolor = new Color(255, 0 , 0, 100);
    public static Color nationcolor = new Color(0, 190, 200, 100);


    public static void CompleteUpdate(MarkerSet set) {
        try {
            if (BlueMapAPI.getInstance().isPresent()) {
                if (BlueMapAPI.getInstance().get().getMarkerAPI().getMarkerSet("towns").isPresent()) {
                    BlueMapAPI.getInstance().get().getMarkerAPI().removeMarkerSet("towns");
                }
                BlueMapAPI.getInstance().get().getMarkerAPI().createMarkerSet("towns");

                for (BlueMapWorld world1 : BlueMapAPI.getInstance().get().getWorlds()) {
                    for (BlueMapMap map : world1.getMaps()) {
                        World world = Bukkit.getWorld(map.getWorld().getUuid());
                        if (world != null) {
                            TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(world.getName());

                            for (TownBlock townBlock : townyWorld.getTownBlocks()) {
                                double xvalue = townBlock.getCoord().getX() * 16;
                                double zvalue = townBlock.getCoord().getZ() * 16;
                                int y = config.getInt("y-height");

                                // shape
                                Shape shape = Shape.createRect(xvalue, zvalue, xvalue + 16, zvalue + 16);
                                ShapeMarker marker = set.createShapeMarker(townBlock.getTown().getName() + "_" + xvalue / 16 + "_" + zvalue / 16, map, shape, y);
                                marker.setLabel(getHTMLforTown(townBlock.getTown()));
                                marker.setMaxDistance(3500);

                                // color
                                if (townBlock.getTown().hasNation()) {
                                    marker.setFillColor(nationcolor);
                                } else {
                                    marker.setFillColor(towncolor);
                                }
                                marker.setBorderColor(marker.getFillColor());

                                // homeblock marker
                                if (townBlock.isHomeBlock()) {
                                    Vector3d vector = new Vector3d(xvalue + 8, y + config.getInt("marker-offset"), zvalue + 8);
                                    POIMarker home = set.createPOIMarker(townBlock.getTown().getName() + "_" + xvalue / 16 + "_" + zvalue / 16 + "_icon", map, vector);
                                    home.setLabel(getHTMLforTown(townBlock.getTown()));
                                    home.setIcon(config.getString("home-marker"), home.getIconAnchor());
                                    home.setMaxDistance(config.getInt("max-distance"));

                                    if (townBlock.getTown().isCapital()) {
                                        home.setIcon(config.getString("capital-marker"), home.getIconAnchor());
                                    }
                                }
                            }
                            if (TownyBlue.api != null) {
                                TownyBlue.api.save();
                            }
                        }
                    }
                }

            }
        } catch (NotRegisteredException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void Update() {
        if (TownyBlue.api != null && BlueMapAPI.getInstance().isPresent()) {
            MarkerAPI api = TownyBlue.api;
            try {
                api.load();
                if (api.getMarkerSets() != null) {
                    for (MarkerSet set : api.getMarkerSets()) {
                        if (set.getId().equals("towns"))
                            api.removeMarkerSet(set);
                    }
                }
                MarkerSet set = api.createMarkerSet("towns");

                CompleteUpdate(set);
            } catch (IOException e) {e.printStackTrace();}

        }
    }

    public static String getHTMLforTown(Town town) {
        String Html = config.getString("html");
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
            Html = Html.replace("%residentcount%", String.valueOf(town.getResidents().toArray().length));
            // add extra placeholders here, just Html = Html.replace("%yourplaceholder%", thing);

        } else {Html = "";}

        return Html;
    }

    private static String getResidents(Town town) {
        String result = "";
        StringBuilder resultBuilder = new StringBuilder(result);
        resultBuilder.append(town.getMayor().getName());
        resultBuilder.append(", ");

        int n = 0;
        for (Resident resident : town.getResidents()) {
            if (resident.getName() != null && !resident.isMayor()) {
                if (n != 0 && n % 5 == 0) {
                    resultBuilder.append("<br>");
                } else {resultBuilder.append(", ");}
                resultBuilder.append(resident.getName());
                n = n + 1;
            }
            result = resultBuilder.toString();
        }

        return result;
    }
}
/*
*
* */
