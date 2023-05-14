package sprite;

import app.game.Game;
import audio.AudioManager;
import entity.factory.EntityFactory;
import entity.factory.PunishmentFactory;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.assertEquals;

public class SpriteTest {
    private Game game;
    private Sprite<?> sprite;

    @Before
    public void setUpTests() {
        // Set up a game.
        AudioManager audioManager = new AudioManager();
        audioManager.mute();
        game = new Game(audioManager, null, null);
        game.setupGame();

        // Create an entity.
        EntityFactory entityFactory = new PunishmentFactory(game);
        sprite = entityFactory.create(new Point2D.Double(100, 60));
    }

    @Test
    public void testGetWidth() {
        assertEquals(48, sprite.getWidth(), 0);
    }

    @Test
    public void testGetHeight() {
        assertEquals(48, sprite.getHeight(), 0);
    }

    @Test
    public void testGetCenter() {
        assertEquals(new Point2D.Double(124, 84), sprite.getCenter());
    }

    @Test
    public void testGetCurrentImageWidth() {
        assertEquals(48, sprite.getCurrentImageWidth(), 0);
    }

    @Test
    public void testGetCurrentImageHeight() {
        assertEquals(48, sprite.getCurrentImageHeight(), 0);
    }
}
