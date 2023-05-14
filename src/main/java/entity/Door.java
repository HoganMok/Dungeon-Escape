package entity;

import app.game.Game;
import audio.AudioManager;
import sprite.data.SpriteData;

/**
 * The exit door of a level.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class Door extends Entity<Door.DoorAnimation> {
    /**
     * The enum Door animation.
     */
    public enum DoorAnimation {
        /**
         * Open door animation.
         */
        OPEN,
        /**
         * Close door animation.
         */
        CLOSE
    }

    /**
     * The Is opened.
     */
    boolean isOpened = false;

    /**
     * Instantiates a new Door and set it up in Entity.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Door(Game game, SpriteData<DoorAnimation> spriteData) {
        super(game, spriteData);

        game.addDoor(spriteId);
    }

    @Override
    public void destroy() {
        game.destroyDoor(spriteId);

        super.destroy();
    }

    /**
     * Opens the door with setting current animation of the time to be open, and plays the DOOR_OPEN soundtrack once
     */
    public void open() {
        isOpened = true;
        game.getAudioManager().playAudioOnce(AudioManager.AudioType.DOOR_OPEN);

        setCurrentAnimation(DoorAnimation.OPEN);
    }


    public void update(double deltaTime) {
    }

    /**
     * Get isOpened
     * @return isOpened
     */
    public boolean getIsOpened(){
        return isOpened;
    }
}
