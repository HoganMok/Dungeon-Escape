package tileset;

import app.game.Game;
import level.tile.BasicTile;
import level.tile.Tile;
import sprite.draw.DrawOffset;
import level.MapManager;
import sprite.data.SpriteData;
import util.resources.ConfigReader;
import util.resources.ResourcesReader;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Stores data loaded in from a tileset image.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public abstract class Tileset {
    private final List<BufferedImage> tilesetImages;
    /**
     * The Tileset image.
     */
    protected final BufferedImage tilesetImage;

    /**
     * The Asset tile size.
     */
    protected final int assetTileSize;
    /**
     * The Asset scale.
     */
    protected final double assetScale;

    /**
     * The Tileset type.
     */
    protected final MapManager.TilesetType tilesetType;

    /**
     * The Tile images.
     */
    Map<Integer, List<BufferedImage>> tileImages = new HashMap<>();

    private final Game game;

    /**
     * Instantiates a new Tileset and create a list of tileset image.
     *
     * @param game          the game
     * @param tilesetType   the tileset type
     * @param fileExtension the file extension that contains the tileset
     * @param assetScale    the asset scale
     */
    public Tileset(Game game, MapManager.TilesetType tilesetType, String fileExtension, double assetScale) {
        this.game = game;

        this.tilesetType = tilesetType;

        tilesetImage = new ResourcesReader().readImage(
                "tilesets/" + MapManager.getTilesetName(tilesetType) + "/tileset." + fileExtension);

        tilesetImages = new ArrayList<>();

        assetTileSize = ConfigReader.getTileSize(tilesetType);
        this.assetScale = assetScale;

        for (int y = 0; y < tilesetImage.getHeight(); y += assetTileSize) {
            for (int x = 0; x < tilesetImage.getWidth(); x += assetTileSize) {
                // Load in the tile at the current (x, y) position on the tileset image.
                BufferedImage tileImage = tilesetImage.getSubimage(x, y, assetTileSize, assetTileSize);
                tilesetImages.add(scaleImage(tileImage, assetScale));
            }
        }

        loadTileImages();
    }

    /**
     * Scales image of tiles.
     *
     * @param image the image of a tile
     * @param scale the scale
     * @return the buffered image after scaling
     */
    protected BufferedImage scaleImage(BufferedImage image, double scale) {
        int scaledWidth = (int) (image.getWidth() * scale);
        int scaledHeight = (int) (image.getHeight() * scale);

        // Create a new scaled up image.
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, image.getType());
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();

        return scaledImage;
    }

    /**
     * Gets asset tile size.
     *
     * @return the asset tile size
     */
    public int getAssetTileSize() {
        return assetTileSize;
    }

    /**
     * Gets scaled tile size.
     *
     * @return the scaled tile size
     */
    public int getScaledTileSize() {
        return (int) (getAssetTileSize() * assetScale);
    }

    /**
     * Gets image from the specific tile in the tileset.
     *
     * @param tileNumber the tile number
     * @return the tileset image
     */
    public BufferedImage getTilesetImage(int tileNumber) {
        return tilesetImages.get(tileNumber);
    }

    /**
     * Gets tile's animation images.
     *
     * @param tileNumber the tile number
     * @return the animation images
     */
    public abstract List<BufferedImage> getTileAnimationImages(int tileNumber);

    /**
     * Load images list into a list of Buffered Image.
     *
     * @param tileX       the tile x
     * @param tileY       the tile y
     * @param tileWidth   the tile width
     * @param tileHeight  the tile height
     * @param imageAmount the image amount
     * @return the list of tile set images
     */
    protected List<BufferedImage> loadImages(double tileX, double tileY, double tileWidth, double tileHeight, int imageAmount) {
        List<BufferedImage> images = new ArrayList<>();
        for (int i = 0; i < imageAmount; i++) {
            BufferedImage characterImage = tilesetImage.getSubimage((int) (tileX * assetTileSize + i * tileWidth * assetTileSize),
                    (int) (tileY * assetTileSize), (int) (tileWidth * assetTileSize), (int) (tileHeight * assetTileSize));
            images.add(scaleImage(characterImage, assetScale));
        }

        return images;
    }

    private void loadTileImages() {
        for (int tileNumber : ConfigReader.getTileNumbers(tilesetType)) {
            tileImages.put(tileNumber, getTileAnimationImages(tileNumber));
        }
    }

    /**
     * Gets tile number from the tileset.
     *
     * @param tileNumber the tile number
     * @return the tile number
     */
    protected abstract int getTrueTileNumber(int tileNumber);

    /**
     * Create tile with its hit box, the hit box offset and animations.
     *
     * @param tileNumber      the tile number
     * @param tileCoordinates the tile coordinates
     * @return the tile
     */
    public Tile<?> createTile(int tileNumber, Point2D.Double tileCoordinates) {
        int trueTileNumber = getTrueTileNumber(tileNumber);
        List<BufferedImage> animationImages = getTileAnimationImages(trueTileNumber);

        // The variable tileHitbox will be null if there is no tile hitbox.
        List<Rectangle2D.Double> tileRelativeHitboxes = ConfigReader.getTileRelativeHitbox(tilesetType, trueTileNumber);

        Map<BasicTile.TileAnimation, List<Integer>> animationImageNumbers = new HashMap<>();
        List<Integer> tileAnimationIdle = new ArrayList<>();
        for (int i = 0; i < animationImages.size(); i++) {
            tileAnimationIdle.add(i);
        }
        animationImageNumbers.put(BasicTile.TileAnimation.IDLE, tileAnimationIdle);

        double tileSize = getAssetTileSize() * assetScale;

        Rectangle2D.Double enclosingRectangle = new Rectangle2D.Double(tileCoordinates.getX() * tileSize,
                tileCoordinates.getY() * tileSize, tileSize, tileSize);

        List<Rectangle2D.Double> relativeHitboxes = new ArrayList<>(tileRelativeHitboxes);

        Point2D.Double animationOffset = new Point2D.Double(0, 0);

        Map<BasicTile.TileAnimation, Point2D.Double> offsets = new HashMap<>();
        offsets.put(BasicTile.TileAnimation.IDLE, animationOffset);
        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(0, 0);
        DrawOffset<BasicTile.TileAnimation> drawOffset = new DrawOffset<>(offsets,
                flipAnimationHorizontallyOffset);

        SpriteData<BasicTile.TileAnimation> spriteData = new SpriteData<>(enclosingRectangle,
                relativeHitboxes, drawOffset, animationImages, animationImageNumbers,
                BasicTile.TileAnimation.IDLE);

        return new BasicTile(game, spriteData, trueTileNumber);
    }
}
