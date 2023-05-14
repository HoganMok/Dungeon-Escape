package sprite.data;

import sprite.draw.DrawOffset;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Stores data used to create a sprite.
 *
 * @param <T> the animation type
 * @author Ewan Brinkman
 * @version 1.2
 */
public record SpriteData<T extends Enum<T>>(Rectangle2D.Double enclosingRectangle,
                                            List<Rectangle2D.Double> relativeHitboxes, DrawOffset<T> drawOffset,
                                            List<BufferedImage> animationImages,
                                            Map<T, List<Integer>> animationImageNumbers, T initialAnimation) {
    /**
     * Store the data for creating a sprite.
     *
     * @param enclosingRectangle    the enclosing rectangle of a sprite
     * @param relativeHitboxes      the relative hitboxes of a sprite
     * @param drawOffset            the draw offset of a sprite
     * @param animationImages       the animation images of a sprite
     * @param animationImageNumbers the animation image numbers of a sprite
     * @param initialAnimation      the initial animation of a sprite
     */
    public SpriteData {
    }
}
