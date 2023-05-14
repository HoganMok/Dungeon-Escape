package entity.factory;

import app.game.Game;
import entity.Door;
import sprite.draw.DrawOffset;
import sprite.data.SpriteData;
import tileset.DungeonTileset;
import util.resources.ConfigReader;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * The door factory.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class DoorFactory extends EntityFactory {
    private final double assetScale = ConfigReader.getAssetScale();

    private final double hitboxOffsetX = 0 * assetScale;
    private final double hitboxOffsetY = 0 * assetScale;

    private final DungeonTileset.Door doorType;

    /**
     * Instantiates a new Door factory.
     *
     * @param game the game
     * @param doorType the type of door to create
     */
    public DoorFactory(Game game, DungeonTileset.Door doorType) {
        super(game);

        this.doorType = doorType;
    }

    /**
     *Creates a door by setting up its hitbox. There are two type of animations saved in two arrays, either open and
     *close. The animation of the door is set to be close when creating it Once the player collected all the
     *keys(regular reward) required in that level, the animation will change from close to open.
     *
     * @param position  the position
     * @return the door
     */
    public Door create(Point2D.Double position) {
        double hitboxWidth = switch (doorType){
            case DOOR_2, DOOR_3 -> 16;
            default -> 32;
        }* assetScale;

        double hitboxHeight = 32 * assetScale;

        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(position.getX(), position.getY(), hitboxWidth,
                hitboxHeight);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>();
        relativeHitboxes.add(new Rectangle2D.Double(0, 0, hitboxWidth, hitboxHeight));

        Point2D.Double animationOffset = new Point2D.Double(hitboxOffsetX, hitboxOffsetY);

        Map<Door.DoorAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(Door.DoorAnimation.OPEN, animationOffset);
        offsets.put(Door.DoorAnimation.CLOSE, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(0 * assetScale, 0);
        DrawOffset<Door.DoorAnimation> drawOffset = new DrawOffset<>(
                offsets, flipAnimationHorizontallyOffset);

        List<BufferedImage> animationImages = game.getDoorImages(doorType);

        Map<Door.DoorAnimation, List<Integer>> animationImageNumbers = new HashMap<>();

        List<Integer> animationOpen = new ArrayList<>();
        animationOpen.add(1);

        List<Integer> animationClose = new ArrayList<>();
        animationClose.add(0);

        animationImageNumbers.put(Door.DoorAnimation.OPEN, animationOpen);
        animationImageNumbers.put(Door.DoorAnimation.CLOSE, animationClose);

        SpriteData<Door.DoorAnimation> spriteData = new SpriteData<>(enclosingRectangle, relativeHitboxes, drawOffset,
                animationImages, animationImageNumbers, Door.DoorAnimation.CLOSE);

        return new Door(game, spriteData);
    }
}
