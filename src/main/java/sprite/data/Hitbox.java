package sprite.data;

import sprite.draw.CameraManager;
import sprite.Sprite;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Stores hitbox data.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class Hitbox extends Rectangle2D.Double {
    private final Point2D.Double ownerPositionOffset;

    private final Color debugColorOpaque;
    private final Color debugColorTransparent;
    private final float debugThickness;

    private final CameraManager cameraManager;

    private final Sprite<?> owner;

    /**
     * Instantiates a new Hitbox of a sprite and set it up.
     *
     * @param rectangle      the rectangle of a sprite
     * @param debugColor     the debug color of a sprite
     * @param debugOpacity   the debug opacity of a sprite
     * @param debugThickness the debug thickness of a sprite
     * @param cameraManager  the camera manager of a sprite
     * @param owner          the owner of a sprite
     */
    public Hitbox(Rectangle2D.Double rectangle, Color debugColor, double debugOpacity,
                  float debugThickness, CameraManager cameraManager, Sprite<?> owner) {
        super(owner.getPositionX() + rectangle.getX(), owner.getPositionY() + rectangle.getY(), rectangle.getWidth(),
                rectangle.getHeight());

        ownerPositionOffset = new Point2D.Double(rectangle.getX(), rectangle.getY());

        debugColorOpaque = debugColor;
        debugColorTransparent = new Color(
                debugColorOpaque.getRed(),
                debugColorOpaque.getGreen(),
                debugColorOpaque.getBlue(),
                (int) (255 * debugOpacity));
        this.debugThickness = debugThickness;

        this.cameraManager = cameraManager;

        this.owner = owner;
    }

    private double getPositionX() {
        return getX();
    }
    private double getPositionY() {
        return getY();
    }
    private Point2D.Double getPosition() {
        return new Point2D.Double(getPositionX(), getPositionY());
    }

    /**
     * Sets position of the hitbox.
     *
     * @param ownerPosition the owner position
     */
    public void setPosition(Point2D.Double ownerPosition) {
        x = ownerPosition.getX() + ownerPositionOffset.getX();
        y = ownerPosition.getY() + ownerPositionOffset.getY();
    }

    /**
     * Draw the hitbox.
     *
     * @param graphics2D the graphics 2 d
     */
    public void draw(Graphics2D graphics2D) {
        // Draw the sprite's hitbox outline.
        graphics2D.setColor(debugColorOpaque);
        graphics2D.setStroke(new BasicStroke(debugThickness));
        Point2D.Double cameraShiftedPositionHitboxOutline = cameraManager.getAppliedCameraShift(getPosition(),
                new Point2D.Double(debugThickness / 2, debugThickness / 2), owner);
        if (cameraShiftedPositionHitboxOutline != null) {
            graphics2D.drawRect(
                    (int) (cameraShiftedPositionHitboxOutline.getX()),
                    (int) (cameraShiftedPositionHitboxOutline.getY()),
                    (int) (getWidth() - debugThickness),
                    (int) (getHeight() - debugThickness));
        }

        // Draw the sprite's hitbox inside.
        graphics2D.setColor(debugColorTransparent);
        Point2D.Double cameraShiftedPositionHitboxFill = cameraManager.getAppliedCameraShift(getPosition(),
                new Point2D.Double(0, 0), owner);
        if (cameraShiftedPositionHitboxFill != null) {
            graphics2D.fillRect((int) (cameraShiftedPositionHitboxFill.getX()),
                    (int) (cameraShiftedPositionHitboxFill.getY()), (int) getWidth(), (int) getHeight());
        }
    }
}
