package util.media;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 * A helper class to flip animation images.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class Animation {
    /**
     * Flip the image horizontally.
     *
     * @param image the image of an entity
     * @return the buffered image that has flipped
     */
    public static BufferedImage flipImageHorizontally(BufferedImage image) {
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1, 1);
        affineTransform.translate(-image.getWidth(), 0);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform,
                AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = affineTransformOp.filter(image, null);

        return image;
    }
}
