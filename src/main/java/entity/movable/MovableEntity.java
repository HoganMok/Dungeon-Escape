package entity.movable;

import app.game.Game;
import entity.Entity;
import sprite.data.Hitbox;
import sprite.Sprite;
import sprite.data.SpriteData;
import level.tile.Tile;
import util.physics.CollisionChecker;
import util.physics.Vector;
import util.resources.ConfigReader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * An entity that can move around and detect collisions with wall tiles.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 *
 * @param <T> the animation type
 */
public abstract class MovableEntity<T extends Enum<T>> extends Entity<T> {
    private enum Direction {
        /**
         * X direction.
         */
        X,
        /**
         * Y direction.
         */
        Y
    }

    private final Point2D.Double velocity = new Point2D.Double(0, 0);
    private final Point2D.Double acceleration = new Point2D.Double(0, 0);

    // A value to multiply the velocity by when not moving. Usually, this value would be from 0 to 1.
    private final double friction;
    // The maximum velocity the sprite is allowed to move at.
    private final double maxVelocity;
    // The minimum velocity the sprite is allowed to move at. This stops the sprite from having the velocity become a
    // very small number when friction is applied. This is because friction only multiples the current velocity by a
    // number. So, if the number is neither 0 nor 1 (but between 0 and 1), then the sprite's velocity will approach 0
    // but never reach 0 when slowing down. Having a minVelocity will set the sprite's velocity to 0 if they are moving
    // slower than the minimum magnitude of the velocity (which is minVelocity).
    private final double minVelocity;

    private static final Color velocityColor = ConfigReader.getDebugModeVelocityColor();
    private static final Color accelerationColor = ConfigReader.getDebugModeAccelerationColor();
    private static final Color maxVelocityColor = ConfigReader.getDebugModeMaxVelocityColor();

    private static final float debugModeVectorThickness = ConfigReader.getDebugModeVectorThickness();

    private final Point2D.Double spawnPosition;

    /**
     * The Spawning.
     */
    protected boolean spawning = false;
    private double spawnCurrentTime = 0;
    private final double spawnTotalTime = 0;

    /**
     * Instantiates a new Movable entity and set up its maximum and minimum velocity, also set up its friction, and
     * the spawn position of the entity.
     *
     * @param game        the game
     * @param spriteData  the data to create this sprite
     * @param friction    the friction of a movable entity
     * @param maxVelocity the max velocity of a movable entity
     * @param minVelocity the min velocity of a movable entity
     */
    public MovableEntity(Game game, SpriteData<T> spriteData, double friction, double maxVelocity, double minVelocity) {
        super(game, spriteData);

        this.friction = friction;
        this.maxVelocity = maxVelocity;
        this.minVelocity = minVelocity;

        spawnPosition = getPosition();
    }

    protected void drawDebugMode(Graphics2D graphics2D) {
        // Call the Sprite drawDebugMode method, which draws the sprite's hitbox.
        super.drawDebugMode(graphics2D);

        graphics2D.setStroke(new BasicStroke(debugModeVectorThickness));

        // Draw a line to represent the acceleration vector.
        drawDebugVector(graphics2D, getAcceleration(), accelerationColor);

        // Draw a line to represent the maximum magnitude of the velocity vector that is allowed.
        drawDebugVector(graphics2D, Vector.getVectorScaledToMax(getVelocity(), maxVelocity), maxVelocityColor);

        // Draw a line to represent the velocity vector.
        drawDebugVector(graphics2D, getVelocity(), velocityColor);
    }

    private void drawDebugVector(Graphics2D graphics2D, Point2D.Double vector, Color vectorColor) {
        graphics2D.setColor(vectorColor);
        // Set the sprite Id for the camera shift to be -1 so that the camera shift is always applied thinking that this
        // sprite is not the camera's target.
        Point2D.Double cameraShiftedPositionStart = cameraManager.getAppliedCameraShift(getCenter(),
                new Point2D.Double(0, 0), null);
        Point2D.Double cameraShiftedPositionEnd = cameraManager.getAppliedCameraShift(getCenter(), vector, null);
        if (cameraShiftedPositionStart != null && cameraShiftedPositionEnd != null) {
            graphics2D.drawLine(
                    (int) (cameraShiftedPositionStart.getX()),
                    (int) (cameraShiftedPositionStart.getY()),
                    (int) (cameraShiftedPositionEnd.getX()),
                    (int) (cameraShiftedPositionEnd.getY()));
        }
    }

