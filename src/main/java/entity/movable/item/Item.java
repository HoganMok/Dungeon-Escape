package entity.movable.item;

import app.game.Game;
import entity.movable.MovableEntity;
import sprite.data.SpriteData;
import util.physics.CollisionChecker;

import java.awt.geom.Point2D;

/**
 * A basic game item.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 *
 * @param <T> the animation type
 */
public abstract class Item<T extends Enum<T>> extends MovableEntity<T> {
    private final double secondsPerBob = 1.5;
    private double currentBobDuration = 0;
    private final double bobDisplacementY = 7;
    private Point2D.Double bobCenter;

    /**
     * Instantiates a new Item.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Item(Game game, SpriteData<T> spriteData) {
        super(game, spriteData, 1, 0, 0);

        bobCenter = getPosition();
    }

    @Override
    public void update(double deltaTime) {
        if (!isActive) {
            return;
        }

        bob(deltaTime);

        if (CollisionChecker.isCollision(this, game.getPlayer())) {
            this.pickedUp();
            this.destroy();
        }
    }

    /**
     * Keep moving the item up and down.
     *
     * @param deltaTime the delta time
     */
    protected void bob(double deltaTime) {
        currentBobDuration = (currentBobDuration + deltaTime) % secondsPerBob;
        setPositionY(
                bobCenter.getY() + Math.sin((currentBobDuration / secondsPerBob) * 2 * Math.PI) * bobDisplacementY);
        super.update(deltaTime);
    }

    /**
     * The item is picked up.
     */
    protected abstract void pickedUp();
}
