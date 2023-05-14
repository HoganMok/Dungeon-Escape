package entity.movable.item;

import app.game.Game;
import audio.AudioManager;

import sprite.data.SpriteData;

/**
 * A game punishment, which is a bomb.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.2
 */
public class Punishment extends Item<Punishment.PunishmentAnimation> {
    /**
     * The enum Punishment animation.
     */
    public enum PunishmentAnimation {
        /**
         * Bob punishment animation.
         */
        BOB
    }

    /**
     * Instantiates a new Punishment and set it up in sprite.
     *
     * @param game       the game
     * @param spriteData the data to create this sprite
     */
    public Punishment(Game game, SpriteData<PunishmentAnimation> spriteData) {
        super(game, spriteData);
    }

    @Override
    protected void pickedUp() {
        game.setGameScore(-300);
        soundEffect();
    }

    private void soundEffect(){
        game.getAudioManager().playAudioOnce(AudioManager.AudioType.BOMB);
    }
}
