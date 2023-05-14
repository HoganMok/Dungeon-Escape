package app.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores the current user input from the keyboard for the game.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.3
 */
public class InputManager implements KeyListener {
    /**
     * The enum Input names for Up, Down, Left, Right, Debug, and Flip.
     */
    public enum InputName {
        UP, DOWN, LEFT, RIGHT, DEBUG, FLIP
    }

    /**
     * The enum Input types.
     */
    public enum InputType {
        JUST_PRESSED, JUST_RELEASED, IS_PRESSED
    }
    private final Map<InputName, Map<InputType, Boolean>> activeInputs;

    /**
     * Instantiates a new Input manager to manage the player's input.
     */
    public InputManager() {
        activeInputs = new HashMap<>();
        for (InputName inputName : InputName.values()) {
            Map<InputType, Boolean> activeInputTypes = new HashMap<>();
            for (InputType inputType : InputType.values()) {
                activeInputTypes.put(inputType, false);
            }

            activeInputs.put(inputName, activeInputTypes);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        InputName inputName = keyCodeToInputName(e.getKeyCode());

        // Only keep track of wanted key events.
        if (inputName == null) {
            return;
        }

        // If isPressed is false, set justPressed to true (the key was just pressed).
        if (!getInputValue(inputName, InputType.IS_PRESSED)) {
            setInputValue(inputName, InputType.JUST_PRESSED, true);
        }
        setInputValue(inputName, InputType.IS_PRESSED, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        InputName inputName = keyCodeToInputName(e.getKeyCode());

        // Only keep track of wanted key events.
        if (inputName == null) {
            return;
        }

        setInputValue(inputName, InputType.JUST_RELEASED, true);
        setInputValue(inputName, InputType.IS_PRESSED, false);
    }

    private InputName keyCodeToInputName(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> InputName.UP;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> InputName.DOWN;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> InputName.LEFT;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> InputName.RIGHT;
            case KeyEvent.VK_B  -> InputName.DEBUG;
            case KeyEvent.VK_SPACE, KeyEvent.VK_F  -> InputName.FLIP;
            default -> null;
        };
    }

    private void setInputValue(InputName inputName, InputType inputType, boolean isInputActive) {
        activeInputs.get(inputName).put(inputType, isInputActive);
    }

    /**
     * Gets input value.
     *
     * @param inputName the input name
     * @param inputType the input type
     * @return the input value
     */
    public boolean getInputValue(InputName inputName, InputType inputType) {
        return activeInputs.get(inputName).get(inputType);
    }

    /**
     * Reset inputs.
     */
    public void resetInputs() {
        InputType[] resetInputTypes = {InputType.JUST_PRESSED, InputType.JUST_RELEASED};
        for (InputType resetInputType : resetInputTypes) {
            for (InputName inputName : activeInputs.keySet()) {
                setInputValue(inputName, resetInputType, false);
            }
        }
    }
}
