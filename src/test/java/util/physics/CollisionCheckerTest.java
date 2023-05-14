package util.physics;

import app.game.Game;
import entity.factory.PlayerFactory;
import org.junit.Before;
import org.junit.Test;
import sprite.Sprite;
import tileset.DungeonTileset;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CollisionCheckerTest {
    private Sprite<?> sprite1;
    private Sprite<?> sprite2;

    @Before
    public void setUpTests() {
        PlayerFactory playerFactory = new PlayerFactory(new Game(null, null, null),
                DungeonTileset.Player.KNIGHT_ORANGE);
        sprite1 = playerFactory.create(new Point2D.Double(0, 0));
        sprite2 = playerFactory.create(new Point2D.Double(0, 0));
    }

    @Test
    public void testInstantiatingClass() {
        // Test instantiating the class.
        CollisionChecker collisionChecker = new CollisionChecker();
    }

    @Test
    public void testCollisionSpritesNoOverlap() {
        sprite1.setPosition(100, 100);

        assertFalse(CollisionChecker.isCollision(sprite1, sprite2));
    }

    @Test
    public void testCollisionSpritesOverlap() {
        sprite1.setPosition(10, 10);

        assertTrue(CollisionChecker.isCollision(sprite1, sprite2));
    }

    @Test
    public void testGetIntersectionsSpritesNoOverlap() {
        sprite1.setPosition(100, 100);

        Map<Integer, List<Rectangle2D.Double>> intersections = new HashMap<>();

        List<Rectangle2D.Double> intersectingHitboxes = new ArrayList<>();
        intersections.put(0, intersectingHitboxes);

        assertEquals(intersections, CollisionChecker.getIntersections(sprite1, sprite2));
    }

    @Test
    public void testGetIntersectionsSpritesOverlap() {
        sprite1.setPosition(10, 10);

        Map<Integer, List<Rectangle2D.Double>> intersections = new HashMap<>();

        List<Rectangle2D.Double> intersectingHitboxes = new ArrayList<>();
        intersectingHitboxes.add(sprite2.getHitboxes().get(0));

        intersections.put(0, intersectingHitboxes);

        assertEquals(intersections, CollisionChecker.getIntersections(sprite1, sprite2));
    }
}
