package entity.movable.item;

import app.game.Game;
import audio.AudioManager;

import sprite.data.SpriteData;

/**
 * The bonus reward item.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class BonusReward extends Item<BonusReward.BonusRewardAnimation> {
    private final double waitTotalDuration = 3 + Math.random() * 15;
    private final double activeTotalDuration = 9 + Math.random() * 4;
    private double currentDuration = 0;

    /**
     * The enum Bonus reward animation.
     */
    public enum BonusRewardAnimation {
        /**
         * Bob bonus reward animation.
         */
        BOB
    }

    /**
     * Instantiates a new Bonus reward and set it is not active.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public BonusReward(Game game, SpriteData<BonusRewardAnimation> spriteData) {
        super(game, spriteData);
        isActive = false;
    }

    @Override
    public void update(double deltaTime) {
        currentDuration += deltaTime;
        if (isActive && currentDuration >= activeTotalDuration) {
            currentDuration = 0;
            isActive = false;
        } else if (!isActive && currentDuration >= waitTotalDuration) {
            currentDuration = 0;
            isActive = true;
        }

        super.update(deltaTime);
    }

    @Override
    protected void pickedUp() {
        game.setGameScore(500);
        soundEffect();
    }

    private void soundEffect(){
        game.getAudioManager().playAudioOnce(AudioManager.AudioType.COIN);
    }
}
