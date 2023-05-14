package entity;

import app.game.Game;
import audio.AudioManager;
import entity.factory.DoorFactory;
import org.junit.Before;
import org.junit.Test;
import tileset.DungeonTileset;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class DoorTest {
    private Game game;
    private Door door;

    @Before
    public void setUpTests() {
        // Set up a game.
        AudioManager audioManager = new AudioManager();
        audioManager.mute();
        game = new Game(audioManager, null, null);
        game.setupGame();

        // Create an entity.
        DoorFactory doorFactory = new DoorFactory(game, DungeonTileset.Door.DOOR_1);
        door = doorFactory.create(new Point2D.Double(225, 581));
    }

    @Test
    public void testDestroy() {
        int spriteId = door.getId();

        // Tell the game this entity should be destroyed next time all entities are removed.
        door.destroy();
        // Actually remove entities set to be destroyed.
        game.update();

        // Make sure the entity no longer is a part of the game.
        assertNull(game.getEntity(spriteId));
    }

    @Test
    public void testOpen() {
        // Doors start off closed.
        assertFalse(door.getIsOpened());

        // Open the door.
        door.open();

        // The door should now be opened.
        assertTrue(door.getIsOpened());
    }
}
