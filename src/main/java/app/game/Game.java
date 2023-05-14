package app.game;

import entity.Door;
import entity.movable.Player;
import entity.factory.PlayerFactory;
import entity.Entity;
import level.tile.Tile;
import sprite.draw.CameraManager;
import tileset.DungeonTileset;
import app.game.InputManager.InputName;
import app.game.InputManager.InputType;
import level.MapManager;
import audio.AudioManager;
import util.media.TextGenerator;
import util.physics.CollisionChecker;
import util.resources.ConfigReader;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The actual game. Handles game logic such as the update and draw methods.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @version 1.3
 */
public class Game extends JPanel implements Runnable {
    private final AudioManager audioManager;

    private Thread gameLoopThread;
    /**
     * The ThreeDventure font used for the game.
     */
    Font ThreeDventure;
    private InputManager inputManager;

    private final MapManager mapManager = new MapManager(this);

    private final CameraManager cameraManager = new CameraManager(this);

    private int gameScore, gameDurationMinutes;
    private double gameDurationSeconds;

    private DungeonTileset.Player playerType = DungeonTileset.Player.KNIGHT_ORANGE;
    // Map from a sprite's Id to an instantiated Sprite object.
    private final ConcurrentMap<Integer, Entity<?>> entities = new ConcurrentHashMap<>();
    private final List<Integer> entitiesToDelete = new ArrayList<>();
    // Store the Ids of entities that should not be destroyed when switching to a new level.
    private final Set<Integer> preservedSpriteIds = new HashSet<>();
    // Store the Ids of various entities.
    private int playerId;
    private final Set<Integer> doorIds = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final List<Integer> enemyIds = new ArrayList<>();
    private final List<Integer> regularRewardIds = new ArrayList<>();
    private final int screenTileWidth = ConfigReader.getScreenTileWidth();
    private final int screenTileHeight = ConfigReader.getScreenTileHeight();
    private final int screenPixelWidth = (int) (screenTileWidth * mapManager.getAssetTileSize() * mapManager.getAssetScale());
    private final int screenPixelHeight = (int) (screenTileHeight * mapManager.getAssetTileSize() * mapManager.getAssetScale());
    private final double FPS = ConfigReader.getFPS();
    private double currentFPS, currentDeltaTime;
    private boolean isRunning = false;
    private boolean debugMode = false;
    private boolean isEnteredDoor = false;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Instantiates a new Game.
     *
     * @param audioManager     an audio manager which has all sounds
     * @param font             the font to use for the game
     * @param gameOverDetector a listener that will take actions when the game is over
     */
    public Game(AudioManager audioManager, Font font, PropertyChangeListener gameOverDetector) {
        this.audioManager = audioManager;

        ThreeDventure = font;

        this.setPreferredSize(new Dimension(screenPixelWidth, screenPixelHeight));
        this.setBackground(new Color(34, 34, 34));
        this.setDoubleBuffered(true);

        this.setFocusable(true);

        propertyChangeSupport.addPropertyChangeListener(gameOverDetector);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /**
     * Start game loop to start the game until it breaks.
     */
    public void startGameLoop() {
        // Remove any game data.
        entities.clear();
        entitiesToDelete.clear();
        preservedSpriteIds.clear();
        enemyIds.clear();
        regularRewardIds.clear();
        doorIds.clear();

        // Reset the game score and time.
        gameScore = 0;
        gameDurationMinutes = 0;
        gameDurationSeconds = 0;

        gameLoopThread = new Thread(this);

        // Create a thread to run the game loop, which is the method "run".
        gameLoopThread.start();
    }

    /**
     * Sets up the start of the game.
     */
    public void setupGame() {
        mapManager.loadLevels();
        inputManager = new InputManager();
        this.addKeyListener(inputManager);

        mapManager.resetLevelIndexAndMapIndexes();

        Point2D.Double position = new Point2D.Double(0, 0);
        PlayerFactory playerFactory = new PlayerFactory(this, playerType);
        Player player = playerFactory.create(position);
        playerId = player.getId();
        preservedSpriteIds.add(playerId);
    }

    /**
     * There are two ways to end the game. Firstly, the player collide with the enemies, or the player collide with
     * the punishment and his/her score is negative. Secondly, the player pass through every level including the boss
     * level.
     */
    public void endGame() {
        isRunning = false;
    }

    @Override
    public void run() {
        // Perform anything required for the game to run.
        setupGame();
        // Load levels.
        mapManager.loadCurrentLevelEntities();

        // Do the game loop every "gameLoopFrequency" nanoseconds.
        double gameLoopFrequency = 1_000_000_000 / FPS;
        double accumulator = 0;
        long lastAccumulatorLoopTime = System.nanoTime();
        long currentTime;
        double lastGameLoopTime = System.nanoTime();

        isRunning = true;
        while (isRunning) {
            currentTime = System.nanoTime();
            accumulator += (currentTime - lastAccumulatorLoopTime) / gameLoopFrequency;
            lastAccumulatorLoopTime = currentTime;

            // Do the game loop as many times as needed to "catch up" to where the game should be at in the present.
            while (accumulator >= 1) {
                // Run the game loop once. deltaTime is converted from nanoseconds to seconds.
                currentDeltaTime = (System.nanoTime() - lastGameLoopTime) / 1_000_000_000;
                currentFPS = 1 / currentDeltaTime;
                gameLoop();

                lastGameLoopTime = System.nanoTime();
                accumulator--;
            }

            if (gameLoopThread == null || gameScore < 0) {
                isRunning = false;
            }
        }

        this.removeKeyListener(inputManager);

        propertyChangeSupport.firePropertyChange("gameOver", false, true);
    }

    /**
     * During the game, it keeps updating everything and repaint the graphic.
     */
    public void gameLoop() {
        update();
        repaint();
    }

    /**
     * Updates for everything in the game.
     */
    public void update() {
        gameDurationSeconds += currentDeltaTime;
        // Do what needs to be done for certain keyboard inputs.
        applyInput();
        // Update all tiles and doors.
        mapManager.update(currentDeltaTime);
        // Update all entities (which are of type Entity).
        for (Entity<?> entity : entities.values()) {
            entity.update(currentDeltaTime);
        }
        // Check any other collisions not handled by the entities.
        checkCollisions();
        // Delete any entities that are set to be deleted. The entities can't delete themselves when their update method
        // is called, since that will modify the entities map as it is being iterated through.
        deleteEntities();
        // Run various tasks that should be done at the end of the update part of the game loop.
        cleanupAfterUpdate();
    }

    private void applyInput() {
        // Toggle debug mode if the debug button is pressed.
        if (getCurrentGameInput(InputName.DEBUG, InputType.JUST_PRESSED)) {
            debugMode = !debugMode;
        }

        if (getCurrentGameInput(InputName.FLIP, InputType.JUST_RELEASED)) {
            mapManager.attemptToFlipCurrentLevelToNextMap();
        }
    }

    private void checkCollisions() {
        // Detect when the player enters an open door.
        Player player = getPlayer();
        for (int doorId : doorIds) {
            Door door = (Door)getEntity(doorId);
            if(CollisionChecker.isCollision(player, door)){
                if(door.getIsOpened()){
                    enteredDoor();
                }
            }
        }
    }

    private void deleteEntities() {
        for (int spriteId : entitiesToDelete) {
            entities.remove(spriteId);
        }
        entitiesToDelete.clear();
    }

    /**
     * Reset all "justPressed" and "justReleased" input events to false.
     */
    public void cleanupAfterUpdate() {
        inputManager.resetInputs();
    }

    /**
     * Draw all sprites and tiles for an iteration of the game loop.
     *
     * @param graphics2D the graphics to draw on
     */
    public void draw(Graphics2D graphics2D) {
        mapManager.draw(graphics2D, debugMode, currentDeltaTime);

        // Draw all entities (which are of type Entity). Make sure to draw the player on top of the door.
        for (int doorId : doorIds) {
            getEntity(doorId).draw(graphics2D, debugMode, currentDeltaTime);
        }
        getEntity(playerId).draw(graphics2D, debugMode, currentDeltaTime);
        for (Entity<?> entity : entities.values()) {
            if (entity.getId() != playerId && !doorIds.contains(entity.getId())) {
                entity.draw(graphics2D, debugMode, currentDeltaTime);
            }
        }

        if (debugMode) {
            drawDebugMode(graphics2D);
        }

        drawGameScore(graphics2D);
        drawGameDuration(graphics2D);
    }

    /**
     * Draw debug mode.
     *
     * @param graphics2D the graphics 2 d
     */
    public void drawDebugMode(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0, 0, 0));
        graphics2D.drawString("FPS: " + new DecimalFormat("#.##").format(currentFPS), 20, 20);
    }

    /**
     * Draw game score.
     *
     * @param graphics2D the graphics 2 d
     */
    public void drawGameScore(Graphics2D graphics2D) {
        graphics2D.setFont(ThreeDventure.deriveFont(30f));
        graphics2D.setColor(new java.awt.Color(245, 167, 27, 253));
        int displayX = getParent().getWidth()/2;
        graphics2D.drawString("Score: " + gameScore, displayX-90f, 20);
    }

    /**
     * Draw game duration.
     *
     * @param graphics2D the graphics 2 d
     */
    public void drawGameDuration(Graphics2D graphics2D) {
        if ((int)gameDurationSeconds == 60) {
            gameDurationMinutes += 1;
            gameDurationSeconds = 0;
        }
        graphics2D.setColor(new java.awt.Color(245, 167, 27, 253));
        graphics2D.drawString(TextGenerator.getDurationText(gameDurationMinutes, gameDurationSeconds), 10,
                20);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D)graphics;

        if (isRunning) {
            draw(graphics2D);
        }

        graphics2D.dispose();
    }

    /**
     * Increase or decrease the game score based on the difference.
     *
     * @param diff the number to increase or decrease the game score
     */
    public void setGameScore(int diff) {
        gameScore += diff;
    }

    /**
     * Gets overlapping tiles from the current level map to detect if an entity is colliding with a wall.
     * This method only returns tiles that fall inside the enclosing rectangle, so that the entity has to compare its
     * hitbox with fewer tiles, as opposed to comparing its hitbox with every tile in the current map.
     *
     * @param enclosingRectangle the enclosing rectangle of an entity
     * @return the overlapping tiles from the current level map
     */
    public List<Tile<?>> getOverlappingTilesFromCurrentLevelMap(Rectangle2D.Double enclosingRectangle) {
        return mapManager.getOverlappingTilesFromCurrentLevelMap(enclosingRectangle);
    }

    /**
     * Gets overlapping tiles from the next level map to detect if an entity is colliding with a wall.
     *This method only returns tiles that fall inside the enclosing rectangle, so that the entity has to compare its
     *hitbox with fewer tiles, as opposed to comparing its hitbox with every tile in the current map.
     *
     * @param enclosingRectangle the enclosing rectangle
     * @return the overlapping tiles from next level map
     */
    public List<Tile<?>> getOverlappingTilesFromNextLevelMap(Rectangle2D.Double enclosingRectangle) {
        return mapManager.getOverlappingTilesFromNextLevelMap(enclosingRectangle);
    }

    /**
     * Test can it flip to the next map.
     *
     * @return the boolean
     */
    public boolean canFlipToNextMap() {
        return getPlayer().canFlipToNextMap();
    }

    /**
     * Gets the map manager.
     *
     * @return the map manager
     */
    public MapManager getMapManager(){
        return mapManager;
    }

    /**
     * Gets the audio manager.
     *
     * @return the audio manager
     */
    public AudioManager getAudioManager() {return audioManager;}

    /**
     * Gets screen pixel width.
     *
     * @return the screen pixel width
     */
    public int getScreenPixelWidth() {
        return screenPixelWidth;
    }

    /**
     * Gets screen pixel height.
     *
     * @return the screen pixel height
     */
    public int getScreenPixelHeight() {
        return screenPixelHeight;
    }

    /**
     * Add enemy to the entity hash map.
     *
     * @param spriteId the sprite id of the enemy
     */
    public void addEnemy(int spriteId) {
        enemyIds.add(spriteId);
    }

    /**
     * Add regular reward to the entity hash map.
     *
     * @param spriteId the sprite id of the regular reward
     */
    public void addRegularReward(int spriteId) {
        regularRewardIds.add(spriteId);
    }

    /**
     * Add door to the entity hash map.
     *
     * @param spriteId the sprite id of the door
     */
    public void addDoor(int spriteId) {
        doorIds.add(spriteId);
    }

    /**
     * Add entity to the entity hash map.
     *
     * @param spriteId the sprite id of the entity
     * @param entity   the entity
     */
    public void addEntity(int spriteId, Entity<?> entity) {
        entities.put(spriteId, entity);
    }

    /**
     * Destroy enemy from the entity hash map.
     *
     * @param spriteId the sprite id of the enemy
     */
    public void destroyEnemy(Integer spriteId) {
        enemyIds.remove(spriteId);
    }

    /**
     * Destroy regular reward from the entity hash map
     *
     * @param spriteId the sprite id of the regular reward
     */
    public void destroyRegularReward(Integer spriteId) {
        regularRewardIds.remove(spriteId);
        if (allRegularCollected()) {
            for (int doorId : doorIds) {
                Door door = (Door) entities.get(doorId);
                door.open();
            }
        }
    }

    /**
     * Destroy door from the entity hash map.
     */
    public void destroyDoor(int doorId) {
        doorIds.remove(doorId);
    }

    /**
     * Play soundtrack when the player entered the door. If player is currently in the last level, the game ends.
     * Or else, the player can access to the next level.
     */
    public void enteredDoor() {
        audioManager.playAudioOnce(AudioManager.AudioType.DOOR_CLOSE);
        isEnteredDoor = true;
        if (!mapManager.onLastLevel()){
            mapManager.startNextLevel();
        } else {
            endGame();
        }
    }

    /**
     * Destroy entity from the entity hash map.
     *
     * @param spriteId the sprite id of the entity
     */
    public void destroyEntity(int spriteId) {
        entitiesToDelete.add(spriteId);
    }

    /**
     * Gets entity from the entity hash map based on the sprite id.
     *
     * @param spriteId the sprite id of the entity
     * @return the entity
     */
    public Entity<?> getEntity(int spriteId) {
        return entities.get(spriteId);
    }

    /**
     * Destroy non preserved entities.
     */
    public void destroyNonPreservedEntities() {
        for (Map.Entry<Integer, Entity<?>> entityData : entities.entrySet()) {
            Entity<?> entity = entityData.getValue();
            if (entity != null) {
                if (!preservedSpriteIds.contains(entityData.getKey())) {
                    entity.destroy();
                }
            }
        }
    }

    /**
     * Gets camera manager.
     *
     * @return the camera manager
     */
    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return (Player) entities.get(playerId);
    }

    /**
     * Gets enemy sprite's ids.
     *
     * @return the enemy sprite's ids
     */
    public List<Integer> getEnemySpriteIds() {
        return enemyIds;
    }
    private List<Integer> getRegularRewardSpriteIds() {
        return regularRewardIds;
    }

    private boolean allRegularCollected() {
        return getRegularRewardSpriteIds().size() == 0;
    }

    /**
     * Sets player's type.
     *
     * @param playerType the player's type
     */
    public void setPlayerType(DungeonTileset.Player playerType) {
        this.playerType = playerType;
    }

    /**
     * Gets current game's input.
     *
     * @param inputName the input name
     * @param inputType the input type
     * @return the current game's input
     */
    public boolean getCurrentGameInput(InputName inputName, InputType inputType) {
        return inputManager.getInputValue(inputName, inputType);
    }

    /**
     * Gets player's images.
     *
     * @param character the character of the player
     * @return the player's images
     */
    public List<BufferedImage> getPlayerImages(DungeonTileset.Player character) {
        return mapManager.getPlayerImages(character);
    }

    /**
     * Gets enemy's images.
     *
     * @param character the character of the enemy
     * @return the enemy's images
     */
    public List<BufferedImage> getEnemyImages(DungeonTileset.Enemy character) {
        return mapManager.getEnemyImages(character);
    }

    /**
     * Gets regular reward's images.
     *
     * @return the regular reward's images
     */
    public List<BufferedImage> getRegularRewardImages() {
        return mapManager.getRegularRewardImages();
    }

    /**
     * Gets bonus reward's images.
     *
     * @return the bonus reward's images
     */
    public List<BufferedImage> getBonusRewardImages() {
        return mapManager.getBonusRewardImages();
    }

    /**
     * Gets punishment's images.
     *
     * @return the punishment's images
     */
    public List<BufferedImage> getPunishmentImages() {
        return mapManager.getPunishmentImages();
    }

    /**
     * Gets door's images.
     *
     * @param character the character of the door
     * @return the door's images
     */
    public List<BufferedImage> getDoorImages(DungeonTileset.Door character) {
        return mapManager.getDoorImages(character);
    }

    /**
     * Gets game score.
     *
     * @return the game score
     */
    public int getGameScore() {
        return gameScore;
    }

    /**
     * Gets game duration in minutes.
     *
     * @return the game duration minutes
     */
    public int getGameDurationMinutes() {
        return gameDurationMinutes;
    }

    /**
     * Gets game duration in seconds.
     *
     * @return the game duration seconds
     */
    public double getGameDurationSeconds() {
        return gameDurationSeconds;
    }


    /**
     * Returns if game is running or not.
     *
     * @return if the is running.
     */
    public boolean getIsRunning() {return isRunning;}

    /**
     * Returns if the door is entered or not.
     *
     * @return if the is entered.
     */
    public boolean getIsEnteredDoor(){
        return isEnteredDoor;
    }

    /**
     * Returns player type.
     *
     * @return player type.
     */
    public DungeonTileset.Player getPlayerType(){
        return playerType;
    }

    /**
     * Returns the input manager.
     *
     * @return the input manager of the game
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Returns the current position of the player.
     *
     * @return the current position of the player.
     */
    public Point2D.Double getCurrentPlayerPosition() {
        return getPlayer().getPosition();
    }

    /**
     * Returns the current Ids of regular rewards in the game.
     *
     * @return the current Ids of regular rewards in the game.
     */
    public List<Integer> getRegularRewardIds (){
        return regularRewardIds;
    }
}