    /**
     * Gets the horizontal velocity of a movable entity.
     *
     * @return the horizontal velocity
     */
    public double getVelocityX() {
        return velocity.getX();
    }

    /**
     * Gets the vertical velocity of a movable sprite.
     *
     * @return the vertical velocity
     */
    public double getVelocityY() {
        return velocity.getY();
    }

    /**
     * Gets velocity of a movable sprite.
     *
     * @return the velocity
     */
    public Point2D.Double getVelocity() {
        return velocity;
    }

    /**
     * Sets the horizontal velocity of a movable entity.
     *
     * @param x the horizontal velocity
     */
    public void setVelocityX(double x) {
        velocity.x = x;
    }

    /**
     * Sets the vertical velocity of a movable entity.
     *
     * @param y the vertical velocity
     */
    public void setVelocityY(double y) {
        velocity.y = y;
    }

    /**
     * Sets velocity of a movable entity.
     *
     * @param point the point of double including the velocity of a movable entity
     */
    public void setVelocity(Point2D.Double point) {
        velocity.setLocation(point);
    }

    /**
     * Gets horizontal acceleration of a movable entity.
     *
     * @return the horizontal acceleration of a movable entity
     */
    public double getAccelerationX() {
        return acceleration.getX();
    }

    /**
     * Gets vertical acceleration of a movable entity.
     *
     * @return the vertical acceleration of a movable entity
     */
    public double getAccelerationY() {
        return acceleration.getY();
    }

    /**
     * Gets acceleration of a movable entity.
     *
     * @return the acceleration of a movable entity.
     */
    public Point2D.Double getAcceleration() {
        return acceleration;
    }

    /**
     * Sets horizontal acceleration of a movable entity.
     *
     * @param x the horizontal acceleration of a movable entity
     */
    public void setAccelerationX(double x) {
        acceleration.x = x;
    }

    /**
     * Sets vertical acceleration of a movable entity.
     *
     * @param y the vertical acceleration of a movable entity
     */
    public void setAccelerationY(double y) {
        acceleration.y = y;
    }

    /**
     * Sets acceleration of a movable entity.
     *
     * @param x the horizontal acceleration of a movable entity
     * @param y the vertical acceleration of a movable entity
     */
    public void setAcceleration(double x, double y) {
        acceleration.setLocation(x, y);
    }

    /**
     * Sets acceleration of a movable entity.
     *
     * @param point the point including the acceleration of a movable entity
     */
    public void setAcceleration(Point2D.Double point) {
        acceleration.setLocation(point);
    }

    /**
     * Is accelerating or not.
     *
     * @return the boolean
     */
    public boolean isAccelerating() {
        return getAccelerationX() != 0 || getAccelerationY() != 0;
    }

    public void update(double deltaTime) {
        if (spawning) {
            spawnCurrentTime += deltaTime;
            if (spawnCurrentTime >= spawnTotalTime && !collidingWithTile()) {
                setTransparency(1f);
                spawning = false;
            }
        }

        // Move the sprite based on its position, velocity, and acceleration.
        if (!spawning) {
            move(deltaTime);
        }
    }

    private void move(double deltaTime) {
        // For this call of the game loop, calculate the final position before the final velocity, since the position
        // depends on velocity and so calculating the final velocity first will mess up the final position.

        // Calculate the new position using the physics kinematics equation d_f = d_i + v_i*t + (1/2)*a*t^2.

        // Calculate the new velocity using the physics kinematics equation v_f = v_i + a*t.

        double newPositionX = getPositionX() + getVelocityX() * deltaTime + 0.5 * getAccelerationX() * Math.pow(deltaTime, 2);
        setPositionX(newPositionX);
        double newVelocityX = getVelocityX() + getAccelerationX() * deltaTime;
        setVelocityX(newVelocityX);
        applyVelocityRestrictions();

        checkTileCollisions(Direction.X);

        double newPositionY = getPositionY() + getVelocityY() * deltaTime + 0.5 * getAccelerationY() * Math.pow(deltaTime, 2);
        setPositionY(newPositionY);
        double newVelocityY = getVelocityY() + getAccelerationY() * deltaTime;
        setVelocityY(newVelocityY);
        applyVelocityRestrictions();

        checkTileCollisions(Direction.Y);
    }

