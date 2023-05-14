package app;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// TO FIX: Windows popping up during tests
public class AppTest {
    App appTest;

    @Before
    public void setUpTests() {
        appTest = new App();
    }

    // Testing visibility of all panels and that the buttons do what they're supposed to
    @Test
    public void testPanels() {
        // Testing that the initial state is correct: the Title Screen panel is visible at the start
        assertTrue(appTest.titleScreen.isVisible());

        // Test that the Settings screen panel appears when button is pressed
        JButton settingsButton = appTest.titleScreen.settings;
        settingsButton.doClick();
        assertFalse(appTest.titleScreen.isVisible());
        assertTrue(appTest.settings.isVisible());

        // Test that the Choose Avatar screen panel appears when button is pressed (AppTest)
        JButton avatarButton = appTest.titleScreen.avatar;
        avatarButton.doClick();
        assertFalse(appTest.titleScreen.isVisible());
        assertTrue(appTest.avatar.isVisible());

    }

    @Test
    public void settingsTest() {
        // Test that the audio on/off switch works (SettingsTest ?)
        JButton audioButton = appTest.settings.audioSwitch;
        audioButton.doClick(); // off
        audioButton.doClick(); // on

        // Test that the How To Play screen panel appears when button is pressed
        JButton howToPlayButton = appTest.settings.helpButton;
        howToPlayButton.doClick();
        assertFalse(appTest.settings.isVisible());
        assertTrue(appTest.helpPage.isVisible());

        // Test that the How To Play back button works
        JButton backToSettingsButton = appTest.helpPage.back;
        backToSettingsButton.doClick();
        assertFalse(appTest.helpPage.isVisible());
        assertTrue(appTest.settings.isVisible());

        // Test that the Settings back button works
        JButton backToTitleScreenFromSettingsButton = appTest.settings.back;
        backToTitleScreenFromSettingsButton.doClick();
        assertFalse(appTest.settings.isVisible());
        assertTrue(appTest.titleScreen.isVisible());
    }

    @Test
    public void avatarTest() {
        // Test that different avatars are correctly selected (ChooseAvatarTest ?)
        JButton avatar1 = appTest.avatar.avatar1;
        avatar1.doClick();

        JButton avatar2 = appTest.avatar.avatar2;
        avatar2.doClick();

        JButton avatar3 = appTest.avatar.avatar3;
        avatar3.doClick();

        // Test that the Avatar back button works
        JButton backToTitleScreenFromAvatarButton = appTest.avatar.back;
        backToTitleScreenFromAvatarButton.doClick();
        assertFalse(appTest.avatar.isVisible());
        assertTrue(appTest.titleScreen.isVisible());
    }

    @Test
    public void gameTest() {
        // Test that the Game screen panel appears when button is pressed and game loop begins
        JButton playButton = appTest.titleScreen.play;
        playButton.doClick();
        assertFalse(appTest.titleScreen.isVisible());
        assertTrue(appTest.game.isVisible());

        // Test that the Game Over Screen panel appears when the player dies
        // Test that the return to title screen button works
        JButton backToTitleFromGameOver = appTest.gameOverScreen.back;
        backToTitleFromGameOver.doClick();
        assertFalse(appTest.gameOverScreen.isVisible());
        assertTrue(appTest.titleScreen.isVisible());
    }
}
