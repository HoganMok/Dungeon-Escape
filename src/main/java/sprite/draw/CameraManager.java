package sprite.draw;

import app.game.Game;
import entity.Entity;
import level.MapManager;
import sprite.Sprite;

import java.awt.geom.Point2D;

/**
 * Implements the scrolling camera for the game.
 * <p>
 * The class provides methods to calculate and return updated positions based on the sprite it is targeting.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class CameraManager {
    private Integer targetSpriteId = -1;

    private final Game game;
    private final MapManager mapManager;

    /**
     * Instantiates a new Camera manager to manage the scrolling camera.
     *
     * @param game the game
     */
    public CameraManager(Game game) {
        this.game = game;
        mapManager = game.getMapManager();
    }

    /**
     * Sets target.
     *
     * @param spriteId the sprite id
     */
    public void setTarget(int spriteId) {
        targetSpriteId = spriteId;
    }

    private Entity<?> getTarget() {
        return game.getEntity(targetSpriteId);
    }

    private int getScreenWidth() {
        return game.getWidth();
    }
    private int getScreenHeight() {
        return game.getHeight();
    }

    private int getScreenXOffset() {
        int offsetX = (getScreenWidth() - mapManager.getCurrentLevelPixelWidth()) / 2;
        return Math.max(offsetX, 0);
    }
    private int getScreenYOffset() {
        int offsetY = (getScreenHeight() - mapManager.getCurrentLevelPixelHeight()) / 2;
        return Math.max(offsetY, 0);
    }

    private double getTargetPositionX() {
        return getTarget().getPositionX();
    }
    private double getTargetPositionY() {
        return getTarget().getPositionY();
    }

    private boolean isTarget(int spriteId) {
        return spriteId == targetSpriteId;
    }

    private double getTargetScreenX() {
        int x = (int) (getScreenWidth() / 2 - getTarget().getWidth() / 2);

        if (getTargetPositionX() < x) {
            return getTargetPositionX();
        } else if (mapManager.getCurrentLevelPixelWidth() - getTargetPositionX() < x + getTarget().getWidth()) {
            return getScreenWidth() - (mapManager.getCurrentLevelPixelWidth() - getTargetPositionX());
        } else {
            return x;
        }
    }
    private double getTargetScreenY() {
        int y = (int) (getScreenHeight() / 2 - getTarget().getHeight() / 2);

        if (getTargetPositionY() < y) {
            return getTargetPositionY();
        } else if (mapManager.getCurrentLevelPixelHeight() - getTargetPositionY() < y + getTarget().getHeight()) {
            return getScreenHeight() - (mapManager.getCurrentLevelPixelHeight() - getTargetPositionY());
        } else {
            return y;
        }
    }

    private double getNonTargetOffsetX() {
        return getTargetScreenX() - getTargetPositionX();
    }
    private double getNonTargetOffsetY() {
        return getTargetScreenY() - getTargetPositionY();
    }

    /**
     * Gets how much a sprite should shift based on the camera.
     *
     * @param sprite the sprite that a camera shift should be applied to
     * @return the updated position to draw at, null means don't draw (off the screen)
     */
    public Point2D.Double getAppliedCameraShift(Sprite<?> sprite) {
        return getAppliedCameraShift(sprite.getPosition(), sprite.getAnimationOffset(), sprite, false);
    }

    /**
     * Gets how much a point should shift based on the camera.
     * <p>
     * If the attachedSprite is off the screen, return null to not draw using the point.
     *
     * @param position       the position of a camera
     * @param offset         the offset of a camera
     * @param attachedSprite a sprite associated with this point
     * @return the amount that the camera need to shift, null means don't draw (off the screen)
     */
    public Point2D.Double getAppliedCameraShift(Point2D.Double position, Point2D.Double offset,
                                                Sprite<?> attachedSprite) {
        Point2D.Double newPosition = getAppliedCameraShift(position, offset, attachedSprite, attachedSprite == null);

        // Don't draw if the attached sprite will not be drawn.
        if (attachedSprite != null) {
            // Make sure the attached sprite is on the screen.
            Point2D.Double attachedSpriteDrawPosition = getAppliedCameraShift(attachedSprite);
            if (attachedSpriteDrawPosition == null
                    || attachedSpriteDrawPosition.getX() + attachedSprite.getCurrentImageWidth() < 0
                    || attachedSpriteDrawPosition.getX() > getScreenWidth()
                    || attachedSpriteDrawPosition.getY() + attachedSprite.getCurrentImageHeight() < 0
                    || attachedSpriteDrawPosition.getY() > getScreenHeight()) {
                return null;
            }
        }

        return newPosition;
    }

    private Point2D.Double getAppliedCameraShift(Point2D.Double position, Point2D.Double offset, Sprite<?> sprite,
                                                boolean forceReturnPosition) {
        int spriteId = -1;
        double imageWidth = 0;
        double imageHeight = 0;
        if (sprite != null) {
            spriteId = sprite.getId();
            imageWidth = sprite.getCurrentImageWidth();
            imageHeight = sprite.getCurrentImageHeight();
        }

        Point2D.Double finalDrawLocation = getFinalDrawLocation(position, offset, spriteId);

        // Used to still draw vectors which may be partially off the screen.
        if (forceReturnPosition) {
            return finalDrawLocation;
        }

        // If the final location is off the screen, don't draw it.
        if (finalDrawLocation.getX() + imageWidth < 0
                || finalDrawLocation.getX() > getScreenWidth()
                || finalDrawLocation.getY() + imageHeight < 0
                || finalDrawLocation.getY() > getScreenHeight()) {
            return null;
        }

        return finalDrawLocation;
    }

    private Point2D.Double getFinalDrawLocation(Point2D.Double position, Point2D.Double offset, int spriteId) {
        if (getTarget() == null) {
            return new Point2D.Double(position.getX() + offset.getX() + getScreenXOffset(),
                    position.getY() + offset.getY() + getScreenYOffset());
        }

        if (isTarget(spriteId)) {
            return new Point2D.Double(
                    offset.getX() + getScreenXOffset() + (getScreenXOffset() == 0 ? getTargetScreenX() : position.getX()),
                    offset.getY() + getScreenYOffset() + (getScreenYOffset() == 0 ? getTargetScreenY() : position.getY()));
        } else {
            return new Point2D.Double(
                    offset.getX() + getScreenXOffset() + position.getX() + (getScreenXOffset() == 0 ? getNonTargetOffsetX() : 0),
                    offset.getY() + getScreenYOffset() + position.getY() + (getScreenYOffset() == 0 ? getNonTargetOffsetY() : 0));
        }
    }
}
