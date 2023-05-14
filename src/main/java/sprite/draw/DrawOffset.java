package sprite.draw;

import java.awt.geom.Point2D;
import java.util.Map;

/**
 * The draw offset for an image. This is done so the image and hitbox line up correctly.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 *
 * @param <T> the animation type
 */
public class DrawOffset<T extends Enum<T>> {
    private final Map<T, Point2D.Double> offsets;
    private final Point2D.Double flipAnimationHorizontallyOffset;

    /**
     * Instantiates a new Draw offset.
     *
     * @param offsets                         the offsets of a sprite
     * @param flipAnimationHorizontallyOffset the flip animation horizontally offset of a sprite
     */
    public DrawOffset(Map<T, Point2D.Double> offsets, Point2D.Double flipAnimationHorizontallyOffset) {
        this.offsets = offsets;
        this.flipAnimationHorizontallyOffset = flipAnimationHorizontallyOffset;
    }

    /**
     * Gets offset.
     *
     * @param animation                 the animation of a sprite
     * @param flipAnimationHorizontally the flip animation horizontally of a sprite
     * @return the offset
     */
    public Point2D.Double getOffset(T animation, boolean flipAnimationHorizontally) {
        Point2D.Double baseOffset = offsets.get(animation);
        Point2D.Double offset = new Point2D.Double(baseOffset.getX(), baseOffset.getY());

        if (flipAnimationHorizontally) {
            offset.setLocation(
                    offset.getX() + flipAnimationHorizontallyOffset.getX(),
                    offset.getY() + flipAnimationHorizontallyOffset.getY());
        }

        return offset;
    }
}
