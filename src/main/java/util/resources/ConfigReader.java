package util.resources;

import level.MapManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * Reads in all config data for the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class ConfigReader {
    private static final JSONObject appConfig;

    private static final JSONObject gameConfig;

    private static final JSONObject drawConfig;
    private static final JSONObject drawConfigDebugModeHitbox;
    private static final JSONObject drawConfigDebugModeVector;
    private static final Map<String, Color> drawConfigDebugModeColorsHitboxProfiles;
    private static final Color drawConfigDebugModeColorsVelocity;
    private static final Color drawConfigDebugModeColorsAcceleration;
    private static final Color drawConfigDebugModeColorsMaxVelocity;

    private static final JSONObject entitySpriteConfig;
    private static final JSONObject entitySpriteConfigAnimation;

    private static final JSONObject entityPlayerConfig;
    private static final JSONObject entityPlayerConfigPhysics;

    private static final JSONObject entityEnemyConfig;
    private static final JSONObject entityEnemyConfigPhysics;

    private static final JSONObject entityBossConfig;
    private static final JSONObject entityBossConfigPhysics;

    private static final Map<MapManager.TilesetType, List<Integer>> noHitboxTiles = new HashMap<>();
    private static final Map<MapManager.TilesetType, List<Integer>> fullHitboxTiles = new HashMap<>();
    private static final Map<MapManager.TilesetType,
            Map<Integer, List<Rectangle2D.Double>>> customTileHitboxes = new HashMap<>();

    private static final Map<MapManager.TilesetType, Integer> tileSizes = new HashMap<>();

    private static final JSONObject levelsConfig;

    private static final Map<String, Integer> levelsConfigTileLayerPriorities = new HashMap<>();

    static {
        ResourcesReader reader = new ResourcesReader();

        // Read in data from various JSON files.

        appConfig = reader.readJSON("config/app.json");

        gameConfig = reader.readJSON("config/game.json");

        drawConfig = reader.readJSON("config/draw.json");
        JSONObject drawConfigDebugMode = drawConfig.getJSONObject("debugMode");
        drawConfigDebugModeHitbox = drawConfigDebugMode.getJSONObject("hitbox");
        drawConfigDebugModeVector = drawConfigDebugMode.getJSONObject("vector");
        JSONObject drawConfigDebugModeColors = drawConfigDebugMode.getJSONObject("colors");
        JSONObject drawConfigDebugModeColorsHitbox = drawConfigDebugModeColors.getJSONObject("hitbox");
        drawConfigDebugModeColorsHitboxProfiles = new HashMap<>();
        for (String drawConfigDebugModeColorsHitboxProfileName : drawConfigDebugModeColorsHitbox.keySet()) {
            JSONObject RGBComponents = drawConfigDebugModeColorsHitbox.getJSONObject(
                    drawConfigDebugModeColorsHitboxProfileName);
            drawConfigDebugModeColorsHitboxProfiles.put(drawConfigDebugModeColorsHitboxProfileName,
                    readColor(RGBComponents));
        }
        drawConfigDebugModeColorsVelocity = readColor(drawConfigDebugModeColors.getJSONObject("velocity"));
        drawConfigDebugModeColorsAcceleration = readColor(drawConfigDebugModeColors.getJSONObject("acceleration"));
        drawConfigDebugModeColorsMaxVelocity = readColor(drawConfigDebugModeColors.getJSONObject("maxVelocity"));

        entitySpriteConfig = reader.readJSON("config/entity/sprite.json");
        entitySpriteConfigAnimation = entitySpriteConfig.getJSONObject("animation");

        entityPlayerConfig = reader.readJSON("config/entity/player.json");
        entityPlayerConfigPhysics = entityPlayerConfig.getJSONObject("physics");

        entityEnemyConfig = reader.readJSON("config/entity/enemy.json");
        entityEnemyConfigPhysics = entityEnemyConfig.getJSONObject("physics");

        entityBossConfig = reader.readJSON("config/entity/enemy.json");
        entityBossConfigPhysics = entityEnemyConfig.getJSONObject("physics");

        Map<MapManager.TilesetType, JSONObject> tilesetTileHitboxes = new HashMap<>();
        JSONObject dungeonTilesetTileHitboxes = reader.readJSON("tilesets/dungeon/tile_hitboxes.json");
        tilesetTileHitboxes.put(MapManager.TilesetType.DUNGEON, dungeonTilesetTileHitboxes);

        // Get the hitboxes of tiles.
        for (Map.Entry<MapManager.TilesetType, JSONObject> tilesetTileHitboxesEntry : tilesetTileHitboxes.entrySet()) {
            MapManager.TilesetType tilesetType = tilesetTileHitboxesEntry.getKey();

            JSONObject tileHitboxes = tilesetTileHitboxesEntry.getValue();

            // Get the data for tiles that have no hitbox.
            JSONArray noneTileHitboxesData = tileHitboxes.getJSONArray("none");
            List<Integer> noneTileHitboxesTileNumbers = new ArrayList<>();
            for (int i = 0; i < noneTileHitboxesData.length(); i++) {
                noneTileHitboxesTileNumbers.add(noneTileHitboxesData.getInt(i));
            }
            noHitboxTiles.put(tilesetType, noneTileHitboxesTileNumbers);

            // Get the data for tiles that have their hitbox take up the entire tile.
            JSONArray fullTileHitboxesData = tileHitboxes.getJSONArray("full");
            List<Integer> fullTileHitboxesTileNumbers = new ArrayList<>();
            for (int i = 0; i < fullTileHitboxesData.length(); i++) {
                fullTileHitboxesTileNumbers.add(fullTileHitboxesData.getInt(i));
            }
            fullHitboxTiles.put(tilesetType, fullTileHitboxesTileNumbers);

            // Get the data for tiles that have their hitbox take up part of a tile.
            Map<Integer, List<Rectangle2D.Double>> tilesetCustomTileHitboxes = new HashMap<>();
            JSONObject customTileHitboxesData = tileHitboxes.getJSONObject("custom");
            for (String tileNumber : customTileHitboxesData.keySet()) {
                List<Rectangle2D.Double> customTileHitboxesForTile = new ArrayList<>();

                JSONArray tileHitboxData = customTileHitboxesData.getJSONArray(tileNumber);
                for (int i = 0; i < tileHitboxData.length(); i++) {
                    customTileHitboxesForTile.add(new Rectangle2D.Double(
                            tileHitboxData.getJSONArray(i).getDouble(0),
                            tileHitboxData.getJSONArray(i).getDouble(1),
                            tileHitboxData.getJSONArray(i).getDouble(2),
                            tileHitboxData.getJSONArray(i).getDouble(3)));
                }
                tilesetCustomTileHitboxes.put(Integer.valueOf(tileNumber), customTileHitboxesForTile);
            }
            customTileHitboxes.put(tilesetType, tilesetCustomTileHitboxes);
        }

        // Get tileset config data.
        JSONObject tilesetConfig = reader.readJSON("config/tileset.json");
        JSONObject tilesetConfigTileSize = tilesetConfig.getJSONObject("tileSize");
        for (String tilesetName : tilesetConfigTileSize.keySet()) {
            tileSizes.put(MapManager.getTilesetType(tilesetName), tilesetConfigTileSize.getInt(tilesetName));
        }

        // Get level config data.
        levelsConfig = reader.readJSON("levels/levels.json");
        JSONObject levelsConfigTileLayerPrioritiesData = levelsConfig.getJSONObject("tileLayerPriority");
        for (String tileLayerName : levelsConfigTileLayerPrioritiesData.keySet()) {
            levelsConfigTileLayerPriorities.put(tileLayerName,
                    levelsConfigTileLayerPrioritiesData.getInt(tileLayerName));
        }
    }

    private static Color readColor(JSONObject RGBColor) {
        return new Color(
                RGBColor.getInt("r"),
                RGBColor.getInt("g"),
                RGBColor.getInt("b"));
    }

    /**
     * Gets the game title.
     *
     * @return the game title
     */
    public static String getGameTitle() {
        return appConfig.getString("gameTitle");
    }

    /**
     * Gets the screen tile width.
     *
     * @return the screen tile width
     */
    public static int getScreenTileWidth() {
        return gameConfig.getInt("screenTileWidth");
    }

    /**
     * Gets the screen tile height.
     *
     * @return the screen tile height
     */
    public static int getScreenTileHeight() {
        return gameConfig.getInt("screenTileHeight");
    }

    /**
     * Gets the asset scale.
     *
     * @return the asset scale
     */
    public static double getAssetScale() {
        return gameConfig.getDouble("assetScale");
    }

    /**
     * Gets the FPS to use for the game.
     *
     * @return the target FPS the game should try to use
     */
    public static double getFPS() {
        return gameConfig.getInt("FPS");
    }

    /**
     * Gets the thickness of the debug mode hitbox.
     *
     * @return the hitbox's thickness
     */
    public static float getDebugModeHitboxThickness() {
        return drawConfigDebugModeHitbox.getFloat("thickness");
    }

    /**
     * Gets the opacity of the debug mode hitbox.
     *
     * @return the hitbox's opacity
     */
    public static float getDebugModeHitboxOpacity() {
        return drawConfigDebugModeHitbox.getFloat("opacity");
    }

    /**
     * Gets the color of debug mode hitbox.
     *
     * @param profileName the profile name of an entity
     * @return the color of hitbox
     */
    public static Color getDebugModeHitboxColor(String profileName) {
        return drawConfigDebugModeColorsHitboxProfiles.get(profileName);
    }

    /**
     * Gets the thickness for debug mode vectors.
     *
     * @return the thickness to draw vectors at.
     */
    public static float getDebugModeVectorThickness() {
        return drawConfigDebugModeVector.getFloat("thickness");
    }

    /**
     * Gets the color of debug mode velocity vectors.
     *
     * @return the color of the line of velocity
     */
    public static Color getDebugModeVelocityColor() {
        return drawConfigDebugModeColorsVelocity;
    }

    /**
     * Gets the color debug mode acceleration vectors.
     *
     * @return the color of the line of acceleration
     */
    public static Color getDebugModeAccelerationColor() {
        return drawConfigDebugModeColorsAcceleration;
    }

    /**
     * Gets the color of debug mode max velocity vectors.
     *
     * @return the color of the line of max velocity
     */
    public static Color getDebugModeMaxVelocityColor() {
        return drawConfigDebugModeColorsMaxVelocity;
    }

    /**
     * Gets the FPS of all animations.
     *
     * @return the FPS of all animations.
     */
    public static double getAnimationFramesPerSecond() {
        return entitySpriteConfigAnimation.getDouble("animationFramesPerSecond");
    }

    /**
     * Gets the player's acceleration.
     *
     * @return the player's acceleration
     */
    public static double getPlayerAcceleration() {
        return entityPlayerConfigPhysics.getDouble("acceleration");
    }

    /**
     * Gets the player's friction.
     *
     * @return the player's friction
     */
    public static double getPlayerFriction() {
        return entityPlayerConfigPhysics.getDouble("friction");
    }

    /**
     * Gets the player's max velocity.
     *
     * @return the player's max velocity
     */
    public static double getPlayerMaxVelocity() {
        return entityPlayerConfigPhysics.getDouble("maxVelocity");
    }

    /**
     * Gets the player's min velocity.
     *
     * @return the player's min velocity
     */
    public static double getPlayerMinVelocity() {
        return entityPlayerConfigPhysics.getDouble("minVelocity");
    }

    /**
     * Gets the enemy's acceleration.
     *
     * @return the enemy's acceleration
     */
    public static double getEnemyAcceleration() {
        return entityEnemyConfigPhysics.getDouble("acceleration");
    }

    /**
     * Gets the enemy's friction.
     *
     * @return the enemy's friction
     */
    public static double getEnemyFriction() {
        return entityEnemyConfigPhysics.getDouble("friction");
    }

    /**
     * Gets the enemy's max velocity.
     *
     * @return the enemy's max velocity
     */
    public static double getEnemyMaxVelocity() {
        return entityEnemyConfigPhysics.getDouble("maxVelocity");
    }

    /**
     * Gets the enemy's min velocity.
     *
     * @return the enemy's min velocity
     */
    public static double getEnemyMinVelocity() {
        return entityEnemyConfigPhysics.getDouble("minVelocity");
    }

    /**
     * Gets the enemy's tracking distance.
     *
     * @return the enemy's tracking distance
     */
    public static double getEnemyTrackingDistance() {
        return entityEnemyConfig.getDouble("trackingDistance");
    }

    private static List<Integer> getNoHitboxTiles(MapManager.TilesetType tilesetType) {
        return noHitboxTiles.get(tilesetType);
    }
    private static List<Integer> getFullHitboxTiles(MapManager.TilesetType tilesetType) {
        return fullHitboxTiles.get(tilesetType);
    }
    private static Set<Integer> getCustomHitboxTiles(MapManager.TilesetType tilesetType) {
        return customTileHitboxes.get(tilesetType).keySet();
    }

    private static List<Rectangle2D.Double> getCustomTileHitbox(MapManager.TilesetType tilesetType, int tileNumber) {
        return customTileHitboxes.get(tilesetType).get(tileNumber);
    }

    /**
     * Gets a list of the tiles in numbers.
     *
     * @param tilesetType the tileset type
     * @return the list of tiles
     */
    public static List<Integer> getTileNumbers(MapManager.TilesetType tilesetType) {
        List<Integer> tileNumbers = new ArrayList<>();

        tileNumbers.addAll(getNoHitboxTiles(tilesetType));
        tileNumbers.addAll(getFullHitboxTiles(tilesetType));
        tileNumbers.addAll(getCustomHitboxTiles(tilesetType));

        return tileNumbers;
    }

    /**
     * Gets tile size.
     *
     * @param tilesetType the tileset type
     * @return the tile's size
     */
    public static int getTileSize(MapManager.TilesetType tilesetType) {
        return tileSizes.get(tilesetType);
    }

    /**
     * Gets the tile relative hitbox.
     *
     * @param tilesetType the tileset type
     * @param tileNumber  the tile number
     * @return the tile's relative hitbox
     */
    public static List<Rectangle2D.Double> getTileRelativeHitbox(MapManager.TilesetType tilesetType, int tileNumber) {
        List<Rectangle2D.Double> tileRelativeHitboxes = new ArrayList<>();

        double assetScale = getAssetScale();

        if (getNoHitboxTiles(tilesetType).contains(tileNumber)) {
            tileRelativeHitboxes.add(new Rectangle2D.Double(0, 0, 0, 0));
        } else if (getFullHitboxTiles(tilesetType).contains(tileNumber)) {
            int tileSize = getTileSize(tilesetType);
            tileRelativeHitboxes.add(new Rectangle2D.Double(0, 0, tileSize * assetScale, tileSize * assetScale));
        } else {
            List<Rectangle2D.Double> tileHitboxes = getCustomTileHitbox(tilesetType, tileNumber);

            for (Rectangle2D.Double hitbox : tileHitboxes) {
                tileRelativeHitboxes.add(new Rectangle2D.Double(
                        hitbox.getX() * assetScale,
                        hitbox.getY() * assetScale,
                        hitbox.getWidth() * assetScale,
                        hitbox.getHeight() * assetScale));
            }
        }

        return tileRelativeHitboxes;
    }

    /**
     * Gets the number of levels.
     *
     * @return the number of levels.
     */
    public static int getLevelMaxNumber() {
        return levelsConfig.getInt("levelMaxNumber");
    }

    /**
     * Gets tile's layer priority.
     * <p>
     * Layers with a higher priority will be on top of layers with a smaller priority.
     *
     * @param tileLayerName the tile layer name
     * @return the tile's layer priority value
     */
    public static int getTileLayerPriority(String tileLayerName) {
        return levelsConfigTileLayerPriorities.get(tileLayerName);
    }
}
