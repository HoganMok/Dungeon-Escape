package tileset;

import app.game.Game;
import level.MapManager;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Stores a dungeon tileset.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class DungeonTileset extends Tileset {
    /**
     * The enum Player.
     */
    public enum Player {
        /**
         * Green 1 player.
         */
        GREEN_1,
        /**
         * Green 2 player.
         */
        GREEN_2,
        /**
         * Knight pink player.
         */
        KNIGHT_PINK,
        /**
         * Knight orange player.
         */
        KNIGHT_ORANGE,
        /**
         * Wizard 1 player.
         */
        WIZARD_1,
        /**
         * Wizard 2 player.
         */
        WIZARD_2,
        /**
         * Dino 1 player.
         */
        DINO_1,
        /**
         * Dino 2 player.
         */
        DINO_2,
        /**
         * Green 3 player.
         */
        GREEN_3
    }

    /**
     * The enum Enemy.
     */
    public enum Enemy {
        /**
         * Beige square enemy.
         */
        BEIGE_SQUARE,
        /**
         * Green square enemy.
         */
        GREEN_SQUARE,
        /**
         * Red square enemy.
         */
        RED_SQUARE,
        /**
         * Skeleton enemy.
         */
        SKELETON,
        /**
         * Brown sludge enemy.
         */
        BROWN_SLUDGE,
        /**
         * Green sludge enemy.
         */
        GREEN_SLUDGE,
        /**
         * Beige walker enemy.
         */
        BEIGE_WALKER,
        /**
         * Cyan walker enemy.
         */
        CYAN_WALKER,
        /**
         * Skull mask enemy.
         */
        SKULL_MASK,
        /**
         * Orc 1 enemy.
         */
        ORC_1,
        /**
         * Orc 2 enemy.
         */
        ORC_2,
        /**
         * Sorcerer enemy.
         */
        SORCERER,
        /**
         * Terror 1 enemy.
         */
        TERROR_1,
        /**
         * Terror 2 enemy.
         */
        TERROR_2,
        /**
         * Angry tooth chest enemy.
         */
        ANGRY_TOOTH_CHEST,
        /**
         * Zombie enemy.
         */
        ZOMBIE,
        /**
         * Orc enemy.
         */
        ORC,
        /**
         * Terror enemy.
         */
        TERROR
    }

    /**
     * The enum Door.
     */
    public enum Door {
        /**
         * Door 1 door.
         */
        DOOR_1,
        /**
         * Door 2 door.
         */
        DOOR_2,
        /**
         * Door 3 door.
         */
        DOOR_3
    }

    private final Map<Player, List<BufferedImage>> playerImages = new HashMap<>();
    private final Map<Enemy, List<BufferedImage>> enemyImages = new HashMap<>();
    private final List<BufferedImage> regularRewardImages = new ArrayList<>();
    private final List<BufferedImage> bonusRewardImages = new ArrayList<>();
    private final List<BufferedImage> punishmentImages = new ArrayList<>();
    private final Map<Door, List<BufferedImage>> doorImages = new HashMap<>();

    /**
     * Instantiates a new Dungeon tileset and load in the characters and the items.
     *
     * @param game          the game
     * @param fileExtension the file extension of the tileset
     * @param assetScale    the asset scale of the tileset
     */
    public DungeonTileset(Game game, String fileExtension, double assetScale) {
        super(game, MapManager.TilesetType.DUNGEON, fileExtension, assetScale);

        loadCharacters();
        loadItems();
    }

    private void loadCharacters() {
        for (Player player : Player.values()) {
            int i = player.ordinal();
            playerImages.put(player, loadImages(32, 2 * i, 1, 2, 9));
        }

        for (Enemy enemy : Enemy.values()) {
            int i = enemy.ordinal();

            double tileX = switch(enemy) {
                case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, SKELETON, BROWN_SLUDGE, BEIGE_WALKER, SKULL_MASK, ORC_1,
                        ORC_2, SORCERER, TERROR_1, TERROR_2 -> 23;
                case GREEN_SLUDGE, CYAN_WALKER -> 27;
                case ANGRY_TOOTH_CHEST -> 19;
                case ZOMBIE, ORC, TERROR -> 42;
            };

            double tileY = switch(enemy) {
                case ZOMBIE -> 0;
                case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE -> 1 + i;
                case ORC -> 3;
                case SKELETON -> 4;
                case BROWN_SLUDGE, GREEN_SLUDGE, TERROR -> 6;
                case BEIGE_WALKER, CYAN_WALKER -> 8;
                case SKULL_MASK, ORC_1, ORC_2, SORCERER, TERROR_1, TERROR_2 -> 10 + 2 * (i - 8);
                case ANGRY_TOOTH_CHEST -> 23;
            };

            double tileWidth = switch(enemy) {
                case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, SKELETON, BROWN_SLUDGE, GREEN_SLUDGE, BEIGE_WALKER,
                        CYAN_WALKER, SKULL_MASK, ORC_1, ORC_2, SORCERER, TERROR_1, TERROR_2, ANGRY_TOOTH_CHEST -> 1;
                case ORC, ZOMBIE, TERROR -> 2;
            };

            double tileHeight = switch(enemy) {
                case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, ANGRY_TOOTH_CHEST -> 1;
                case SKELETON, BROWN_SLUDGE, GREEN_SLUDGE, BEIGE_WALKER, CYAN_WALKER, SKULL_MASK, ORC_1, ORC_2,
                        SORCERER, TERROR_1, TERROR_2 -> 2;
                case ZOMBIE, ORC, TERROR -> 3;
            };

            int tileAmount = switch(enemy) {
                case BEIGE_SQUARE, GREEN_SQUARE, RED_SQUARE, SKELETON, SKULL_MASK, ORC_1, ORC_2, TERROR_1,
                        TERROR_2, ZOMBIE, TERROR, ORC -> 8;
                case BROWN_SLUDGE, GREEN_SLUDGE, BEIGE_WALKER, CYAN_WALKER, SORCERER -> 4;
                case ANGRY_TOOTH_CHEST -> 3;
            };

            enemyImages.put(enemy, loadImages(tileX, tileY, tileWidth, tileHeight, tileAmount));
        }

        for(Door door : Door.values()){
            switch (door){
                case DOOR_1 -> doorImages.put(door, loadHugeDoorImages());
                case DOOR_2 -> doorImages.put(door, loadSmallDoorImages(9, 16));
                case DOOR_3 -> doorImages.put(door, loadSmallDoorImages(12, 16));
            }
        }
    }

    private void loadItems() {
        regularRewardImages.addAll(loadImages(20, 20, 1, 1, 1));
        bonusRewardImages.addAll(loadImages(18,20,0.5,0.5,4));
        punishmentImages.addAll(loadImages(22, 21, 1, 1, 1));
    }

    private List<BufferedImage> loadHugeDoorImages() {
        List<BufferedImage> images = new ArrayList<>();
        BufferedImage characterImage = tilesetImage.getSubimage(9 * assetTileSize,
                14 * assetTileSize, 2 * assetTileSize, (2 * assetTileSize));
        images.add(scaleImage(characterImage, assetScale));
        BufferedImage characterImage1 = tilesetImage.getSubimage(12 * assetTileSize,
                14 * assetTileSize, 2 * assetTileSize, (2 * assetTileSize));
        images.add(scaleImage(characterImage1, assetScale));

        return images;
    }

    /**
     * Gets player's images.
     *
     * @param character the character
     * @return the player's images
     */
    public List<BufferedImage> getPlayerImages(Player character) {
        return playerImages.get(character);
    }

    /**
     * Gets enemy's images.
     *
     * @param character the character
     * @return the enemy's images
     */
    public List<BufferedImage> getEnemyImages(Enemy character) {
        return enemyImages.get(character);
    }

    /**
     * Gets regular reward's images.
     *
     * @return the regular reward's images
     */
    public List<BufferedImage> getRegularRewardImages() {
        return regularRewardImages;

    }

    private List<BufferedImage> loadSmallDoorImages(double tileX, double tileY) {
        List<BufferedImage> images = new ArrayList<>();
        BufferedImage characterImage = tilesetImage.getSubimage((int) (tileX * assetTileSize),
                (int) (tileY * assetTileSize), assetTileSize,2 * assetTileSize);
        images.add(scaleImage(characterImage, assetScale));
        BufferedImage characterImage1 = tilesetImage.getSubimage((int) (tileX * assetTileSize),
                (int) ((tileY+2) * assetTileSize), assetTileSize,2 * assetTileSize);
        images.add(scaleImage(characterImage1, assetScale));
        return images;
    }

    /**
     * Gets bonus reward's images.
     *
     * @return the bonus reward's images
     */
    public List<BufferedImage> getBonusRewardImages() {
        return bonusRewardImages;
    }

    /**
     * Gets punishment's images.
     *
     * @return the punishment's images
     */
    public List<BufferedImage> getPunishmentImages() {
        return punishmentImages;
    }

    /**
     * Gets door's images.
     *
     * @param door the door
     * @return the door's images
     */
    public List<BufferedImage> getDoorImages(Door door) {
        return doorImages.get(door);
    }

    @Override
    public List<BufferedImage> getTileAnimationImages(int tileNumber) {
        List<BufferedImage> animationImages = new ArrayList<>();

        // The image numbers to get.
        List<Integer> animationImagesTileNumbers = new ArrayList<>();

        switch (tileNumber) {
            case 1284, 1342, 1226, 1400, 1458, 1516-> {
                // Torch fire and holder.
                for (int i = 0; i < 8; i++) {
                    animationImagesTileNumbers.add(tileNumber + i);
                }
            }
            case 294, 352, 410, 468 -> {
                for(int i = 0 ; i < 3; i++){
                    animationImagesTileNumbers.add(tileNumber + i);
                }
            }
            case 929 -> {
                for(int i = 0; i < 4; i++){
                    animationImagesTileNumbers.add(tileNumber + i);
                }
            }
            default -> animationImagesTileNumbers.add(tileNumber);
        }

        for (Integer animationImagesTileNumber : animationImagesTileNumbers) {
            animationImages.add(getTilesetImage(animationImagesTileNumber));
        }

        return animationImages;
    }

    @Override
    protected int getTrueTileNumber(int tileNumber) {
        return switch(tileNumber) {
            case 174 -> {
                List<Integer> tileNumbers = new ArrayList<>();
                tileNumbers.add(233);
                tileNumbers.add(234);
                tileNumbers.add(235);
                tileNumbers.add(241);
                tileNumbers.add(242);
                tileNumbers.add(291);
                tileNumbers.add(292);
                tileNumbers.add(293);
                tileNumbers.add(349);
                tileNumbers.add(350);
                tileNumbers.add(409);
                yield tileNumbers.get((int) (Math.random() * tileNumbers.size()));
            }
            case 232 -> {
                List<Integer> tileNumbers = new ArrayList<>();
                tileNumbers.add(233);
                tileNumbers.add(234);
                tileNumbers.add(235);
                tileNumbers.add(241);
                tileNumbers.add(242);
                tileNumbers.add(292);
                yield tileNumbers.get((int) (Math.random() * tileNumbers.size()));
            }
            default -> tileNumber;
        };
    }
}
