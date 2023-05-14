package level.tile;

import app.game.Game;
import sprite.data.SpriteData;

/**
 * A basic tile which can only have a hitbox and be animated.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class BasicTile extends Tile<BasicTile.TileAnimation> {
    /**
     * The enum Tile animation.
     */
    public enum TileAnimation {
        /**
         * Idle tile animation.
         */
        IDLE
    }

    /**
     * Instantiates a new Basic tile.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     * @param tileNumber the tile number of a tile
     */
    public BasicTile(Game game, SpriteData<TileAnimation> spriteData, int tileNumber) {
        super(game, spriteData, false, tileNumber);

        // There is no need to update animation frames if there is only one animation frame.
        if (spriteData.animationImages().size() == 1) {
            pauseAnimation();
        }
    }

    @Override
    public void update(double deltaTime) {
    }
}
