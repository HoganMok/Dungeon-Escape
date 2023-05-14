package entity.factory;

import app.game.Game;
import entity.movable.item.BonusReward;
import sprite.draw.DrawOffset;
import sprite.data.SpriteData;
import util.resources.ConfigReader;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The bonus reward factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class BonusRewardFactory extends EntityFactory {
    private final double assetScale = ConfigReader.getAssetScale();

    private final double hitboxWidth = 6 * assetScale;
    private final double hitboxHeight = 7 * assetScale;

    private final double hitboxOffsetX = -1 * assetScale;
    private final double hitboxOffsetY = -1 * assetScale;

    /**
     * Instantiates a new Bonus reward factory.
     *
     * @param game the game
     */
    public BonusRewardFactory(Game game) {
        super(game);
    }

    /**
     * Creates bonus reward by setting up its hitbox and having an array to store the bobbing animation of the
     * bonus reward.
     *
     * @param position the position
     * @return the bonus reward
     */
    public BonusReward create(Point2D.Double position) {
        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(position.getX(), position.getY(), hitboxWidth,
                hitboxHeight);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>();
        relativeHitboxes.add(new Rectangle2D.Double(0, 0, hitboxWidth, hitboxHeight));

        Point2D.Double animationOffset = new Point2D.Double(hitboxOffsetX, hitboxOffsetY);

        Map<BonusReward.BonusRewardAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(BonusReward.BonusRewardAnimation.BOB, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(0 * assetScale, 0);
        DrawOffset<BonusReward.BonusRewardAnimation> drawOffset = new DrawOffset<>(
                offsets, flipAnimationHorizontallyOffset);

        List<BufferedImage> animationImages = game.getBonusRewardImages();

        Map<BonusReward.BonusRewardAnimation, List<Integer>> animationImageNumbers = new HashMap<>();
        List<Integer> BonusRewardAnimationBob = new ArrayList<>();
        BonusRewardAnimationBob.add(0);
        BonusRewardAnimationBob.add(3);
        BonusRewardAnimationBob.add(2);
        BonusRewardAnimationBob.add(3);

        animationImageNumbers.put(BonusReward.BonusRewardAnimation.BOB, BonusRewardAnimationBob);

        SpriteData<BonusReward.BonusRewardAnimation> spriteData = new SpriteData<>(enclosingRectangle,
                relativeHitboxes, drawOffset, animationImages, animationImageNumbers,
                BonusReward.BonusRewardAnimation.BOB);

        return new BonusReward(game, spriteData);
    }
}
