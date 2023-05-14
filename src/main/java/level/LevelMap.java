package level;

import org.json.JSONObject;
import level.tile.Tile;
import util.resources.ResourcesReader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Stores tile layers for a level of the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class LevelMap {
    // Key: tile layer name (for example, "walls").
    // Value: an instance of a TileLayer class, which stores the array of tile numbers.
    private final Map<String, TileLayer> tileLayers = new HashMap<>();

    /**
     * Instantiates a new Level map and set it up.
     *
     * @param levelId    the level id
     * @param mapId      the map id
     * @param tileWidth  the tile's width
     * @param tileHeight the tile's height
     * @param mapManager the map manager
     */
    LevelMap(String levelId, String mapId, int tileWidth, int tileHeight, MapManager mapManager) {
        JSONObject mapData = new ResourcesReader().readJSON("levels/" + levelId + "/maps/" + mapId + ".json");
        JSONObject mapTileLayers = mapData.getJSONObject("tileLayers");

        for (String tileLayerName : mapTileLayers.keySet()) {
            tileLayers.put(tileLayerName, new TileLayer(tileLayerName, mapTileLayers.getJSONObject(tileLayerName),
                    tileWidth, tileHeight, mapManager));
        }
    }

    /**
     * Update the tiles in every layer.
     *
     * @param deltaTime the delta time
     */
    public void update(double deltaTime) {
        for (TileLayer tileLayer : tileLayers.values()) {
            tileLayer.update(deltaTime);
        }
    }

    private Collection<TileLayer> getTileLayers() {
        return tileLayers.values();
    }

    /**
     * Draw.
     *
     * @param graphics2D   the graphics 2 d
     * @param debugMode    the debug mode of a tile layer
     * @param deltaTime    the delta time
     * @param transparency the transparency of a tile layer
     */
    public void draw(Graphics2D graphics2D, boolean debugMode, double deltaTime, float transparency) {
        // Sort the tile layers such that the tile layers are drawn in the correct order.
        List<TileLayer> tileLayersInDrawOrder = new ArrayList<>(getTileLayers());
        tileLayersInDrawOrder.sort(Comparator.comparingInt(TileLayer::getPriority));

        // Draw each tile layer in order.
        for (TileLayer tileLayer : tileLayersInDrawOrder) {
            tileLayer.draw(graphics2D, debugMode, deltaTime, transparency);
        }
    };

    /**
     * Gets overlapping tiles.
     *
     * @param enclosingRectangle the enclosing rectangle of a tile
     * @return the overlapping tiles
     */
    public List<Tile<?>> getOverlappingTiles(Rectangle2D.Double enclosingRectangle) {
        List<Tile<?>> overlappingTiles = new ArrayList<>();

        for (TileLayer tileLayer : tileLayers.values()) {
            overlappingTiles.addAll(tileLayer.getOverlappingTiles(enclosingRectangle));
        }

        return overlappingTiles;
    }
}
