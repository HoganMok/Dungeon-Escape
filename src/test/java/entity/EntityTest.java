package entity;

import app.game.Game;
import audio.AudioManager;
import entity.factory.EntityFactory;
import entity.factory.PunishmentFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntityTest {
    private Game game;
    private Entity<?> entity;

    @Before
    public void setUpTests() {
        // Set up a game.
        AudioManager audioManager = new AudioManager();
        audioManager.mute();
        game = new Game(audioManager, null, null);
        game.setupGame();

        // Create an entity.
        EntityFactory entityFactory = new PunishmentFactory(game);
        entity = entityFactory.create(new Point2D.Double(45, 82));
    }

    @Test
    public void testAddEntityToGame() {
        int spriteId = entity.getId();

        // Make sure the entity is now in the game, and can be found using its correct sprite Id.
        assertEquals(entity, game.getEntity(spriteId));
    }

    @Test
    public void testRemoveEntityFromGame() {
        int spriteId = entity.getId();

        // Tell the game this entity should be destroyed next time all entities are removed.
        entity.destroy();
        // Actually remove entities set to be destroyed.
        game.update();

        // Make sure the entity no longer is a part of the game.
        assertNull(game.getEntity(spriteId));
    }
}
