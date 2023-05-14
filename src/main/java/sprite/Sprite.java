package sprite;

import app.game.Game;
import sprite.draw.CameraManager;
import sprite.data.Hitbox;
import sprite.data.SpriteData;
import sprite.draw.DrawOffset;
import sprite.draw.Drawable;
import util.resources.ConfigReader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A sprite of the game. It can either be an entity, such as the player, or a tile.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 *
 * @param <T> the animation type
 */
public abstract class Sprite<T extends Enum<T>> extends Drawable<T> {
    /**
     * The Enclosing rectangle.
     */
    // The position, width, and height of the sprite are stored in enclosingRectangle. The width and height are used for
    // finding the center when drawing physics vectors in debug mode. For collisions, the sprite's hitboxes are used.
    protected final Rectangle2D.Double enclosingRectangle;

    /**
     * The Hitboxes.
     */
    protected final List<Hitbox> hitboxes = new ArrayList<>();

    private final DrawOffset<T> drawOffset;

    /**
     * The constant debugModeProfileName.
     */
    protected static final String debugModeProfileName = "sprite";

    /**
     * The Game.
     */
    protected final Game game;

    /**
     * The Camera manager.
     */
    protected final CameraManager cameraManager;

    /**
     * The Is active.
     */
    protected boolean isActive = true;
    private float transparency = 1;

    private static int currentSpriteId = 0;
    /**
     * The Sprite id.
     */
    protected final int spriteId;

    /**
     * Instantiates a new Sprite and set it up in Entity.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Sprite(Game game, SpriteData<T> spriteData) {
        super(spriteData.animationImages(), spriteData.animationImageNumbers(), spriteData.initialAnimation());

        spriteId = currentSpriteId++;

        this.game = game;

        this.enclosingRectangle = spriteData.enclosingRectangle();

        cameraManager = game.getCameraManager();

        for (Rectangle2D.Double relativeHitbox : spriteData.relativeHitboxes()) {
            hitboxes.add(new Hitbox(relativeHitbox, ConfigReader.getDebugModeHitboxColor(debugModeProfileName),
                    ConfigReader.getDebugModeHitboxOpacity(), ConfigReader.getDebugModeHitboxThickness(),
                    cameraManager, this));
        }

        this.drawOffset = spriteData.drawOffset();
    }

    /**
     * Gets id of the sprite.
     *
     * @return the id
     */
    public int getId() {
        return spriteId;
    }


    /**
     * Sets transparency of the sprite.
     *
     * @param transparency the transparency level
     */
    protected void setTransparency(float transparency) {
        if (transparency <= 0) {
            transparency = 0;
        }

        if (transparency >= 1) {
            transparency = 1;
        }

        this.transparency = transparency;
    }

    private void updateHitboxPositions() {
        for (Hitbox hitbox : hitboxes) {
            hitbox.setPosition(getPosition());
        }
    }

    /**
     * Draw the sprites.
     *
     * @param graphics2D the graphics 2 d of a sprite
     * @param debugMode  the debug mode of a sprite
     * @param deltaTime  the delta time
     */
    public void draw(Graphics2D graphics2D, boolean debugMode, double deltaTime) {
        if (!isActive) {
            return;
        }

        draw(graphics2D, deltaTime);
        if (debugMode) {
            drawDebugMode(graphics2D);
        }
    }

    private void draw(Graphics2D graphics2D, double deltaTime) {
        if (transparency != 1) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        }
        drawAnimationImage(graphics2D, getCurrentImage());
        if (transparency != 1) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        super.update(deltaTime);
    }

    private void drawAnimationImage(Graphics2D graphics2D, BufferedImage image) {
        Point2D.Double cameraShiftedPosition = cameraManager.getAppliedCameraShift(this);
        if (cameraShiftedPosition != null) {
            graphics2D.drawImage(image, (int) (cameraShiftedPosition.getX()), (int) (cameraShiftedPosition.getY()),
                    null, null);
        }
    }

    public Point2D.Double getAnimationOffset() {
        return drawOffset.getOffset(getCurrentAnimation(), getFlipAnimationHorizontally());
    }

    /**
     * Draw debug mode of a sprite.
     *
     * @param graphics2D the graphics 2 d of a sprite
     */
    protected void drawDebugMode(Graphics2D graphics2D) {
        for (Hitbox hitbox : hitboxes) {
            hitbox.draw(graphics2D);
        }
    }

    /**
     * Gets the horizontal position of the hit box.
     *
     * @return the horizontal position
     */
    public double getPositionX() {
        return enclosingRectangle.getX();
    }

    /**
     * Gets the vertical position of the hit box.
     *
     * @return the vertical position of the hit box
     */
    public double getPositionY() {
        return enclosingRectangle.getY();
    }

    /**
     * Gets position of the entity.
     *
     * @return the position
     */
    public Point2D.Double getPosition() {
        return new Point2D.Double(getPositionX(), getPositionY());
    }

    /**
     * Sets x coordinate of the entity.
     *
     * @param x the x coordinate
     */
    public void setPositionX(double x) {
        enclosingRectangle.x = x;
        updateHitboxPositions();
    }

    /**
     * Sets y coordinate of the entity.
     *
     * @param y the y coordinate
     */
    public void setPositionY(double y) {
        enclosingRectangle.y = y;
        updateHitboxPositions();
    }

    /**
     * Sets position of the entity.
     *
     * @param position the new position of the sprite
     */
    public void setPosition(Point2D.Double position) {
        setPositionX(position.getX());
        setPositionY(position.getY());
    }

    /**
     * Sets position of the entity.
     *
     * @param x the new x position of the sprite
     * @param y the new y position of the sprite
     */
    public void setPosition(double x, double y) {
        setPositionX(x);
        setPositionY(y);
    }

    /**
     * Gets the horizontal center of the hitbox.
     *
     * @return the horizontal center
     */
    public double getCenterX() {
        return enclosingRectangle.getCenterX();
    }

    /**
     * Gets the vertical center of the hitbox.
     *
     * @return the vertical center of the hitbox
     */
    public double getCenterY() {
        return enclosingRectangle.getCenterY();
    }

    /**
     * Gets a list hitboxes of a entity.
     *
     * @return the lsit of hitboxes
     */
    public List<Hitbox> getHitboxes() {
        return hitboxes;
    }

    /**
     * Gets center of the hitbox.
     *
     * @return the center
     */
    public Point2D.Double getCenter() {
        return new Point2D.Double(getCenterX(), getCenterY());
    }

    /**
     * Gets the width of the hitbox.
     *
     * @return the width
     */
    public double getWidth() {
        return enclosingRectangle.getWidth();
    }

    /**
     * Gets the height of the hitbox.
     *
     * @return the height
     */
    public double getHeight() {
        return enclosingRectangle.getHeight();
    }

    /**
     * Gets the current width of the current image of the sprite.
     *
     * @return the current width
     */
    public double getCurrentImageWidth() {
        return getCurrentImage().getWidth();
    }

    /**
     * Gets the current height of the current image of the sprite.
     *
     * @return the current height
     */
    public double getCurrentImageHeight() {
        return getCurrentImage().getHeight();
    }
}
