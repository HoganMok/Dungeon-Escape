package app.game;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class InputManagerTest {

    private InputManager inputManager;
    private JPanel panel;

    @Before
    public void setUpTests() {
        inputManager = new InputManager();
        panel = new JPanel();
    }

    private Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> getActiveInputs() {
        // Store all active inputs.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputs = new HashMap<>();

        // Get all input types for each input name.
        for (InputManager.InputName inputName : InputManager.InputName.values()) {
            Map<InputManager.InputType, Boolean> inputNameActiveInputTypes = new HashMap<>();
            for (InputManager.InputType inputType : InputManager.InputType.values()) {
                inputNameActiveInputTypes.put(inputType, inputManager.getInputValue(inputName, inputType));
            }

            activeInputs.put(inputName, inputNameActiveInputTypes);
        }

        return activeInputs;
    }

    @Test
    public void testAllValidKeysPressed() {
        Map<Integer, InputManager.InputName> keyCodeToInputName = new HashMap<>();
        keyCodeToInputName.put(KeyEvent.VK_W, InputManager.InputName.UP);
        keyCodeToInputName.put(KeyEvent.VK_UP, InputManager.InputName.UP);
        keyCodeToInputName.put(KeyEvent.VK_S, InputManager.InputName.DOWN);
        keyCodeToInputName.put(KeyEvent.VK_DOWN, InputManager.InputName.DOWN);
        keyCodeToInputName.put(KeyEvent.VK_A, InputManager.InputName.LEFT);
        keyCodeToInputName.put(KeyEvent.VK_LEFT, InputManager.InputName.LEFT);
        keyCodeToInputName.put(KeyEvent.VK_D, InputManager.InputName.RIGHT);
        keyCodeToInputName.put(KeyEvent.VK_RIGHT, InputManager.InputName.RIGHT);
        keyCodeToInputName.put(KeyEvent.VK_B, InputManager.InputName.DEBUG);
        keyCodeToInputName.put(KeyEvent.VK_SPACE, InputManager.InputName.FLIP);
        keyCodeToInputName.put(KeyEvent.VK_F, InputManager.InputName.FLIP);

        for (Map.Entry<Integer, InputManager.InputName> inputAssociation : keyCodeToInputName.entrySet()) {
            // Get the status of key presses before simulating a key press.
            Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsExpected = getActiveInputs();

            // Do a simulated key event.
            KeyEvent keyEvent = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                    inputAssociation.getKey(), ' ');
            inputManager.keyPressed(keyEvent);

            // Update the expected active input with the expected result.
            activeInputsExpected.get(inputAssociation.getValue()).put(InputManager.InputType.JUST_PRESSED, true);
            activeInputsExpected.get(inputAssociation.getValue()).put(InputManager.InputType.IS_PRESSED, true);

            assertEquals(activeInputsExpected, getActiveInputs());
        }
    }

    @Test
    public void testInvalidKeyPressed() {
        // Get the status of key presses before simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsBefore = getActiveInputs();

        // Do a simulated key event.
        KeyEvent keyEvent = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, -1, ' ');
        inputManager.keyPressed(keyEvent);

        // Get the status of key presses after simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsAfter = getActiveInputs();

        assertEquals(activeInputsBefore, activeInputsAfter);
    }

    @Test
    public void testAllValidKeysReleased() {
        // Get the status of key presses before simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsBefore = getActiveInputs();

        Map<Integer, InputManager.InputName> keyCodeToInputName = new HashMap<>();
        keyCodeToInputName.put(KeyEvent.VK_W, InputManager.InputName.UP);
        keyCodeToInputName.put(KeyEvent.VK_UP, InputManager.InputName.UP);
        keyCodeToInputName.put(KeyEvent.VK_S, InputManager.InputName.DOWN);
        keyCodeToInputName.put(KeyEvent.VK_DOWN, InputManager.InputName.DOWN);
        keyCodeToInputName.put(KeyEvent.VK_A, InputManager.InputName.LEFT);
        keyCodeToInputName.put(KeyEvent.VK_LEFT, InputManager.InputName.LEFT);
        keyCodeToInputName.put(KeyEvent.VK_D, InputManager.InputName.RIGHT);
        keyCodeToInputName.put(KeyEvent.VK_RIGHT, InputManager.InputName.RIGHT);
        keyCodeToInputName.put(KeyEvent.VK_B, InputManager.InputName.DEBUG);
        keyCodeToInputName.put(KeyEvent.VK_SPACE, InputManager.InputName.FLIP);
        keyCodeToInputName.put(KeyEvent.VK_F, InputManager.InputName.FLIP);

        for (Map.Entry<Integer, InputManager.InputName> inputAssociation : keyCodeToInputName.entrySet()) {
            // Get the status of key presses before simulating a key press.
            Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsExpected = getActiveInputs();

            // Do a simulated key event press.
            KeyEvent keyEventPressed = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0,
                    inputAssociation.getKey(), ' ');
            inputManager.keyPressed(keyEventPressed);

            inputManager.resetInputs();

            // Do a simulated key event release.
            KeyEvent keyEventReleased = new KeyEvent(panel, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0,
                    inputAssociation.getKey(), ' ');
            inputManager.keyReleased(keyEventReleased);

            // Update the expected active input with the expected result.
            activeInputsExpected.get(inputAssociation.getValue()).put(InputManager.InputType.JUST_RELEASED, true);
            activeInputsExpected.get(inputAssociation.getValue()).put(InputManager.InputType.IS_PRESSED, false);

            assertEquals(activeInputsExpected, getActiveInputs());

            inputManager.resetInputs();

            assertEquals(activeInputsBefore, getActiveInputs());
        }
    }

    @Test
    public void testInvalidKeyReleased() {
        // Get the status of key presses before simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsBefore = getActiveInputs();

        // Do a simulated key event.
        KeyEvent keyEvent = new KeyEvent(panel, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, -1, ' ');
        inputManager.keyReleased(keyEvent);

        // Get the status of key presses after simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsAfter = getActiveInputs();

        assertEquals(activeInputsBefore, activeInputsAfter);
    }

    @Test
    public void testKeyTyped() {
        // Get the status of key presses before simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsBefore = getActiveInputs();

        // Do a simulated key event.
        KeyEvent keyEvent = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W,
                'w');
        inputManager.keyTyped(keyEvent);

        // Get the status of key presses after simulating a key press.
        Map<InputManager.InputName, Map<InputManager.InputType, Boolean>> activeInputsAfter = getActiveInputs();

        assertEquals(activeInputsBefore, activeInputsAfter);
    }

    @Test
    public void testHoldingDownAKey() {
        // Do a simulated key event.
        KeyEvent keyEvent1 = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A,
                'a');
        inputManager.keyPressed(keyEvent1);

        // Make sure input is set correctly.
        assertTrue(inputManager.getInputValue(InputManager.InputName.LEFT, InputManager.InputType.JUST_PRESSED));
        assertTrue(inputManager.getInputValue(InputManager.InputName.LEFT, InputManager.InputType.IS_PRESSED));

        // Update the input, as it is done every game loop.
        inputManager.resetInputs();

        // Do another simulated key event.
        KeyEvent keyEvent2 = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A,
                'a');
        inputManager.keyPressed(keyEvent2);

        // Make sure input is set correctly. JUST_PRESSED should now be false.
        assertFalse(inputManager.getInputValue(InputManager.InputName.LEFT, InputManager.InputType.JUST_PRESSED));
        assertTrue(inputManager.getInputValue(InputManager.InputName.LEFT, InputManager.InputType.IS_PRESSED));
    }

    @Test
    public void testResetInput() {
        // Do a simulated key event.
        KeyEvent keyEvent = new KeyEvent(panel, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W,
                'w');
        inputManager.keyPressed(keyEvent);

        // Check that resetting input no longer makes the key event be active.
        assertTrue(inputManager.getInputValue(InputManager.InputName.UP, InputManager.InputType.JUST_PRESSED));
        inputManager.resetInputs();
        assertFalse(inputManager.getInputValue(InputManager.InputName.UP, InputManager.InputType.JUST_PRESSED));
    }
}
