package util.resources;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.sound.sampled.*;

import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;

/**
 * Used to read in various resource files.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.1
 */
public class ResourcesReader {
    public InputStream readResourceInputStream(String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }

    /**
     * Read a JSON file.
     *
     * @param filename the filename
     * @return the JSON object
     */
    public JSONObject readJSON(String filename)  {
        return new JSONObject(new JSONTokener(new BufferedReader(new InputStreamReader(readResourceInputStream(filename)))));
    }

    /**
     * Read an image.
     *
     * @param filename the filename of the image
     * @return the buffered image
     */
    public BufferedImage readImage(String filename) {
        try {
            return ImageIO.read(readResourceInputStream(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read an audio file.
     * <p>
     * All audio files of the game have the wav extension.
     *
     * @param filename the filename of the audio
     * @return the audio input stream
     */
    public AudioInputStream readAudioSteam(String filename){
        // Read in the audio if it exists.
        try {
            return AudioSystem.getAudioInputStream(new BufferedInputStream(readResourceInputStream(filename)));
        } catch (Exception e){ e.printStackTrace();}
        return null;
    }

}