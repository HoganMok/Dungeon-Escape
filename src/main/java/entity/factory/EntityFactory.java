package entity.factory;

import app.game.Game;
import entity.Entity;

import java.awt.geom.Point2D;

/**
 * The entity factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public abstract class EntityFactory {

    protected final Game game;

    /**
     * Instantiates a new entity factory.
     *
     * @param game the game
     */
    public EntityFactory(Game game) {
        this.game = game;
    }

    /**
     *Creates an entity by setting up its hitbox, adjust its flipped animations, and also creating an array
     * to save the animated images of the entity.
     *
     * @param position the position of the entity
     * @return the entity
     */
    public abstract Entity<?> create(Point2D.Double position);
}
