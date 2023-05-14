package entity.movable.item;

import app.game.Game;
import audio.AudioManager;
import sprite.data.SpriteData;

/**
 * The regular reward item.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class RegularReward extends Item<RegularReward.RegularRewardAnimation> {
    /**
     * The enum Regular reward animation.
     */
    public enum RegularRewardAnimation {
        /**
         * Bob regular reward animation.
         */
        BOB
    }

    /**
     * Instantiates a new Regular reward and add it to the entity map under Game.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public RegularReward(Game game, SpriteData<RegularRewardAnimation> spriteData) {
        super(game, spriteData);

        game.addRegularReward(spriteId);
    }
    @Override
    public void destroy() {
        game.destroyRegularReward(spriteId);
        super.destroy();
    }

    @Override
    protected void pickedUp() {
        game.setGameScore(100);
        soundEffect();
    }

    private void soundEffect(){
        game.getAudioManager().playAudioOnce(AudioManager.AudioType.COIN);
    }
}
