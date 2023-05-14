package app.game;

import audio.AudioManager;
import entity.factory.DoorFactory;
import entity.movable.Player;
import level.MapManager;
import org.junit.*;
import tileset.DungeonTileset;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class GameTest {

    AudioManager audioManager;

    @Before
    public void setUpTest(){
        audioManager = new AudioManager();
        audioManager.addAudio(AudioManager.AudioType.DOOR_CLOSE, "DOOR_CLOSE.wav");
        audioManager.addAudio(AudioManager.AudioType.DOOR_OPEN, "DOOR_OPEN.wav");
    }
    @Test
    public void testStartGameLoop(){
        Game game = new Game(null,null,null);
        game.startGameLoop();
        assertEquals(0, game.getEnemySpriteIds().size());
    }

    @Test
    public void testSetUpGame(){
        Game game = new Game(null, null, null);
        game.setupGame();
        Player player = game.getPlayer();
        assertEquals(new Point2D.Double(0,0),player.getPosition());
    }

    @Test
    public void testEndGame(){
        Game game = new Game(null, null, null);
        game.endGame();
        assertFalse(game.getIsRunning());
    }

    @Test
    public void testSetGameScore(){
        Game game = new Game(null, null, null);
        game.setGameScore(100);
        assertEquals(100, game.getGameScore());
    }

    @Test
    public void testGetScreenPixelWidth(){
        Game game = new Game(null, null, null);
        assertEquals(20*16*3, game.getScreenPixelWidth());
    }

    @Test
    public void testGetScreenPixelHeight(){
        Game game = new Game(null, null, null);
        assertEquals(14*16*3, game.getScreenPixelHeight());
    }

    @Test
    public void testDestroyEnemy(){
        Game game = new Game(null, null, null);
        game.addEnemy(0);
        game.destroyEnemy(0);
        assertEquals(0, game.getEnemySpriteIds().size());
    }

    @Test
    public void testEnteredDoor(){
        Game game = new Game(audioManager, null, null);
        game.setupGame();
        MapManager mapManager = new MapManager(game);
        mapManager.loadLevel("TestLevel");
        mapManager.switchToLevel("TestLevel");
        game.enteredDoor();
        assertTrue(game.getIsEnteredDoor());
        mapManager.loadLevel("TestLevel1");
        mapManager.switchToLevel("TestLevel1");
        game.enteredDoor();
        assertTrue(game.getIsEnteredDoor());
    }

    @Test
    public void testSetPlayerType(){
        Game game = new Game(null, null, null);
        game.setPlayerType(DungeonTileset.Player.DINO_1);
        assertEquals(DungeonTileset.Player.DINO_1, game.getPlayerType());
    }

    @Test
    public void testGetGameDuration(){
        Game game = new Game(null, null, null);
        assertEquals(0, game.getGameDurationMinutes());
        assertEquals(0,0, game.getGameDurationSeconds());
    }

    @Test
    public void testDestroyRegularReward(){
        Game game = new Game(audioManager, null, null);
        game.addRegularReward(0);
        game.destroyRegularReward(0);
        DoorFactory doorFactory = new DoorFactory(game, DungeonTileset.Door.DOOR_1);
        Point2D.Double position =new Point2D.Double(1,1);
        doorFactory.create(position);
        game.addRegularReward(0);
        game.destroyRegularReward(0);
        assertEquals(0, game.getRegularRewardIds().size());
    }
}
