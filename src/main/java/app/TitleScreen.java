package app;

import util.resources.ConfigReader;
import util.resources.ResourcesReader;
import javax.swing.*;
import java.awt.*;

/**
 * The title screen of the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @version 1.2
 */
public class TitleScreen extends JPanel {
    /**
     * The Menu text colour.
     */
    Color menuTextColour = new java.awt.Color(245, 167, 27, 253);

    /**
     * Title screen text font.
     */
    Font ThreeDventure;

    /**
     * Play, avatar, and settings buttons.
     */
    JButton play, avatar, settings;

    /**
     * Instantiates a new Title screen and calls methods to set it up.
     *
     * @param width  the width of the title screen
     * @param height the height of the title screen
     */
    public TitleScreen(int width, int height, Font font) {
        ThreeDventure = font;
        this.setBackground(new java.awt.Color(115, 87, 75));
        this.setPreferredSize(new Dimension(width, height));

        // Displaying the game title and menu.
        this.displayTitle();
        this.titleMenu();
        this.addBackground();

    }

    /**
     * Method to add background image to the title screen.
     */
    public void addBackground() {
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ResourcesReader().readImage("assets/backgrounds/TitleScreen.png")));
        Dimension size = background.getPreferredSize(); // Gets the size of the image
        background.setBounds(0, 0, size.width, size.height); // Sets the location of the image

        this.add(background);
    }

    /**
     * Method to display game title in the middle of the screen.
     */
    public void displayTitle() {
        // Create title and set size, font, and colour.
        JLabel gameTitle = new JLabel(ConfigReader.getGameTitle());
        gameTitle.setFont(ThreeDventure.deriveFont(77f));
        gameTitle.setForeground(menuTextColour);
        Dimension size = gameTitle.getPreferredSize();

        int titleWidth = size.width;
        int centerTitle = ((getPreferredSize().width-titleWidth)/2);
        gameTitle.setBounds(centerTitle,91,size.width+10,size.height+10);

        this.setLayout(null);
        this.add(gameTitle);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Method to display game menu and set up different options.
     */
    public void titleMenu() {
        // Play button.
        play = new JButton("Play");
        play.setFont(ThreeDventure.deriveFont(50f));
        play.setForeground(menuTextColour);
        // Next two lines make border around the button invisible.
        play.setBorderPainted(false);
        play.setFocusable(false);
        play.setContentAreaFilled(false);
        Dimension size = play.getPreferredSize();
        int positionPlay = getPreferredSize().width/4;

        // Avatar option.
        avatar = new JButton("Avatar");
        avatar.setFont(ThreeDventure.deriveFont(50f));
        avatar.setForeground(menuTextColour);
        avatar.setBorderPainted(false);
        avatar.setFocusable(false);
        avatar.setContentAreaFilled(false);
        Dimension size2 = avatar.getPreferredSize();
        int avatarWidth = size2.width;
        int positionAvatar = getPreferredSize().width-positionPlay-avatarWidth;

        // Settings option.
        settings = new JButton("Settings + Info");
        settings.setFont(ThreeDventure.deriveFont(49f));
        settings.setForeground(menuTextColour);
        settings.setBorderPainted(false);
        settings.setFocusable(false);
        settings.setContentAreaFilled(false);
        Dimension size3 = settings.getPreferredSize();
        int settingsWidth = size3.width;
        int positionSettings = ((getPreferredSize().width-settingsWidth)/2);

        // Positioning buttons.
        play.setBounds(positionPlay-2,320,size.width+15,size.height+15);
        avatar.setBounds(positionAvatar,320,size2.width+15,size2.height+15);
        settings.setBounds(positionSettings-7,465,size3.width+20,size3.height);

        this.setLayout(null);
        this.add(play);
        this.add(avatar);
        this.add(settings);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
