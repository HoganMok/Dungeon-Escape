package entity.movable;

import app.game.Game;
import entity.Entity;
import sprite.Sprite;
import sprite.data.SpriteData;
import level.tile.Tile;
import tileset.DungeonTileset;
import util.physics.CollisionChecker;
import util.physics.Vector;
import util.resources.ConfigReader;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An enemy in the game which moves towards the player.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class Enemy extends MovableEntity<Enemy.EnemyAnimation> {
    /**
     * The enum Enemy animation.
     */
    public enum EnemyAnimation {
        /**
         * Idle enemy animation.
         */
        IDLE,
        /**
         * Move enemy animation.
         */
        MOVE
    }

    /**
     * The Enemy type.
     */
    public DungeonTileset.Enemy enemyType;
    private final Player player;

    // The value the acceleration is set to when the player wants to move in a certain direction.
    private final double acceleration = ConfigReader.getEnemyAcceleration();

    // Only accelerate towards the player if the distance to the player is smaller or equal to this value.
    private final double trackingDistance = ConfigReader.getEnemyTrackingDistance();
    // To save computation time, compared the squared distance with trackingDistance squared, so a square root does not
    // need to be computed.
    private final double trackingDistanceSquared = Math.pow(trackingDistance, 2);

    /**
     * Instantiates a new Enemy and set it up in Entity.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Enemy(Game game, SpriteData<EnemyAnimation> spriteData) {
        super(game, spriteData, ConfigReader.getEnemyFriction(), ConfigReader.getEnemyMaxVelocity(),
                ConfigReader.getEnemyMinVelocity());

        player = game.getPlayer();

        game.addEnemy(spriteId);
    }
    @Override
    public void destroy() {
        game.destroyEnemy(spriteId);
        super.destroy();
    }

    @Override
    public void update(double deltaTime) {
        updateAcceleration();
        updateAnimations();
        if (!spawning && CollisionChecker.isCollision(this, player)) {
            game.endGame();
        }
        super.update(deltaTime);
    }

    private void updateAcceleration() {
        // Stop accelerating if the player is far enough away.
        if (player.getPosition().distanceSq(getPosition()) > trackingDistanceSquared) {
            setAcceleration(0, 0);

            return;
        }

        // Make the enemy accelerate towards the player.
        Point2D.Double accelerationTowardsPlayer = new Point2D.Double(
                player.getCenterX() - getCenterX(),
                player.getCenterY() - getCenterY());
        setAcceleration(accelerationTowardsPlayer);

        double enemyRepulsion = acceleration * 0.3;

        Set<Integer> otherEnemies = new HashSet<>(game.getEnemySpriteIds());
        otherEnemies.remove(spriteId);

        for (int spriteId : otherEnemies) {
            Entity<?> enemy = game.getEntity(spriteId);
            double scaleX = getCenterX() - enemy.getCenterX();
            if (scaleX == 0) {
                scaleX = 1;
            }
            double scaleY = getCenterY() - enemy.getCenterY();
            if (scaleY == 0) {
                scaleY = 1;
            }
            setAcceleration(
                    getAccelerationX() + 1 / scaleX * enemyRepulsion,
                    getAccelerationY() + 1 / scaleY * enemyRepulsion);
        }

        setAcceleration(Vector.getVectorScaledToMax(getAcceleration(), acceleration));
    }

    private void updateAnimations() {
        if (isAccelerating() && !spawning) {
            setCurrentAnimation(EnemyAnimation.MOVE);
        } else {
            setCurrentAnimation(EnemyAnimation.IDLE);
        }

        if (getAccelerationX() > 0) {
            setFlipAnimationHorizontally(false);
        } else if (getAccelerationX() < 0) {
            setFlipAnimationHorizontally(true);
        }
    }

    /**
     * Respawn the enemy if it flipped onto walls or any blocking tiles.
     */
    public void respawnIfFlipOntoTile() {
        List<Tile<?>> overlappingTiles = game.getOverlappingTilesFromNextLevelMap(enclosingRectangle);
        for (Sprite<?> sprite : overlappingTiles) {
            if (CollisionChecker.isCollision(this, sprite)) {
                respawn(false);
            }
        }
    }
}
