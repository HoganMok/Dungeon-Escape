package entity;

import app.game.Game;
import sprite.Sprite;
import sprite.data.SpriteData;

/**
 * An entity of the game. For example, the player, enemies, and items.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 *
 * @param <T> the animation type
 */
public abstract class Entity<T extends Enum<T>> extends Sprite<T> {
    /**
     * Instantiates a new Entity and set it up in sprite.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Entity(Game game, SpriteData<T> spriteData) {
        super(game, spriteData);

        game.addEntity(spriteId, this);
    }

    public abstract void update(double deltaTime);

    /**
     * Destroy the entity.
     */
    public void destroy() {
        game.destroyEntity(spriteId);
    }
}
