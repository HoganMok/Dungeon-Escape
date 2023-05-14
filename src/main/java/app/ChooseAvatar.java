package app;

import app.game.Game;
import tileset.DungeonTileset;
import util.resources.ResourcesReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The screen to choose a player avatar for the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @author Taaibah Malik
 * @author Ariel Lin
 * @version 1.2
 */
public class ChooseAvatar extends JPanel implements ActionListener {
    /**
     * The Menu Text color
     */
    Color menuTextColour = new java.awt.Color(245, 167, 27, 253);
    /**
     * The Selected avatar.
     */
    Color selectedAvatar = new java.awt.Color(241, 215, 177, 253);
    /**
     * ThreeDventure font.
     */
    Font ThreeDventure;
    /**
     * Label for choosing an avatar.
     */
    JLabel chooseAvatar;
    /**
     * Buttons for going back and switching between the 3 avatar choices.
     */
    JButton back, avatar1, avatar2, avatar3;

    /**
     * Default player type (Knight)
     */
    private DungeonTileset.Player playerType = DungeonTileset.Player.KNIGHT_ORANGE;

    private final Game game;

    /**
     * Instantiates a new Choose avatar screen.
     *
     * @param width  the width of "Choose Avatar" screen
     * @param height the height of "Choose Avatar" screen
     * @param game   the game
     */
    public ChooseAvatar(int width, int height, Game game, Font font) {
        this.game = game;
        ThreeDventure = font;

        this.setPreferredSize(new Dimension(width, height));

        // Methods for Avatar
        this.avatarTitle();
        this.returnToTitle();
        this.changeAvatar();
        this.addBackground();
    }

    /**
     * Add background to the Choose Avatar screen.
     */
    public void addBackground() {
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ResourcesReader().readImage("assets/backgrounds/AvatarScreen.png")));
        Dimension size = background.getPreferredSize(); // Gets the size of the image
        background.setBounds(0, 0, size.width, size.height); // Sets the location of the image

        this.add(background);
    }

    /**
     * Display the string of Choose an Avatar screen.
     */
    public void avatarTitle() {
        this.setBackground(new java.awt.Color(52, 69, 59));
        // Create title and set size, font, and colour.
        chooseAvatar = new JLabel("Choose an Avatar");
        chooseAvatar.setFont(ThreeDventure.deriveFont(70f));
        chooseAvatar.setForeground(menuTextColour);
        Dimension size = chooseAvatar.getPreferredSize();

        // Make sure that the title is in the middle of the screen.
        int titleWidth = size.width;
        int centerTitle = ((getPreferredSize().width-titleWidth)/2);
        chooseAvatar.setBounds(centerTitle,102,size.width,size.height);

        this.setLayout(null);
        this.add(chooseAvatar);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Takes the player back to Main Menu screen
     */
    private void returnToTitle() {
        // Back button.
        back = new JButton("Back");
        back.setFont(ThreeDventure.deriveFont(33f));
        back.setForeground(menuTextColour);
        back.setBorderPainted(false);
        back.setFocusable(false);
        back.setContentAreaFilled(false);
        back.addActionListener(this);
        Dimension size = back.getPreferredSize();
        back.setBounds(25,45,size.width,size.height);

        this.setLayout(null);
        this.add(back);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /**
     * Giving three options (Knight, Wizard, and Dino) for player to choose as his/her avatar.
     */
    public void changeAvatar() {
        // avatar1 button (KNIGHT_ORANGE)
        avatar1 = new JButton("Knight");
        avatar1.setFont(ThreeDventure.deriveFont(44f));
        avatar1.setForeground(menuTextColour);
        avatar1.setBorderPainted(false);
        avatar1.setFocusable(false);
        avatar1.setContentAreaFilled(false);
        Dimension size = avatar1.getPreferredSize();
        avatar1.setBounds(138,428,size.width+30,size.height);
        // Adding toggle feature to button
        avatar1.addActionListener(this);

        // avatar2 button (WIZARD_2)
        avatar2 = new JButton("Wizard");
        avatar2.setFont(ThreeDventure.deriveFont(44f));
        avatar2.setForeground(menuTextColour);
        avatar2.setBorderPainted(false);
        avatar2.setFocusable(false);
        avatar2.setContentAreaFilled(false);
        Dimension size2 = avatar2.getPreferredSize();
        avatar2.setBounds(364,428,size2.width+30,size2.height);
        // Adding toggle feature to button
        avatar2.addActionListener(this);

        // avatar3 button (DINO_1)
        avatar3 = new JButton("Dino");
        avatar3.setFont(ThreeDventure.deriveFont(44f));
        avatar3.setForeground(menuTextColour);
        avatar3.setBorderPainted(false);
        avatar3.setFocusable(false);
        avatar3.setContentAreaFilled(false);
        Dimension size3 = avatar3.getPreferredSize();
        avatar3.setBounds(610,428,size3.width+30,size3.height);
        // Adding toggle feature to button
        avatar3.addActionListener(this);

        this.add(avatar1);
        this.add(avatar2);
        this.add(avatar3);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Switching text colour to highlight whatever choice is selected
        if (e.getSource()==avatar1) {
            playerType = DungeonTileset.Player.KNIGHT_ORANGE;
            avatar1.setForeground(selectedAvatar);
            avatar2.setForeground(menuTextColour);
            avatar3.setForeground(menuTextColour);
        }
        if (e.getSource()==avatar2) {
            playerType = DungeonTileset.Player.WIZARD_2;
            avatar1.setForeground(menuTextColour);
            avatar2.setForeground(selectedAvatar);
            avatar3.setForeground(menuTextColour);
        }
        if (e.getSource()==avatar3) {
            playerType = DungeonTileset.Player.DINO_1;
            avatar1.setForeground(menuTextColour);
            avatar2.setForeground(menuTextColour);
            avatar3.setForeground(selectedAvatar);
        }
        // Set player to what the player has chosen
        game.setPlayerType(playerType);
    }
}
