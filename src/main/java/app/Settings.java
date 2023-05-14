package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import audio.AudioManager;
import util.resources.ResourcesReader;

/**
 * The settings screen of the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @version 1.2
 */
public class Settings extends JPanel implements ActionListener {
    /**
     * The Menu text colour.
     */
    Color menuTextColour = new java.awt.Color(245, 167, 27, 253);

    /**
     * Title screen text font.
     */
    Font ThreeDventure;

    /**
     * Game title and audio labels.
     */
    JLabel gameTitle, audio;

    /**
     * Back, audioSwitch, and help buttons.
     */
    JButton back, audioSwitch, helpButton;

    /**
     * The Audio manager.
     */
    AudioManager audioManager;

    /**
     * Instantiates a new Settings and calling function to set it up. And getting the font style of the characters.
     *
     * @param width        the width of the setting screen
     * @param height       the height of the setting screen
     * @param audioManager the audio manager
     */
    public Settings(int width, int height, AudioManager audioManager, Font font) {
        this.audioManager = audioManager;
        ThreeDventure = font;

        this.setPreferredSize(new Dimension(width, height));

        // Running settings methods.
        this.settingsTitle();
        this.returnToTitle();
        this.settingsMenu();
        this.addBackground();
    }

    /**
     * Method to background image to the settings screen.
     */
    public void addBackground() {
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ResourcesReader().readImage("assets/backgrounds/SettingsScreen.png")));
        Dimension size = background.getPreferredSize(); // Gets the size of the image
        background.setBounds(0, 0, size.width, size.height); // Sets the location of the image

        this.add(background);
    }

    /**
     * Method to display game title in the middle of the screen.
     */
    public void settingsTitle() {
        this.setBackground(new java.awt.Color(75, 75, 77));
        // Create title and set size, font, and colour.
        gameTitle = new JLabel("Settings + Info");
        gameTitle.setFont(ThreeDventure.deriveFont(70f));
        gameTitle.setForeground(menuTextColour);
        Dimension size = gameTitle.getPreferredSize();

        // Setting the title to the middle of the screen.
        int titleWidth = size.width;
        int centerTitle = ((getPreferredSize().width-titleWidth)/2);
        gameTitle.setBounds(centerTitle,95,size.width,size.height);

        this.setLayout(null);
        this.add(gameTitle);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Method to return to the title screen.
     */
    public void returnToTitle() {
        // Back button.
        back = new JButton("Back");
        back.setFont(ThreeDventure.deriveFont(33f));
        back.setForeground(menuTextColour);
        back.setBorderPainted(false);
        back.setFocusable(false);
        back.setContentAreaFilled(false);
        Dimension size = back.getPreferredSize();
        back.setBounds(25,45,size.width,size.height);

        this.setLayout(null);
        this.add(back);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Sets up the setting menu including options to mute audio and  access to "How to Play" menu.
     */
    public void settingsMenu() {
        // Audio label.
        audio = new JLabel("Audio");
        audio.setFont(ThreeDventure.deriveFont(50f));
        audio.setForeground(menuTextColour);
        Dimension size = audio.getPreferredSize();
        audio.setBounds(240,300,size.width+50,size.height+50);

        // Audio button.
        audioSwitch = new JButton("ON");
        audioSwitch.setFont(ThreeDventure.deriveFont(50f));
        audioSwitch.setForeground(menuTextColour);
        audioSwitch.setBorderPainted(false);
        audioSwitch.setFocusable(false);
        audioSwitch.setContentAreaFilled(false);
        Dimension size2 = audioSwitch.getPreferredSize();
        audioSwitch.setBounds(720-size2.width,300,size2.width+50,size2.height+50);
        // Adding toggle feature to volumeSwitch button.
        audioSwitch.addActionListener(this);

        // Help button.
        helpButton = new JButton("How To Play");
        helpButton.setFont(ThreeDventure.deriveFont(50f));
        helpButton.setForeground(menuTextColour);
        helpButton.setBorderPainted(false);
        helpButton.setFocusable(false);
        helpButton.setContentAreaFilled(false);
        Dimension size3 = helpButton.getPreferredSize();
        int centerHowToPlay = 277;
        helpButton.setBounds(centerHowToPlay,420,size3.width+50,size3.height+50);

        this.setLayout(null);
        this.add(audio);
        this.add(audioSwitch);
        this.add(helpButton);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Audio switch.
        if(e.getSource()== audioSwitch) {
            if (Objects.equals(audioSwitch.getText(), "ON")) {
                audioSwitch.setText("OFF");
                audioManager.stopAudio(AudioManager.AudioType.BACKGROUND);
                audioManager.mute();
            } else {
                audioSwitch.setText("ON");
                audioManager.unMute();
                audioManager.playAudioOnLoop(AudioManager.AudioType.BACKGROUND);
            }
        }
    }
}
