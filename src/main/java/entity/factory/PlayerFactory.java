package entity.factory;

import app.game.Game;
import entity.movable.Player;
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
 * The player factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class PlayerFactory extends EntityFactory {
    private final double assetScale = ConfigReader.getAssetScale();

    private final double hitboxWidth = 12 * assetScale;
    private final double hitboxHeight = 16 * assetScale;

    private final double hitboxOffsetX = -3 * assetScale;
    private final double hitboxOffsetY = -16 * assetScale;

    private final DungeonTileset.Player playerType;

    /**
     * Instantiates a new Player factory.
     *
     * @param game the game
     * @param playerType the type of player to create
     */
    public PlayerFactory(Game game, DungeonTileset.Player playerType) {
        super(game);

        this.playerType = playerType;
    }

    /**
     * Create a player by setting up its hitbox, adjust it flipped animations, and also creating two array to store
     * the idling and moving images of the player.
     *
     *
     * @param position  the position
     * @return the player
     */
    public Player create(Point2D.Double position) {
        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(position.getX(), position.getY(), hitboxWidth,
                hitboxHeight);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>();
        relativeHitboxes.add(new Rectangle2D.Double(0, 0, hitboxWidth, hitboxHeight));

        Point2D.Double animationOffset = new Point2D.Double(hitboxOffsetX, hitboxOffsetY);

        Map<Player.PlayerAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(Player.PlayerAnimation.IDLE, animationOffset);
        offsets.put(Player.PlayerAnimation.MOVE, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(2 * assetScale, 0);
        DrawOffset<Player.PlayerAnimation> drawOffset = new DrawOffset<>(offsets, flipAnimationHorizontallyOffset);

        List<BufferedImage> animationImages = game.getPlayerImages(playerType);

        Map<Player.PlayerAnimation, List<Integer>> animationImageNumbers = new HashMap<>();
        List<Integer> playerAnimationIdle = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            playerAnimationIdle.add(i);
        }
        List<Integer> playerAnimationMove = new ArrayList<>();
        for (int i = 4; i < 8; i++) {
            playerAnimationMove.add(i);
        }
        animationImageNumbers.put(Player.PlayerAnimation.IDLE, playerAnimationIdle);
        animationImageNumbers.put(Player.PlayerAnimation.MOVE, playerAnimationMove);

        SpriteData<Player.PlayerAnimation> spriteData = new SpriteData<>(enclosingRectangle, relativeHitboxes, drawOffset,
                animationImages, animationImageNumbers, Player.PlayerAnimation.IDLE);

        return new Player(game, spriteData);
    }
}
