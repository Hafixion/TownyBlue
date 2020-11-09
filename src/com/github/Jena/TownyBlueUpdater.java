package com.github.Jena;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyWorld;
import com.sun.deploy.util.StringUtils;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.ShapeMarker;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.LinkedList;
import java.util.List;

public class TownyBlueUpdater {

    public static void CompleteUpdate(MarkerSet set) {
        if (BlueMapAPI.getInstance().isPresent()) {
            for (BlueMapWorld world1 : BlueMapAPI.getInstance().get().getWorlds()) {
                for (BlueMapMap map : world1.getMaps()) {
                    World world = Bukkit.getWorld(map.getWorld().getUuid());
                    TownyWorld townyWorld = TownyAPI.getInstance().getTownyWorld(world.getName());

                    for (Town town : townyWorld.getTowns().values()) {
                        for (TownBlock townBlock : town.getTownBlocks()) {
                            double xvalue = townBlock.getCoord().getX() * 16;
                            double zvalue = townBlock.getCoord().getZ() * 16;

                            Shape home = Shape.createRect(xvalue, zvalue, xvalue + 15, zvalue + 15);
                            ShapeMarker marker = set.createShapeMarker(town.getName(), map, home, TownyBlue.config.getInt("y-height"));
                            marker.setLabel(getHTMLforTown(town));
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getHTMLforTown(Town town) {
        String result = null;
        // get the main html string
        String Html = TownyBlue.config.getString("html");
        String stringresidents = getResidents(town);

        // placeholders
        Html.replace("%name%", town.getName());
        try {
            Html.replace("%nation%", town.getNation().getName());
        } catch (NotRegisteredException e) {
            e.printStackTrace();
            Html.replace("%nation%", "");
        }
        Html.replace("%mayor%", town.getMayor().getName());
        Html.replace("%residents%", stringresidents);

        if (Html != null) {result = Html;}

        return result;
    }

    private static String getResidents(Town town) {
        String result = null;
        List<String> list = new LinkedList<>();

        for (Resident resident : town.getResidents()) {
            list.add(resident.getName());
        }

        if (list.toArray().length >= 1) {result = StringUtils.join(list, ", ");}
        
        return result;
    }
}
