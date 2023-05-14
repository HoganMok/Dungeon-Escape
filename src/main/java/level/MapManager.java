package level;

import app.game.Game;
import entity.movable.Enemy;
import audio.AudioManager;
import level.tile.Tile;
import tileset.DungeonTileset;
import tileset.Tileset;
import util.resources.ConfigReader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Manages levels and maps.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class MapManager {
    /**
     * The enum Tileset type.
     */
    public enum TilesetType {
        /**
         * Dungeon tileset type.
         */
        DUNGEON
    }

    private int currentLevelIndex = 0;

    private final double assetScale = ConfigReader.getAssetScale();

    /**
     * The Tilesets.
     */
    Map<TilesetType, Tileset> tilesets = new HashMap<>();
    private final DungeonTileset mainTileset;

    private final List<Level> levels = new ArrayList<>();
    private final Map<String, Integer> levelIdToLevelIndex = new HashMap<>();

    private final Game game;

    private boolean previewingFlip = false;
    private final double flipFailedPreviewTotalDuration = 0.5;
    private double flipFailedPreviewDuration = 0;

    /**
     * Instantiates a new Map manager to manage the map.
     *
     * @param game the game
     */
    public MapManager(Game game) {
        mainTileset = new DungeonTileset(game, "png", assetScale);

        tilesets.put(TilesetType.DUNGEON, mainTileset);

        this.game = game;
    }

    /**
     * Reset level's index and map's indexes.
     */
    public void resetLevelIndexAndMapIndexes() {
        currentLevelIndex = 0;
        for (Level level : levels) {
            level.resetMapIndex();
        }
    }

    /**
     * Load all levels into the list of levels.
     */
    public void loadLevels() {
        int levelAmount = ConfigReader.getLevelMaxNumber();
        for (int i = 0; i <= levelAmount; i++) {
            loadLevel("level" + i);
        }
    }

    /**
     * Load a level into the list of levels.
     */
    public void loadLevel(String levelId) {
        levels.add(new Level(game, levelId));
        levelIdToLevelIndex.put(levelId, levels.size() - 1);
    }

    /**
     * Gets tileset by its type.
     *
     * @param tilesetType the tileset type
     * @return the tileset
     */
    public Tileset getTileset(TilesetType tilesetType) {
        return tilesets.get(tilesetType);
    }

    /**
     * Gets tileset by its name.
     *
     * @param tilesetName the tileset name
     * @return the tileset
     */
    public Tileset getTileset(String tilesetName) {
        return getTileset(getTilesetType(tilesetName));
    }

    /**
     * Gets tileset type.
     *
     * @param tilesetName the tileset name
     * @return the tileset type
     */
    public static TilesetType getTilesetType(String tilesetName) {
        return switch(tilesetName) {
            case "dungeon" -> TilesetType.DUNGEON;
            default -> null;
        };
    }

    /**
     * Gets name of a tileset.
     *
     * @param tilesetType the tileset type
     * @return the name of a tileset
     */
    public static String getTilesetName(TilesetType tilesetType) {
        return switch(tilesetType) {
            case DUNGEON -> "dungeon";
        };
    }

    /**
     * Gets asset scale.
     *
     * @return the asset scale
     */
    public double getAssetScale() {
        return assetScale;
    }

    /**
     * Gets asset tile size.
     *
     * @return the asset tile size
     */
    public int getAssetTileSize() {
        return mainTileset.getAssetTileSize();
    }

    /**
     * Gets player's images.
     *
     * @param character the character
     * @return the player's images
     */
    public List<BufferedImage> getPlayerImages(DungeonTileset.Player character) {
        return mainTileset.getPlayerImages(character);
    }

    /**
     * Gets enemy's images.
     *
     * @param character the character
     * @return the enemy's images
     */
    public List<BufferedImage> getEnemyImages(DungeonTileset.Enemy character) {
        return mainTileset.getEnemyImages(character);
    }

    /**
     * Gets regular reward's images.
     *
     * @return the regular reward's images
     */
    public List<BufferedImage> getRegularRewardImages() {
        return mainTileset.getRegularRewardImages();
    }

    /**
     * Gets bonus reward's images.
     *
     * @return the bonus reward's images
     */
    public List<BufferedImage> getBonusRewardImages() {
        return mainTileset.getBonusRewardImages();
    }

    /**
     * Gets punishment's images.
     *
     * @return the punishment's images
     */
    public List<BufferedImage> getPunishmentImages() {
        return mainTileset.getPunishmentImages();
    }

    /**
     * Gets door's images.
     *
     * @param character the character
     * @return the door's images
     */
    public List<BufferedImage> getDoorImages(DungeonTileset.Door character) {
        return mainTileset.getDoorImages(character);
    }

    private Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    /**
     * Update the map.
     *
     * @param deltaTime the delta time
     */
    public void update(double deltaTime) {
        getCurrentLevel().update(deltaTime);

        if (previewingFlip) {
            flipFailedPreviewDuration += deltaTime;

            if (flipFailedPreviewDuration >= flipFailedPreviewTotalDuration) {
                previewingFlip = false;
            }
        }
    }

    /**
     * Draw the current level out.
     *
     * @param graphics2D the graphics 2 d
     * @param debugMode  the debug mode
     * @param deltaTime  the delta time
     */
    public void draw(Graphics2D graphics2D, boolean debugMode, double deltaTime) {
        getCurrentLevel().draw(graphics2D, debugMode, deltaTime, previewingFlip);
    }

    /**
     * Gets overlapping tiles from current level map.
     *
     * @param enclosingRectangle the enclosing rectangle
     * @return the overlapping tiles from current level map
     */
    public List<Tile<?>> getOverlappingTilesFromCurrentLevelMap(Rectangle2D.Double enclosingRectangle) {
        return getCurrentLevel().getOverlappingTilesFromCurrentLevelMap(enclosingRectangle);
    }

    /**
     * Gets overlapping tiles from next level map.
     *
     * @param enclosingRectangle the enclosing rectangle of a tile
     * @return the list of overlapping tiles from next level map
     */
    public List<Tile<?>> getOverlappingTilesFromNextLevelMap(Rectangle2D.Double enclosingRectangle) {
        return getCurrentLevel().getOverlappingTilesFromNextLevelMap(enclosingRectangle);
    }

    /**
     * Gets pixel tile size.
     *
     * @return the pixel tile size
     */
    public double getPixelTileSize() {
        return getAssetScale() * getAssetTileSize();
    }

    /**
     * On last level boolean or not.
     *
     * @return the boolean
     */
    public boolean onLastLevel() {
        return levels.size() - 1 == currentLevelIndex;
    }

    /**
     * Load current level entities.
     */
    public void loadCurrentLevelEntities(){
        levels.get(currentLevelIndex).loadCurrentLevel();
    }

    private void swapLevel() {
        game.destroyNonPreservedEntities();
    }

    /**
     * Start next level.
     */
    public void startNextLevel() {
        swapLevel();
        currentLevelIndex++;
        loadCurrentLevelEntities();
    }

    /**
     * Start a level.
     *
     * @param levelId the level Id of the level to swap to
     */
    public void switchToLevel(String levelId) {
        swapLevel();
        currentLevelIndex = levelIdToLevelIndex.get(levelId);
        loadCurrentLevelEntities();
    }

    private boolean canFlipToNextMap() {
        return game.canFlipToNextMap();
    }

    /**
     * Attempt to flip current level to next map.
     */
    public void attemptToFlipCurrentLevelToNextMap() {
        if (canFlipToNextMap()) {
            List<Integer> enemyIdsCopy = new ArrayList<>(game.getEnemySpriteIds());
            for (int spriteId : enemyIdsCopy) {
                ((Enemy) game.getEntity(spriteId)).respawnIfFlipOntoTile();
            }
            game.getAudioManager().playAudioOnce(AudioManager.AudioType.FLIP);
            getCurrentLevel().flipToNextMap();
        } else {
            game.getAudioManager().playAudioOnce(AudioManager.AudioType.FLIP_FAILED);
            previewingFlip = true;
            flipFailedPreviewDuration = 0;
        }
    }

    /**
     * Gets current level pixel width.
     *
     * @return the current level pixel width
     */
    public int getCurrentLevelPixelWidth() {
        return (int) (getCurrentLevel().getTileWidth() * getAssetTileSize() * assetScale);
    }

    /**
     * Gets current level pixel height.
     *
     * @return the current level pixel height
     */
    public int getCurrentLevelPixelHeight() {
        return (int) (getCurrentLevel().getTileHeight() * getAssetTileSize() * assetScale);
    }
}
