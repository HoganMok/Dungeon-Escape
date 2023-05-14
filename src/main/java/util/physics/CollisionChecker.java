package util.physics;

import sprite.data.Hitbox;
import sprite.Sprite;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Detects collisions between sprites.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class CollisionChecker {
    /**
     * Check if there is any collision between sprite.
     *
     * @param sprite1 the first sprite to compare
     * @param sprite2 the second sprite to compare
     * @return true if a hitbox from sprite 1 and sprite 2 overlap, and false otherwise
     */
    public static boolean isCollision(Sprite<?> sprite1, Sprite<?> sprite2){
        // Check if any hitboxes of two sprites collide.
        for (Hitbox sprite1Hitbox : sprite1.getHitboxes()) {
            for (Hitbox sprite2Hitbox : sprite2.getHitboxes()) {
                if (sprite1Hitbox.intersects(sprite2Hitbox)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the hitboxes of a sprite that collide with another sprite.
     *
     * @param sprite1 the first sprite to compare
     * @param sprite2 the second sprite to compare
     * @return a map of the hitboxes of the first sprite, that collided with a list of hitboxes of the second sprite
     */
    public static Map<Integer, List<Rectangle2D.Double>> getIntersections(Sprite<?> sprite1, Sprite<?> sprite2) {
        // Get all hitboxes of sprite2 that intersect sprite1. The return value map key is the index of a hitbox of
        // sprite1 that collides with one or more hitboxes of sprite2. The return value map value is the list of
        // hitboxes of sprite2 that collide with the hitbox represented by the key for the map entry.

        // Temporary return value for now, before this method is implemented.
        List<Hitbox> sprite1Hitboxes = sprite1.getHitboxes();
        Map<Integer, List<Rectangle2D.Double>> collisionHitboxes = new HashMap<>();
        for (Hitbox sprite1Hitbox : sprite1Hitboxes) {
            List<Rectangle2D.Double> intersectingRectangles = new ArrayList<>();
            for (Hitbox sprite2Hitbox : sprite2.getHitboxes()) {
                if (sprite1Hitbox.intersects(sprite2Hitbox)) {
                    intersectingRectangles.add(sprite2Hitbox);
                }
            }
            collisionHitboxes.put(sprite1Hitboxes.indexOf(sprite1Hitbox), intersectingRectangles);
        }
        return collisionHitboxes;
    }
}
