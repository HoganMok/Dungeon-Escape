package app;

import audio.AudioManager;
import util.media.TextGenerator;
import util.resources.ResourcesReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The game over screen that displays after the game ends.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @author Ariel Lin
 * @version 1.2
 */
public class GameOverScreen extends JPanel implements ActionListener {
    /**
     * Sets Menu options text colors.
     */
    Color menuTextColour = new Color(245, 167, 27, 253);

    /**
     * ThreeDventure Font.
     */
    Font ThreeDventure;

    /**
     * Labels for the game score and duration
     */
    private JLabel gameScoreLabel, gameDurationLabel;

    /**
     * The Back Button.
     */

    JButton back;
    /**
     * The Audio manager.
     */
    AudioManager audioManager;

    private final App app;

    /**
     * Instantiates a new Game Over screen once the game is ended.
     *
     * @param width  the width of the Game Over screen
     * @param height the height of the Game Over screen
     * @param app    the game app
     */
    public GameOverScreen(int width, int height, App app) {
        this.audioManager = app.getAudioManager();
        ThreeDventure = app.getFont();
        this.app = app;

        this.setPreferredSize(new Dimension(width, height));

        this.setLayout(null);

        // Running settings methods.
        addTitle();
        addReturnToTitleButton();
        addMenu();
        addBackground();
    }

    /**
     * Add background to the Game Over screen.
     * Displays game over background
     */
    public void addBackground() {
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ResourcesReader().readImage("assets/backgrounds/GameOver.png")));
        Dimension size = background.getPreferredSize(); //Gets the size of the image
        background.setBounds(0, 0, size.width, size.height); //Sets the location of the image

        this.add(background);
    }

    /**
     * Add title to the Game Over screen.
     * Displays the settings title
     */
    public void addTitle() {
        this.setBackground(new Color(75, 75, 77));
        // Create title and set size, font, and colour.
        JLabel gameTitle = new JLabel("Game Over");
        gameTitle.setFont(ThreeDventure.deriveFont(70f));
        gameTitle.setForeground(menuTextColour);
        Dimension size = gameTitle.getPreferredSize();

        // Make sure that the title is in the middle of the screen.
        int titleWidth = size.width;
        int centerTitle = ((getPreferredSize().width-titleWidth)/2);
        gameTitle.setBounds(centerTitle,95,size.width,size.height);

        this.add(gameTitle);
    }

    /**
     * Add a button go return back to the Title screen.
     * Method to go back to title screen from the game over screen.
     */
    public void addReturnToTitleButton() {
        // Back button.
        back = new JButton("Back To Main Menu");
        back.setFont(ThreeDventure.deriveFont(33f));
        back.setForeground(menuTextColour);
        back.setBorderPainted(false);
        back.setFocusable(false);
        back.setContentAreaFilled(false);
        back.addActionListener(this);
        Dimension size = back.getPreferredSize();
        back.setBounds((getPreferredSize().width - size.width) / 2, 500, size.width ,size.height);

        this.add(back);
    }

    /**
     * Add a menu to display the score and duration of the game.
     */
    public void addMenu() {
        // Game score.
        gameScoreLabel = new JLabel("Score");
        gameScoreLabel.setFont(ThreeDventure.deriveFont(40f));
        gameScoreLabel.setForeground(menuTextColour);

        // Game duration
        gameDurationLabel = new JLabel("Time");
        gameDurationLabel.setFont(ThreeDventure.deriveFont(40f));
        gameDurationLabel.setForeground(menuTextColour);

        this.add(gameScoreLabel);
        this.add(gameDurationLabel);
    }
    
    /**
     * Takes player back to Main Menu when back button is pressed
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==back) {
            app.returnToMainMenu();
        }
    }

    /**
     * Sets up player data after game over
     *
     * @param gameScore           the game score
     * @param gameDurationMinutes the game duration minutes
     * @param gameDurationSeconds the game duration seconds
     */
    public void setGameOverData(int gameScore, int gameDurationMinutes, double gameDurationSeconds) {
        gameScoreLabel.setText("Score: " + gameScore);
        Dimension gameScoreLabelSize = gameScoreLabel.getPreferredSize();
        gameScoreLabel.setBounds(
                (getPreferredSize().width - gameScoreLabelSize.width) / 2,
                (int) ((getPreferredSize().height - gameScoreLabelSize.height) * (1.0 / 3.0)),
                gameScoreLabelSize.width + 50, gameScoreLabelSize.height + 50);

        gameDurationLabel.setText(TextGenerator.getDurationText(gameDurationMinutes, gameDurationSeconds));
        Dimension gameDurationLabelSize = gameDurationLabel.getPreferredSize();
        gameDurationLabel.setBounds(
                (getPreferredSize().width - gameDurationLabelSize.width) / 2,
                (int) ((getPreferredSize().height - gameDurationLabelSize.height) * (1.0 / 2.0)),
                gameDurationLabelSize.width + 50, gameDurationLabelSize.height + 50);
    }
}
