package audio;

import util.resources.ResourcesReader;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * The type Audio.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class Audio {
    /**
     * The clip that store the audio recourses.
     */
    Clip clip = null;
    /**
     * The Audio system for this audio.
     */
    AudioInputStream audioSystem;
    /**
     * The file that contains the audio.
     */
    String musicPath;
    /**
     * The Resources reader to read in the audio stream.
     */
    ResourcesReader resourcesReader = new ResourcesReader();

    /**
     * Instantiates a new Audio.
     *
     * @param filename the filename of the new audio
     */
    public Audio(String filename){
        musicPath = filename;
        load();
    }

    private void load() {
        audioSystem = resourcesReader.readAudioSteam(musicPath);
        try {
            clip = AudioSystem.getClip();
            clip.open(audioSystem);
            clip.setFramePosition(0);
            FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            floatControl.setValue(-20.0f);
        } catch (LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Play the audio once.
     */
    public void playAudioOnce(){
        load();
        clip.start();
    }

    /**
     * Play the audio on loop.
     */
    public void playAudioOnLoop(){
        load();
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stop the audio.
     */
    public void stopAudio(){
        clip.stop();
    }
}
