package audio;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all audio for the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class AudioManager {
    /**
     * The enum Audio types for the Background, Game, Coin, Bomb, Door open/close, Flip fail, and Flip.
     */
    public enum AudioType {
        BACKGROUND, GAME, COIN, BOMB, DOOR_OPEN, DOOR_CLOSE, FLIP_FAILED, FLIP
    }

    /**
     * Store the Audio files in map.
     */
    Map<AudioType, Audio> audioFiles;

    private boolean isMuted = false;
    private boolean isPlayingOnLoop = false;
    private boolean isPlayingOnce = false;

    /**
     * Instantiates a new Audio manager with hash map.
     */
    public AudioManager(){
        audioFiles = new HashMap<>();
    }

    /**
     * Mutes the audio.
     */
    public void mute() {
        isMuted = true;
        isPlayingOnLoop = false;
        isPlayingOnce = false;
    }

    /**
     * Unmutes the audio.
     */
    public void unMute() {
        isMuted = false;
    }

    /**
     * Add audio to the map.
     *
     * @param audioType the audio type
     * @param filename  the filename
     */
    public void addAudio(AudioType audioType, String filename){
        Audio Music = new Audio("audio/music/" + filename);
        audioFiles.put(audioType, Music);
    }

    /**
     * Gets audio from the map.
     *
     * @param audioType the audio type
     * @return the audio
     */
    public Audio getAudio(AudioType audioType) {
        return audioFiles.get(audioType);
    }

    /**
     * Play audio once.
     *
     * @param audioType the audio type
     */
    public void playAudioOnce(AudioType audioType){
        if (isMuted) {
            return;
        }

        Audio audio = getAudio(audioType);

        audio.playAudioOnce();
        isPlayingOnce = true;
    }

    /**
     * Play audio on loop.
     *
     * @param audioType the audio type
     */
    public void playAudioOnLoop(AudioType audioType){
        if (isMuted) {
            return;
        }

        Audio audio = getAudio(audioType);

        audio.playAudioOnLoop();
        isPlayingOnLoop = true;
    }

    /**
     * Stop audio.
     *
     * @param audioType the audio type
     */
    public void stopAudio(AudioType audioType){
        Audio audio = audioFiles.get(audioType);
        audio.stopAudio();
        isPlayingOnce = false;
        isPlayingOnLoop = false;
    }

    /**
     * Returns if audio is muted or not.
     *
     * @return if the audio manager is currently muted.
     */
    public boolean getIsMuted(){
        return isMuted;
    }
    /**
     * Returns if audio is played once or not.
     *
     * @return if the audio manager is played once.
     */
    public boolean getIsPlayingOnce(){
        return isPlayingOnce;
    }
    /**
     * Returns if audio is playing on loop or not.
     *
     * @return if the audio manager is playing on loop.
     */
    public boolean getIsPlayingOnLoop(){
        return isPlayingOnLoop;
    }
}
