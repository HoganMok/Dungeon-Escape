package level;

import app.game.Game;

import entity.factory.*;
import tileset.DungeonTileset;

import org.json.JSONArray;
import org.json.JSONObject;
import level.tile.Tile;
import util.resources.ResourcesReader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Stores all data for a game level.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class Level {
    private final String name;
    private final double difficulty;
    private final int tileWidth;
    private final int tileHeight;
    private final Game game;
    private int currentLevelMapLevelIndex = 0;
    private final String levelId;

    /**
     * The Level maps.
     */
    List<LevelMap> levelMaps = new ArrayList<>();
    private final MapManager mapManager;

    /**
     * Instantiating a new Level and reading the json file under that level.
     *
     * @param game    the game
     * @param levelId the level id of the new level
     */
    public Level(Game game, String levelId) {
        this.game = game;
        mapManager = game.getMapManager();

        JSONObject levelData = new ResourcesReader().readJSON("levels/" + levelId + "/level.json");

        name = levelData.getString("name");
        difficulty = levelData.getDouble("difficulty");
        tileWidth = levelData.getInt("tileWidth");
        tileHeight = levelData.getInt("tileHeight");

        this.levelId = levelId;

        List<String> mapIds = new ArrayList<>();
        JSONArray mapOrderData = levelData.getJSONArray("maps");
        for (int i = 0; i < mapOrderData.length(); i++) {
            mapIds.add(mapOrderData.getString(i));
        }

        loadMaps(levelId, mapIds, mapManager);
    }

    /**
     * Resetting the map index to be zero, so, the players will spawn in map0 for every level
     */
    public void resetMapIndex() {
        currentLevelMapLevelIndex = 0;
    }

    private void loadMaps(String levelId, List<String> mapIds, MapManager mapManager) {
        for (String mapId : mapIds) {
            // Load in the maps for this level.
            levelMaps.add(new LevelMap(levelId, mapId, tileWidth, tileHeight, mapManager));
        }
    }

    /**
     * Details for levels are stored in level.json under each level. It read the numbers and the spawn location of
     * different entities, and calling createEntities to create the entities in the right spawning locations.
     */
    public void loadCurrentLevel() {
        JSONObject levelData = new ResourcesReader().readJSON("levels/" + levelId + "/level.json");
        JSONObject entities = levelData.getJSONObject("entities");
        JSONObject items = entities.getJSONObject("items");
        RegularRewardFactory regularRewardFactory = new RegularRewardFactory(game);
        BonusRewardFactory bonusRewardFactory = new BonusRewardFactory(game);
        PunishmentFactory punishmentFactory = new PunishmentFactory(game);
        loadEntityArray("regularReward", items, regularRewardFactory);
        loadEntityArray("bonusReward", items, bonusRewardFactory);
        loadEntityArray("punishment", items, punishmentFactory);

        JSONObject enemies = entities.getJSONObject("enemies");
        for (String enemyName : enemies.keySet()) {
            JSONArray enemy = enemies.getJSONArray(enemyName);
            List<List<Double>> enemyArray = new ArrayList<>();
            for (int i = 0; i < enemy.length(); i++) {
                JSONArray array = enemy.getJSONArray(i);
                List<Double> enemy1 = new ArrayList<>();
                for(int j = 0; j < array.length(); j++) {
                    enemy1.add(array.getDouble(j));
                }
                enemyArray.add(enemy1);
            }
            DungeonTileset.Enemy enemyType = switch (enemyName.toUpperCase()){
                case "BEIGE_SQUARE" -> DungeonTileset.Enemy.BEIGE_SQUARE;
                case "GREEN_SQUARE" -> DungeonTileset.Enemy.GREEN_SQUARE;
                case "RED_SQUARE" -> DungeonTileset.Enemy.RED_SQUARE;
                case "BROWN_SLUDGE" -> DungeonTileset.Enemy.BROWN_SLUDGE;
                case "GREEN_SLUDGE" -> DungeonTileset.Enemy.GREEN_SLUDGE;
                case "BEIGE_WALKER" -> DungeonTileset.Enemy.BEIGE_WALKER;
                case "CYAN_WALKER" -> DungeonTileset.Enemy.CYAN_WALKER;
                case "SKULL_MASK" -> DungeonTileset.Enemy.SKULL_MASK;
                case "ORC_1" -> DungeonTileset.Enemy.ORC_1;
                case "ORC_2" -> DungeonTileset.Enemy.ORC_2;
                case "SORCERER" -> DungeonTileset.Enemy.SORCERER;
                case "TERROR_1" -> DungeonTileset.Enemy.TERROR_1;
                case "TERROR_2" -> DungeonTileset.Enemy.TERROR_2;
                case "ANGRY_TOOTH_CHEST" -> DungeonTileset.Enemy.ANGRY_TOOTH_CHEST;
                case "ZOMBIE" -> DungeonTileset.Enemy.ZOMBIE;
                case "ORC" -> DungeonTileset.Enemy.ORC;
                case "TERROR" -> DungeonTileset.Enemy.TERROR;
                default -> DungeonTileset.Enemy.SKELETON;
            };
            for (List<Double> doubles : enemyArray) {
                List<Double> positionArray;
                positionArray = doubles;
                double positionX = positionArray.get(0);
                double positionY = positionArray.get(1);
                createEntity(positionX, positionY, new EnemyFactory(game, enemyType));
            }
        }

        JSONArray door = levelData.getJSONArray("door");
        List<Double> doorArray = new ArrayList<>();
        for (int i = 0; i < door.length(); i++) {
            doorArray.add(door.getDouble(i));
        }
        double positionX = doorArray.get(0);
        double positionY = doorArray.get(1);
        createEntity(positionX, positionY - 1, new DoorFactory(game, DungeonTileset.Door.DOOR_1));

        JSONArray player = levelData.getJSONArray("player");
        List<Double> playerArray = new ArrayList<>();
        for (int i = 0; i < player.length(); i++) {
            playerArray.add(player.getDouble(i));
        }
        double PositionX = mapManager.getPixelTileSize() * playerArray.get(0);
        double PositionY = mapManager.getPixelTileSize() *playerArray.get(1);
        Point2D.Double position = new Point2D.Double(PositionX,PositionY);
        game.getPlayer().setPosition(position);
    }

    /**
     * Getting the name of the level.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getting tile's width.
     *
     * @return the tile width
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * Getting tile's height.
     *
     * @return the tile height
     */
    public int getTileHeight() {
        return tileHeight;
    }

    private LevelMap getCurrentLevelMap() {
        return levelMaps.get(currentLevelMapLevelIndex);
    }

    private int getNextLevelMapIndex() {
        return (currentLevelMapLevelIndex + 1) % levelMaps.size();
    }

    private LevelMap getNextLevelMap() {
        return levelMaps.get(getNextLevelMapIndex());
    }

    /**
     * Updating the level.
     *
     * @param deltaTime the delta time
     */
    public void update(double deltaTime) {
        for (LevelMap levelMap : levelMaps) {
            levelMap.update(deltaTime);
        }
    }

    /**
     * Drawing the map that the player is currently playing, also, drawing the flipped map transparently while the
     * players are not allowed to flip
     *
     * @param graphics2D  the graphics to draw on
     * @param debugMode   the debug mode that shows the hit boxes
     * @param deltaTime   the delta time
     * @param previewFlip the preview flip to determine can the player flip on his/her current location
     */
    public void draw(Graphics2D graphics2D, boolean debugMode, double deltaTime, boolean previewFlip) {
        getCurrentLevelMap().draw(graphics2D, debugMode, deltaTime, 1f);
        if (previewFlip) {
            getNextLevelMap().draw(graphics2D, debugMode, deltaTime, 0.5f);
        }
    };


    /**
     * Gets overlapping tiles from current level map.
     *
     * @param enclosingRectangle the enclosing rectangle of an entity
     * @return the list of overlapping tiles from current map
     */
    public List<Tile<?>> getOverlappingTilesFromCurrentLevelMap(Rectangle2D.Double enclosingRectangle) {
        return getCurrentLevelMap().getOverlappingTiles(enclosingRectangle);
    }

    /**
     * Gets overlapping tiles from next level map.
     *
     * @param enclosingRectangle the enclosing rectangle of an entity
     * @return the list of overlapping tiles from next map
     */
    public List<Tile<?>> getOverlappingTilesFromNextLevelMap(Rectangle2D.Double enclosingRectangle) {
        return getNextLevelMap().getOverlappingTiles(enclosingRectangle);
    }

    private void loadEntityArray(String key, JSONObject object, EntityFactory entityFactory){
        JSONArray Entity = object.getJSONArray(key);
        List<List<Double>> EntityArray = new ArrayList<>();
        for (int i = 0; i < Entity.length(); i++) {
            JSONArray array = Entity.getJSONArray(i);
            List<Double> EntityArray1 = new ArrayList<>();
            for(int j = 0; j < array.length(); j++) {
                EntityArray1.add(array.getDouble(j));
            }
            EntityArray.add(EntityArray1);
        }
        for(int i = 0; i < Entity.length(); i++){
            List<Double> positionArray = EntityArray.get(i);
            double positionX = positionArray.get(0);
            double positionY = positionArray.get(1);
            createEntity(positionX, positionY, entityFactory);
        }
    }

    /**
     * Creates an entity at a given position using a given factory.
     *
     * @param x the tile x position of the entity
     * @param y the tile y position of the entity
     * @param entityFactory the entity factory to create a specific kind of entity
     */
    private void createEntity(double x, double y, EntityFactory entityFactory) {
        entityFactory.create(new Point2D.Double(mapManager.getPixelTileSize() * x, mapManager.getPixelTileSize() * y));
    }

    /**
     * Changing currentLevelMapLevelIndex in order to flip between map0 and map1
     */
    public void flipToNextMap() {
        currentLevelMapLevelIndex = getNextLevelMapIndex();
    }

}
