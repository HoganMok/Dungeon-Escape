package entity.factory;

import app.game.Game;
import entity.movable.item.Punishment;
import sprite.draw.DrawOffset;
import sprite.data.SpriteData;
import util.resources.ConfigReader;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The punishment factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class PunishmentFactory extends EntityFactory {
    private final double assetScale = ConfigReader.getAssetScale();
    private final double hitboxWidth = 16 * assetScale;
    private final double hitboxHeight = 16 * assetScale;

    private final double hitboxOffsetX = 0 * assetScale;
    private final double hitboxOffsetY = 0 * assetScale;

    /**
     * Instantiates a new Punishment factory.
     *
     * @param game the game
     */
    public PunishmentFactory(Game game) {
        super(game);
    }

    /**
     *Creates a punishment by setting up its hitbox, adjust its flipped animations, and also creating an array
     * to save the animated images of the punishment.
     *
     * @param position the position of the punishment
     * @return the punishment
     */
    public Punishment create(Point2D.Double position) {
        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(position.getX(), position.getY(), hitboxWidth,
                hitboxHeight);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>();
        relativeHitboxes.add(new Rectangle2D.Double(0, 0, hitboxWidth, hitboxHeight));

        Point2D.Double animationOffset = new Point2D.Double(hitboxOffsetX, hitboxOffsetY);

        Map<Punishment.PunishmentAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(Punishment.PunishmentAnimation.BOB, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(0 * assetScale, 0);
        DrawOffset<Punishment.PunishmentAnimation> drawOffset = new DrawOffset<>(
                offsets, flipAnimationHorizontallyOffset);

        List<BufferedImage> animationImages = game.getPunishmentImages();

        Map<Punishment.PunishmentAnimation, List<Integer>> animationImageNumbers = new HashMap<>();
        List<Integer> PunishmentAnimationBOB = new ArrayList<>();
        PunishmentAnimationBOB.add(0);

        animationImageNumbers.put(Punishment.PunishmentAnimation.BOB, PunishmentAnimationBOB);

        SpriteData<Punishment.PunishmentAnimation> spriteData = new SpriteData<>(enclosingRectangle, relativeHitboxes,
                drawOffset, animationImages, animationImageNumbers, Punishment.PunishmentAnimation.BOB);

        return new Punishment(game, spriteData);
    }
}
