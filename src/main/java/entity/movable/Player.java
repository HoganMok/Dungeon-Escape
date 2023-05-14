package entity.movable;

import app.game.Game;
import sprite.Sprite;
import app.game.InputManager.InputType;
import app.game.InputManager.InputName;
import sprite.data.SpriteData;
import level.tile.Tile;
import util.physics.CollisionChecker;
import util.resources.ConfigReader;

import java.util.List;

/**
 * The player of the game.
 * <p>
 * Handles updating its acceleration and animations. Also, the class checks if the player can flip the level map.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class Player extends MovableEntity<Player.PlayerAnimation> {
    /**
     * The enum Player animation.
     */
    public enum PlayerAnimation {
        /**
         * Idle player animation.
         */
        IDLE,
        /**
         * Move player animation.
         */
        MOVE
    }

    // The value the acceleration is set to when the player wants to move in a certain direction.
    private final double acceleration = ConfigReader.getPlayerAcceleration();

    /**
     * Instantiates a new Player and set it up in Entity.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Player(Game game, SpriteData<PlayerAnimation> spriteData) {
        super(game, spriteData, ConfigReader.getPlayerFriction(), ConfigReader.getPlayerMaxVelocity(),
                ConfigReader.getPlayerMinVelocity());

        cameraManager.setTarget(spriteId);
    }

    @Override
    public void update(double deltaTime) {
        updateAcceleration();
        updateAnimations();
        super.update(deltaTime);
    }

    private void updateAcceleration() {
        // Set the player's current acceleration based on events such as which keys are currently being pressed.

        if (game.getCurrentGameInput(InputName.LEFT, InputType.IS_PRESSED)) {
            setAccelerationX(-acceleration);
        } else if (game.getCurrentGameInput(InputName.RIGHT, InputType.IS_PRESSED)) {
            setAccelerationX(acceleration);
        } else {
            setAccelerationX(0);
        }

        if (game.getCurrentGameInput(InputName.UP, InputType.IS_PRESSED)) {
            setAccelerationY(-acceleration);
        } else if (game.getCurrentGameInput(InputName.DOWN, InputType.IS_PRESSED)) {
            setAccelerationY(acceleration);
        } else {
            setAccelerationY(0);
        }

        if (getAccelerationX() != 0 && getAccelerationY() != 0) {
            setAccelerationX(getAccelerationX() / Math.sqrt(2));
            setAccelerationY(getAccelerationY() / Math.sqrt(2));
        }
    }

    private void updateAnimations() {
        if (isAccelerating()) {
            setCurrentAnimation(PlayerAnimation.MOVE);
        } else {
            setCurrentAnimation(PlayerAnimation.IDLE);
        }

        if (getAccelerationX() > 0) {
            setFlipAnimationHorizontally(false);
        } else if (getAccelerationX() < 0) {
            setFlipAnimationHorizontally(true);
        }
    }

    /**
     * Can the player flip to the next map.
     *
     * @return the boolean
     */
    public boolean canFlipToNextMap() {
        List<Tile<?>> overlappingTiles = game.getOverlappingTilesFromNextLevelMap(enclosingRectangle);
        for (Sprite<?> sprite : overlappingTiles) {
            if (CollisionChecker.isCollision(this, sprite)) {
                return false;
            }
        }

        return true;
    }
}
