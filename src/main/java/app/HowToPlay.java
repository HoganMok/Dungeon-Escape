package app;

import util.resources.ResourcesReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * The how to play screen of the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @author Ariel Lin
 * @version 1.2
 */
public class HowToPlay extends JPanel {
    /**
     * The menu text and Text area background colors.
     */
    Color menuTextColour = new java.awt.Color(245, 167, 27, 253);
    Color helpTextColour = new java.awt.Color(35, 35, 36, 255);

    /**
     * ThreeDventure and KenneyPixelSquare fonts.
     */
    Font ThreeDventure, KenneyPixelSquare;

    /**
     * The How To Play screen title label.
     */
    JLabel helpTitle;

    /**
     * The Back Button.
     */
    JButton back;

    /**
     * Instantiates a How to play menu to explain the game concept.
     *
     * @param width  the width
     * @param height the height
     * @param font
     */
    public HowToPlay(int width, int height, Font font) {
        ThreeDventure = font;
        this.setPreferredSize(new Dimension(width, height));

        // Sub text font
        try {
            KenneyPixelSquare = Font.createFont(Font.TRUETYPE_FONT,new ResourcesReader().readResourceInputStream(
                    "fonts/KenneyMini.ttf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        // Methods for How To Play page go here.
        this.howToPlayTitle();
        this.gameInstructions();
        this.returnToTitle();
        this.addBackground();
    }

    /**
     * Add background Image to the "How to Play" menu.
     */
    public void addBackground() {
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ResourcesReader().readImage("assets/backgrounds/SettingsScreen.png")));
        Dimension size = background.getPreferredSize(); //Gets the size of the image
        background.setBounds(0, 0, size.width, size.height); //Sets the location of the image

        this.add(background);
    }

    /**
     * How to play title of the "How to Play" screen.
     */
    public void howToPlayTitle() {
        // Create title and set size, font, and colour.
        this.setBackground(new java.awt.Color(75, 75, 77));
        helpTitle = new JLabel("How To Play");
        helpTitle.setFont(ThreeDventure.deriveFont(70f));
        helpTitle.setForeground(menuTextColour);
        Dimension size = helpTitle.getPreferredSize();

        // Make sure that the title is in the middle of the screen.
        int titleWidth = size.width;
        int centerTitle = ((getPreferredSize().width-titleWidth)/2);
        helpTitle.setBounds(centerTitle,95,size.width,size.height);

        this.setLayout(null);
        this.add(helpTitle);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * The instructions to explain the game.
     */
    // Display game instructions
    JTextArea helpText = new JTextArea("To move your character, use the arrows or WASD keys." +
            "\nTo move to the next level, you must collect all keys." +
            "\nYou can 'flip' the map on each level with the 'F' key" +
            "\nor the space bar to get away from enemies or through" +
            "\nwalls if there is no other escape route. Activate debug" +
            "\nmode to see hitboxes with the 'B' key.");

    /**
     * Setting of the game instructions menu.
     */
    public void gameInstructions() {
        // Create title and set size, font, and colour.
        this.setBackground(new java.awt.Color(75, 75, 77));
        helpText.setFont(KenneyPixelSquare.deriveFont(30f));
        helpText.setForeground(menuTextColour);
        helpText.setBackground(helpTextColour);

        Dimension size = helpText.getPreferredSize();

        helpText.setBounds(53,250,size.width,size.height);

        this.setLayout(null);
        this.add(helpText);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Return to the title screen from settings screen.
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
}