    private void applyVelocityRestrictions() {
        // Stop the sprite from moving too fast.
        applyMaxVelocity();
        // Stop the sprite from moving when the sprite's velocity becomes small enough.
        applyMinVelocity();
        // Make the sprite slow down when they are not accelerating.
        applyFriction();
    }

    private boolean collidingWithTile() {
        List<Tile<?>> overlappingTiles = game.getOverlappingTilesFromCurrentLevelMap(enclosingRectangle);
        for (Sprite<?> sprite : overlappingTiles) {
            if (CollisionChecker.isCollision(this, sprite)) {
                return true;
            }
        }

        return false;
    }

    private void checkTileCollisions(Direction direction) {
        if (spawning) {
            return;
        }

        List<Tile<?>> overlappingTiles = game.getOverlappingTilesFromCurrentLevelMap(enclosingRectangle);
        for (Sprite<?> sprite : overlappingTiles) {
            checkCollisions(direction, sprite);
        }
    }

    private void checkCollisions(Direction direction, Sprite<?> sprite) {
        Map<Integer, List<Rectangle2D.Double>> intersections = CollisionChecker.getIntersections(this, sprite);
        for (Map.Entry<Integer, List<Rectangle2D.Double>> collisionHitboxes: intersections.entrySet()) {
            for (Rectangle2D.Double collisionHitbox : collisionHitboxes.getValue()) {

                int collisionHitboxIndex = collisionHitboxes.getKey();
                Hitbox hitbox = hitboxes.get(collisionHitboxIndex);

                double buffer = 1;

                // Snap to the side along x.
                if (direction == Direction.X && getVelocityX() != 0) {
                    if (getVelocityX() > 0) {
                        // Moving right, snap to the left side.
                        setPositionX(collisionHitbox.getX() - hitbox.getWidth() - buffer);
                    } else if (getVelocityX() < 0) {
                        // Moving left, snap to the right side.
                        setPositionX(collisionHitbox.getX() + collisionHitbox.getWidth() + buffer);
                    }

                    setVelocityX(0);
                }

                // Snap to the side along y.
                if (direction == Direction.Y && getVelocityY() != 0) {
                    if (getVelocityY() > 0) {
                        // Moving down, snap to the top side.
                        setPositionY(collisionHitbox.getY() - hitbox.getHeight() - buffer);
                    } else if (getVelocityY() < 0) {
                        // Moving up, snap to the bottom side.
                        setPositionY(collisionHitbox.getY() + collisionHitbox.getHeight() + buffer);
                    }

                    setVelocityY(0);
                }
            }
        }
    }

    private void applyMaxVelocity() {
        if (Vector.getVectorMagnitude(getVelocity()) > maxVelocity) {
            // setVelocity(getVelocityScaledToMax());
            setVelocity(Vector.getVectorScaledToMax(getVelocity(), maxVelocity));
        }
    }

    private void applyFriction() {
        if (getAccelerationX() == 0) {
            setVelocityX(getVelocityX() * friction);
        }
        if (getAccelerationY() == 0) {
            setVelocityY(getVelocityY() * friction);
        }
    }

    private void applyMinVelocity() {
        if (getVelocityX() > 0 && getVelocityX() < minVelocity || getVelocityX() < 0 && getVelocityX() > -minVelocity) {
            setVelocityX(0);
        }
        if (getVelocityY() > 0 && getVelocityY() < minVelocity || getVelocityY() < 0 && getVelocityY() > -minVelocity) {
            setVelocityY(0);
        }
    }

    /**
     * Respawn the movable entity.
     *
     * @param resetPosition the reset position of the movable entity
     */
    protected final void respawn(boolean resetPosition) {
        setTransparency(0.5f);
        spawnCurrentTime = 0;
        spawning = true;
        if (resetPosition) {
            setPosition(spawnPosition);
        }
    }
}
