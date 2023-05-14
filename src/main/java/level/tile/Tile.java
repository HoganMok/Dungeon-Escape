package level.tile;

import app.game.Game;
import sprite.Sprite;
import sprite.data.SpriteData;

import java.awt.*;

/**
 * Stores the data for a single tile.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 *
 * @param <T> the animations for the tile.
 */
public abstract class Tile<T extends Enum<T>> extends Sprite<T> {
    // If rectangle has width 0 and height 0, then the tile can not detect collisions (and thus should not be checked
    // for collisions).

    private final boolean isUpdatable;

    private final int tileNumber;

    /**
     * Instantiates a new Tile.
     *
     * @param game        the game
     * @param spriteData  the data to create this sprite
     * @param isUpdatable is the tile updatable or not
     * @param tileNumber  the tile number
     */
    public Tile(Game game, SpriteData<T> spriteData, boolean isUpdatable, int tileNumber) {
        super(game, spriteData);

        this.isUpdatable = isUpdatable;

        this.tileNumber = tileNumber;
    }

    /**
     * Draw a tile.
     *
     * @param graphics2D   the graphics of a tile
     * @param debugMode    if debug mode is active
     * @param deltaTime    the delta time since the last game update
     * @param transparency if a tile is transparent
     */
    public void draw(Graphics2D graphics2D, boolean debugMode, double deltaTime, float transparency) {
        setTransparency(transparency);

        super.draw(graphics2D, debugMode, deltaTime);
    }

    public abstract void update(double deltaTime);

    /**
     * Is updatable or not.
     *
     * @return the boolean
     */
    public boolean isUpdatable() {
        return isUpdatable;
    }
}
