package level;

import org.json.JSONArray;
import org.json.JSONObject;
import level.tile.Tile;
import tileset.Tileset;
import util.resources.ConfigReader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.Map;

/**
 * Stores the tiles of a tile layer of a map of a level in the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class TileLayer {
    private final int tileWidth;
    private final int tileHeight;

    private final String name;

    private final List<Tile<?>> tiles = new ArrayList<>();
    private final List<Integer> updatableTileIndexes = new ArrayList<>();
    private final Map<Integer, Map<Integer, Integer>> tileLocations = new HashMap<>();

    private final int tileSize;

    /**
     * Instantiates a new Tile layer and set it up.
     *
     * @param tileLayerName the name of the tile layer
     * @param tileLayerData the data of tile layer
     * @param tileWidth     the tile's width
     * @param tileHeight    the tile's height
     * @param mapManager    the map manager
     */
    public TileLayer(String tileLayerName, JSONObject tileLayerData, int tileWidth, int tileHeight,
                     MapManager mapManager) {
        this.name = tileLayerName;

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        Tileset tileset = mapManager.getTileset(tileLayerData.getString("tileset"));

        tileSize = tileset.getScaledTileSize();

        JSONArray tileData = tileLayerData.getJSONArray("tiles");
        for (int i = 0; i < tileData.length(); i++) {
            int tileNumber = tileData.getInt(i);

            // Tile number -1 represents the absence of a tile.
            if (tileNumber != -1) {
                Point2D.Double tileCoordinates = getTileCoordinates(i);
                Tile<?> tile = tileset.createTile(tileNumber, tileCoordinates);

                tiles.add(tile);

                int tileIndex = tiles.size() - 1;

                // Add the tile to the map from tile coordinate to tile.
                int tileX = (int) tileCoordinates.getX();
                int tileY = (int) tileCoordinates.getY();
                if (!tileLocations.containsKey(tileX)) {
                    Map<Integer, Integer> tileYToTilesIndex = new HashMap<>();
                    tileLocations.put(tileX, tileYToTilesIndex);
                }
                tileLocations.get(tileX).put(tileY, tileIndex);

                if (tile.isUpdatable()) {
                    updatableTileIndexes.add(tileIndex);
                }
            }
        }
    }

    private Point2D.Double getTileCoordinates(int tileIndex) {
        return new Point2D.Double(tileIndex % tileWidth, tileIndex / tileWidth);
    }


    /**
     * Gets tile from specific tile coordinates.
     *
     * @param tileX the x coordinate of the tile
     * @param tileY the y coordinate of the tile
     * @return the tile
     */
    public Tile<?> getTileFromTileCoordinates(int tileX, int tileY) {
        Map<Integer, Integer> tileYToTilesIndex = tileLocations.get(tileX);
        if (tileYToTilesIndex == null) {
            return null;
        }

        Integer tileIndex = tileYToTilesIndex.get(tileY);
        if (tileIndex == null) {
            return null;
        }

        return tiles.get(tileIndex);
    }

    /**
     * Gets tile from specific pixel coordinates.
     *
     * @param pixelX the x pixel coordinates of the tile
     * @param pixelY the y pixel coordinates of the tile
     * @return the tile
     */
    public Tile<?> getTileFromPixelCoordinates(double pixelX, double pixelY) {
        return getTileFromTileCoordinates(getTileXFromPixelX(pixelX), getTileYFromPixelY(pixelY));
    }

    /**
     * Update the tiles.
     *
     * @param deltaTime the delta time
     */
    public void update(double deltaTime) {
        for (int tileIndex : updatableTileIndexes) {
            tiles.get(tileIndex).update(deltaTime);
        }
    }

    /**
     * Draw the tiles.
     *
     * @param graphics2D   the graphics 2 d
     * @param debugMode    the debug mode of the tiles
     * @param deltaTime    the delta time
     * @param transparency the transparency of the tiles
     */
    public void draw(Graphics2D graphics2D, boolean debugMode, double deltaTime, float transparency) {
        for (Tile<?> tile : tiles) {
            tile.draw(graphics2D, debugMode, deltaTime, transparency);
        }
    };

    /**
     * Gets priority of layer.
     *
     * @return the priority
     */
    public int getPriority() {
        return ConfigReader.getTileLayerPriority(name);
    }

    private int getTileXFromPixelX(double x) {
        return (int) (x / tileSize);
    }
    private int getTileYFromPixelY(double y) {
        return (int) (y / tileSize);
    }

    /**
     * Gets points along the edge of a rectangle to check for overlapping tiles.
     * <p>
     * Check from the coordinate start to the coordinate end, inclusive, skipping by tileSize. Note that the coordinates
     * are one-dimensional.
     *
     * @param start the starting coordinate
     * @param end   the ending coordinate
     * @return a list of one-dimensional coordinates to check for overlapping tiles
     */

    private List<Double> getTileOverlappingCheckPixelCoordinates(double start, double end) {
        List<Double> coordinates = new ArrayList<>();
        double coordinate = start;
        for (; coordinate <= end; coordinate += tileSize) {
            coordinates.add(coordinate);
        }
        if (coordinate - tileSize != end) {
            coordinates.add(end);
        }

        return coordinates;
    }

    /**
     * Gets overlapping tiles.
     *
     * @param enclosingRectangle the enclosing rectangle of a tile
     * @return the overlapping tiles
     */
    public List<Tile<?>> getOverlappingTiles(Rectangle2D.Double enclosingRectangle) {
        List<Tile<?>> overlappingTiles = new ArrayList<>();

        double topLeftX = enclosingRectangle.getX();
        double topRightX = topLeftX + enclosingRectangle.getWidth();

        double topLeftY = enclosingRectangle.getY();
        double bottomLeftY = topLeftY + enclosingRectangle.getHeight();

        // Get all tiles that are at least partially overlapped by a given rectangle.
        Set<Point2D.Double> overlappingTileCoordinates = new HashSet<>();
        for (double pixelY : getTileOverlappingCheckPixelCoordinates(topLeftY, bottomLeftY)) {
            for (double pixelX : getTileOverlappingCheckPixelCoordinates(topLeftX, topRightX)) {
                Tile<?> tile = getTileFromPixelCoordinates(pixelX, pixelY);
                if (tile != null) {
                    Point2D.Double tileCoordinate = new Point2D.Double(getTileXFromPixelX(pixelX),
                            getTileYFromPixelY(pixelY));
                    if (!overlappingTileCoordinates.contains(tileCoordinate)) {
                        overlappingTileCoordinates.add(tileCoordinate);
                        overlappingTiles.add(tile);
                    }
                }
            }
        }

        return overlappingTiles;
    }
}
