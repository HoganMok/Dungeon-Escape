package entity.factory;

import app.game.Game;
import entity.movable.Enemy;
import sprite.data.SpriteData;
import tileset.DungeonTileset;
import sprite.draw.DrawOffset;
import util.resources.ConfigReader;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The enemy factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class EnemyFactory extends EntityFactory {
    private final double assetScale = ConfigReader.getAssetScale();

    private final DungeonTileset.Enemy enemyType;

    private final double hitboxWidth;
    private final double hitboxHeight;
    private final double hitboxOffsetX;
    private final double hitboxOffsetY;
    private final double flipAnimationHorizontallyOffsetX;

    /**
     * Instantiates a new Enemy factory.
     *
     * @param game the game
     * @param enemyType the type of enemy to create
     */
    public EnemyFactory(Game game, DungeonTileset.Enemy enemyType) {
        super(game);

        this.enemyType = enemyType;

        this.hitboxWidth = switch(enemyType) {
            case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE -> 9;
            case BROWN_SLUDGE, GREEN_SLUDGE -> 14;
            case BEIGE_WALKER, CYAN_WALKER -> 8;
            case SORCERER -> 12;
            case ANGRY_TOOTH_CHEST -> 16;
            case ZOMBIE, ORC, TERROR -> 18;
            default -> 10;  // SKELETON, SKULL_MASK, ORC_1, ORC_2, TERROR_1, TERROR_2
        } * assetScale;
        this.hitboxHeight = switch(enemyType) {
            case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE -> 8;
            case BEIGE_WALKER, CYAN_WALKER, SKULL_MASK, ORC_1, TERROR_1-> 16;
            case ORC_2, ANGRY_TOOTH_CHEST -> 15;
            case SORCERER -> 17;
            case TERROR_2 -> 18;
            case ZOMBIE, ORC, TERROR -> 27;
            default -> 14;  // SKELETON, BROWN_SLUDGE, GREEN_SLUDGE
        } * assetScale;

        this.hitboxOffsetX = switch (enemyType) {
            case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, BEIGE_WALKER, CYAN_WALKER, SKULL_MASK, ORC_1, ORC_2 -> -4;
            case SKELETON, TERROR_1, TERROR_2 -> -3;
            case BROWN_SLUDGE, GREEN_SLUDGE -> -1;
            case SORCERER -> -2;
            case ZOMBIE, ORC, TERROR -> -7;
            default -> 0;  // ANGRY_TOOTH_CHEST
        } * assetScale;
        this.hitboxOffsetY = switch(enemyType) {
            case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE -> -8;
            case BEIGE_WALKER, CYAN_WALKER, SKULL_MASK, ORC_1, TERROR_1 -> -16;
            case ORC_2 -> -17;
            case SORCERER -> -15;
            case TERROR_2 -> -14;
            case ANGRY_TOOTH_CHEST -> -1;
            case ZOMBIE, ORC, TERROR -> -22;
            default -> -18;  // SKELETON, BROWN_SLUDGE, GREEN_SLUDGE
        } * assetScale;

        this.flipAnimationHorizontallyOffsetX = switch(enemyType) {
            case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE -> 1;
            case SKULL_MASK, ORC_1, ORC_2 -> 2;
            default -> 0;  // SKELETON, BROWN_SLUDGE, GREEN_SLUDGE, BEIGE_WALKER, CYAN_WALKER, SORCERER, TERROR_1,
                           // TERROR_2, ANGRY_TOOTH_CHEST, ZOMBIE, ORC, TERROR
        } * assetScale;
    }

    /**
     * Create an enemy based on the character, since different character has different image height and width, and also
     * the size of the hitbox is different. There are two arrays to store the idling and moving images of the enemy
     *
     * @param position  the position of an enemy
     * @return the enemy
     */
    public Enemy create(Point2D.Double position) {
        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(position.getX(), position.getY(), hitboxWidth,
                hitboxHeight);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>();
        relativeHitboxes.add(new Rectangle2D.Double(0, 0, hitboxWidth, hitboxHeight));

        Point2D.Double animationOffset = new Point2D.Double(hitboxOffsetX, hitboxOffsetY);

        Map<Enemy.EnemyAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(Enemy.EnemyAnimation.IDLE, animationOffset);
        offsets.put(Enemy.EnemyAnimation.MOVE, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(flipAnimationHorizontallyOffsetX, 0);
        DrawOffset<Enemy.EnemyAnimation> drawOffset = new DrawOffset<>(offsets, flipAnimationHorizontallyOffset);

        List<BufferedImage> animationImages = game.getEnemyImages(enemyType);

        Map<Enemy.EnemyAnimation, List<Integer>> animationImageNumbers = new HashMap<>();
        List<Integer> animationIdle = new ArrayList<>();
        switch (enemyType){
            case ANGRY_TOOTH_CHEST -> animationIdle.add(0);
            default -> {
                // BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, SKELETON, SKULL_MASK, ORC_1, ORC_2, TERROR_1, TERROR_2,
                // BROWN_SLUDGE, GREEN_SLUDGE, BEIGE_WALKER, CYAN_WALKER, SORCERER, ZOMBIE, ORC, TERROR
                for (int i = 0; i < 4; i++) {
                    animationIdle.add(i);
                }
            }
        };
        List<Integer> animationMove = new ArrayList<>();
        switch(enemyType) {
            case BROWN_SLUDGE, GREEN_SLUDGE, BEIGE_WALKER, CYAN_WALKER, SORCERER -> {
                for (int i = 0; i < 4; i++) {
                    animationMove.add(i);
                }
            }
            case ANGRY_TOOTH_CHEST -> {
                for(int i = 0; i < 3; i++){
                    animationMove.add(i);
                }
            }
            default -> {
                // BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, SKELETON, SKULL_MASK, ORC_1, ORC_2, TERROR_1, TERROR_2, ZOMBIE,
                // ORC, TERROR
                for (int i = 4; i < 8; i++) {
                    animationMove.add(i);
                }
            }
        };
        animationImageNumbers.put(Enemy.EnemyAnimation.IDLE, animationIdle);
        animationImageNumbers.put(Enemy.EnemyAnimation.MOVE, animationMove);

        SpriteData<Enemy.EnemyAnimation> spriteData = new SpriteData<>(enclosingRectangle, relativeHitboxes, drawOffset,
                animationImages, animationImageNumbers, Enemy.EnemyAnimation.IDLE);

        return new Enemy(game, spriteData);
    }
}
