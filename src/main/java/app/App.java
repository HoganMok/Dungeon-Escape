package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import app.game.Game;
import audio.AudioManager;
import util.resources.ConfigReader;
import util.resources.ResourcesReader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.Objects;

/**
 * The app class that stores everything for the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @version 1.3
 */
public class App implements ActionListener, PropertyChangeListener, EventListener {
    private final JFrame mainWindow;

    private final AudioManager audioManager = new AudioManager();

    /**
     * Instances of the Game, TitleScreen, Settings, Avatar,
     * HowToPlay, and GameOverScreen classes
     */
    Game game;
    TitleScreen titleScreen;
    Settings settings;
    ChooseAvatar avatar;
    HowToPlay helpPage;
    GameOverScreen gameOverScreen;

    /**
     * Title screen text font.
     */
    Font ThreeDventure;

    /**
     * Instantiates a new App and set it up.
     */
    public App() {
        loadAudio();
        loadFont();

        game = new Game(audioManager, ThreeDventure, this);

        // Creating a JFrame window for the game to be displayed on
        mainWindow = new JFrame();
        mainWindow.setSize(game.getScreenPixelWidth(), game.getScreenPixelHeight());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setTitle(ConfigReader.getGameTitle());

        // Make the window start at the middle of the screen of the user.
        mainWindow.setLocationRelativeTo(null);

        // Get the icon image.
        URL imageURL = this.getClass().getClassLoader().getResource("assets/icon/icon.png");
        Image image;
        try {
            assert imageURL != null;
            image = ImageIO.read(imageURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Set the app icon.
        Taskbar taskbar = Taskbar.getTaskbar();
        try {
            taskbar.setIconImage(image);
        } catch (UnsupportedOperationException ignored) {
        }
        mainWindow.setIconImage(image);

        // Add the title screen, game, settings, and avatar JPanels.
        titlePanel();
        settingsPanel();
        helpPanel();
        avatarPanel();
        createGameOverScreen();
        gamePanel();

        mainWindow.setVisible(true);

        audioManager.playAudioOnLoop(AudioManager.AudioType.BACKGROUND);
    }

    public void loadFont() {
        try {
            ThreeDventure = Font.createFont(Font.TRUETYPE_FONT, new ResourcesReader().readResourceInputStream(
                    "fonts/3Dventure.ttf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to create the title panel.
     */
    public void titlePanel() {
        titleScreen = new TitleScreen(mainWindow.getWidth(), mainWindow.getHeight(), ThreeDventure);
        titleScreen.setVisible(true);
        mainWindow.add(titleScreen);
        mainWindow.pack();
        titleScreen.play.addActionListener(this);
        titleScreen.settings.addActionListener(this);
        titleScreen.avatar.addActionListener(this);
    }

    /**
     * Method to create the settings panel.
     */
    public void settingsPanel() {
        settings = new Settings(mainWindow.getWidth(), mainWindow.getHeight(), audioManager, ThreeDventure);
        mainWindow.add(settings);
        mainWindow.pack();
        settings.back.addActionListener(this);
        settings.helpButton.addActionListener(this);
        settings.setVisible(false);
    }

    /**
     * Method to create the avatar panel.
     */
    public void avatarPanel() {
        avatar = new ChooseAvatar(mainWindow.getWidth(), mainWindow.getHeight(), game, ThreeDventure);
        mainWindow.add(avatar);
        mainWindow.pack();
        avatar.back.addActionListener(this);
        avatar.setVisible(false);
    }

    /**
     * Method to create the game over screen.
     */
    public void createGameOverScreen() {
        gameOverScreen = new GameOverScreen(mainWindow.getWidth(), mainWindow.getHeight(), this);
        mainWindow.add(gameOverScreen);
        mainWindow.pack();
        gameOverScreen.setVisible(false);
    }

    /**
     * Method to create the game panel for the actual game.
     */
    public void gamePanel() {
        mainWindow.add(game);
        mainWindow.pack();
        game.setVisible(false);
    }

    /**
     * Method to create the help panel.
     */
    public void helpPanel() {
        helpPage = new HowToPlay(mainWindow.getWidth(),mainWindow.getHeight(), ThreeDventure);
        mainWindow.add(helpPage);
        mainWindow.pack();
        helpPage.back.addActionListener(this);
        helpPage.setVisible(false);
    }

    /**
     * Starts the game when the play button is clicked.
     */
    public void playButtonClicked() {
        titleScreen.setVisible(false);
        game.setVisible(true);
        game.requestFocus();
        startGame();
    }

    /**
     * ActionListener for buttons to go between pages
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==titleScreen.play){
            playButtonClicked();
        }
        if(e.getSource()==titleScreen.avatar){
            avatar.setVisible(true);
            titleScreen.setVisible(false);
        }
        if(e.getSource()==titleScreen.settings){
            settings.setVisible(true);
            titleScreen.setVisible(false);
        }
        if(e.getSource()==settings.back){
            titleScreen.setVisible(true);
            settings.setVisible(false);
        }
        if(e.getSource()==avatar.back) {
            titleScreen.setVisible(true);
            avatar.setVisible(false);
        }
        if(e.getSource()==settings.helpButton) {
            helpPage.setVisible(true);
            settings.setVisible(false);
        }
        if(e.getSource()==helpPage.back) {
            settings.setVisible(true);
            helpPage.setVisible(false);
        }
    }

    /**
     * Start game, and switch the current audio playing.
     */
    public void startGame() {
        audioManager.stopAudio(AudioManager.AudioType.BACKGROUND);
        audioManager.playAudioOnLoop(AudioManager.AudioType.GAME);
        this.game.startGameLoop();
    }

    /**
     * Gets the audio manager for the app.
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }
    /**
     * Gets the font for the game.
     *
     * @return the font for the game
     */
    public Font getFont() {
        return ThreeDventure;
    }

    private void loadAudio() {
        audioManager.addAudio(AudioManager.AudioType.BACKGROUND, "ChiptuneAdventure.wav");
        audioManager.addAudio(AudioManager.AudioType.GAME, "dj soul - Summer Adventure.wav");
        audioManager.addAudio(AudioManager.AudioType.COIN, "coin.wav");
        audioManager.addAudio(AudioManager.AudioType.BOMB, "BOMB.wav");
        audioManager.addAudio(AudioManager.AudioType.DOOR_OPEN, "DOOR_OPEN.wav");
        audioManager.addAudio(AudioManager.AudioType.DOOR_CLOSE, "DOOR_CLOSE.wav");
        audioManager.addAudio(AudioManager.AudioType.FLIP_FAILED, "error.wav");
        audioManager.addAudio(AudioManager.AudioType.FLIP, "laser4.wav");
    }

    /**
     * Runs everything needed to happen when the game ends.
     * Switch the audio playing, switch active windows, and set the game over data on the game over screen.
     * @param gameScore           the game score
     * @param gameDurationMinutes the game duration minutes
     * @param gameDurationSeconds the game duration seconds
     */
    public void gameOver(int gameScore, int gameDurationMinutes, double gameDurationSeconds) {
        audioManager.stopAudio(AudioManager.AudioType.GAME);
        audioManager.playAudioOnLoop(AudioManager.AudioType.BACKGROUND);
        game.setVisible(false);
        gameOverScreen.setVisible(true);
        gameOverScreen.setGameOverData(gameScore, gameDurationMinutes, gameDurationSeconds);
    }

    /**
     * Return to the main menu.
     */
    public void returnToMainMenu() {
        gameOverScreen.setVisible(false);
        titleScreen.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (Objects.equals(e.getPropertyName(), "gameOver") && (boolean) e.getNewValue()) {
            gameOver(game.getGameScore(), game.getGameDurationMinutes(), game.getGameDurationSeconds());
        }
    }

    /**
     * Returns the game.
     *
     * @return the game of the app
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns if the game is currently active.
     *
     * @return if the game of the app is running or not
     */
    public boolean isGameRunning() {
        return game.getIsRunning();
    }
}
