package util.media;

import java.time.Duration;

/**
 * Used for generating duration text for the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class TextGenerator {
    /**
     * Based on different cases and generates the right string to display the gameDuration in the game
     *
     * @param minutes the current minutes of the game
     * @param seconds the current seconds of the game
     * @return the display text of the current game duration.
     */
    public static String getDurationText(int minutes, double seconds) {
        String DurationText = "Time: ";
        if (minutes >= 10){
            DurationText += minutes;
        } else{
            DurationText = DurationText + "0" + minutes;
        }
        DurationText += " : ";
        if (seconds >= 10){
            DurationText += (int)seconds;
        } else{
            DurationText = DurationText + "0" + (int)seconds;
        }
        return DurationText;
    }
}
