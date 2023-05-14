package entity.factory;

import app.game.Game;
import entity.movable.item.RegularReward;
import sprite.draw.DrawOffset;
import sprite.data.SpriteData;
import util.resources.ConfigReader;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The regular reward factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class RegularRewardFactory extends EntityFactory {
    private final double assetScale = ConfigReader.getAssetScale();

    private final double hitboxWidth = 6 * assetScale;
    private final double hitboxHeight = 12 * assetScale;

    private final double hitboxOffsetX = -5 * assetScale;
    private final double hitboxOffsetY = -2 * assetScale;

    /**
     * Instantiates a new Regular reward factory.
     *
     * @param game the game
     */
    public RegularRewardFactory(Game game) {
        super(game);
    }

    /**
     * Creates a regular reward by setting up its hitbox, adjust its flipped animations, and also creating an array
     * to save the animated images of the regular reward.
     *
     * @param position the position of the regular reward
     * @return the regular reward
     */
    public RegularReward create(Point2D.Double position) {
        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(position.getX(), position.getY(), hitboxWidth,
                hitboxHeight);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>();
        relativeHitboxes.add(new Rectangle2D.Double(0, 0, hitboxWidth, hitboxHeight));

        Point2D.Double animationOffset = new Point2D.Double(hitboxOffsetX, hitboxOffsetY);

        Map<RegularReward.RegularRewardAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(RegularReward.RegularRewardAnimation.BOB, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(0 * assetScale, 0);
        DrawOffset<RegularReward.RegularRewardAnimation> drawOffset = new DrawOffset<>(
                offsets, flipAnimationHorizontallyOffset);

        List<BufferedImage> animationImages = game.getRegularRewardImages();

        Map<RegularReward.RegularRewardAnimation, List<Integer>> animationImageNumbers = new HashMap<>();
        List<Integer> regularRewardAnimationBob = new ArrayList<>();
        regularRewardAnimationBob.add(0);

        animationImageNumbers.put(RegularReward.RegularRewardAnimation.BOB, regularRewardAnimationBob);

        SpriteData<RegularReward.RegularRewardAnimation> spriteData = new SpriteData<>(enclosingRectangle,
                relativeHitboxes, drawOffset, animationImages, animationImageNumbers,
                RegularReward.RegularRewardAnimation.BOB);

        return new RegularReward(game, spriteData);
    }
}
