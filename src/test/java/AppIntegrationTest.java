import app.App;
import app.game.Game;
import app.game.InputManager;
import level.MapManager;
import org.junit.*;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class AppIntegrationTest {
    private Game game;
    private InputManager inputManager;
    private MapManager mapManager;

    private double previousTime = 0;
    private double loopTimeRemaining = 0;
    App app;
    @Before
    public void setUpTest(){
        app = new App();

        app.playButtonClicked();

        // Wait until the game is running.
        while (!app.isGameRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        game = app.getGame();
        inputManager = game.getInputManager();
        mapManager = game.getMapManager();
    }

    private void startLevel(String levelId) {
        mapManager.loadLevel(levelId);
        mapManager.switchToLevel(levelId);
    }

    private void setupLoopTimeRemaining(double duration) {
        loopTimeRemaining = duration;
        previousTime = System.nanoTime();
    }

    private boolean loopTimeRemaining() {
        // Update the time left.
        double currentTime = System.nanoTime();
        loopTimeRemaining -= (currentTime - previousTime) / 1_000_000_000;
        previousTime = currentTime;

        // Check if enough time passed.
        return loopTimeRemaining > 0;
    }

    private void threadSleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void startGameAndMoveDown() {
        startLevel("integration-test-level-movement");

        // Don't stop until the test is complete.
        setupLoopTimeRemaining(2);
        while (loopTimeRemaining()) {
            // Do a simulated key event.
            KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S,
                    's');
            inputManager.keyPressed(keyEvent);
        }

        Point2D.Double currentPlayerPosition = game.getCurrentPlayerPosition();
        assertEquals(144, currentPlayerPosition.getX(), 0.0);
        assertTrue(418 <= currentPlayerPosition.getY() && currentPlayerPosition.getY() <= 420);
    }

    @Test
    public void startGameAndMoveUp() {
        startLevel("integration-test-level-movement");

        // Don't stop until the test is complete.
        setupLoopTimeRemaining(2);
        while (loopTimeRemaining()) {
            // Do a simulated key event.
            KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_W,
                    'w');
            inputManager.keyPressed(keyEvent);
        }

        Point2D.Double currentPlayerPosition = game.getCurrentPlayerPosition();
        assertEquals(144, currentPlayerPosition.getX(), 0.0);
        assertTrue(143 <= currentPlayerPosition.getY() && currentPlayerPosition.getY() <= 145);
    }

    @Test
    public void startGameAndMoveLeft() {
        startLevel("integration-test-level-movement");

        // Don't stop until the test is complete.
        setupLoopTimeRemaining(2);
        while (loopTimeRemaining()) {
            // Do a simulated key event.
            KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A,
                    'a');
            inputManager.keyPressed(keyEvent);
        }

        Point2D.Double currentPlayerPosition = game.getCurrentPlayerPosition();
        assertTrue(14 <= currentPlayerPosition.getX() && currentPlayerPosition.getX() <= 16);
        assertEquals(192, currentPlayerPosition.getY(), 0.0);
    }

    @Test
    public void startGameAndMoveRight() {
        startLevel("integration-test-level-movement");

        // Don't stop until the test is complete.
        setupLoopTimeRemaining(2);
        while (loopTimeRemaining()) {
            // Do a simulated key event.
            KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D,
                    'd');
            inputManager.keyPressed(keyEvent);
        }

        Point2D.Double currentPlayerPosition = game.getCurrentPlayerPosition();
        assertTrue(427 <= currentPlayerPosition.getX() && currentPlayerPosition.getX() <= 429);
        assertEquals(192, currentPlayerPosition.getY(), 0.0);
    }

    @Test
    public void startGameAndFlipLevel() {
        startLevel("integration-test-level-flip");
        threadSleep(1000);

        KeyEvent keyEvent1 = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_F,
                'f');
        inputManager.keyReleased(keyEvent1);

        threadSleep(1000);

        KeyEvent keyEvent2 = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_F,
                'f');
        inputManager.keyReleased(keyEvent2);

        threadSleep(300);

        KeyEvent keyEvent3 = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_F,
                'f');
        inputManager.keyReleased(keyEvent3);

        threadSleep(5000);

        assertTrue(game.getIsRunning());
    }

    @Test
    public void startGameAndFlipLevelIsRejected() {
        startLevel("integration-test-level-flip-rejected");
        threadSleep(1000);

        KeyEvent keyEvent1 = new KeyEvent(game, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_F,
                'f');
        inputManager.keyReleased(keyEvent1);

        threadSleep(5000);

        assertFalse(game.getIsRunning());
    }

    @Test
    public void testCollideWithEnemies() {
        startLevel("integration-test-level-enemies");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertFalse(app.isGameRunning());
    }
    @Test
    public void testCollideWithRegularReward (){
        startLevel("integration-test-level-regularReward");
        setupLoopTimeRemaining(2);
        while (loopTimeRemaining()) {
            KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_A,
                    'a');
            inputManager.keyPressed(keyEvent);
        }
        assertEquals(100, app.getGame().getGameScore());
    }

    @Test
    public void testCollideWithBonusReward (){
        startLevel("integration-test-level-bonusReward");
        setupLoopTimeRemaining(61);
        while (loopTimeRemaining()) {
            KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_D,
                    'd');
            inputManager.keyPressed(keyEvent);
        }
        assertEquals(500, app.getGame().getGameScore());
    }

    @Test
    public void testDebugMode () {
        startLevel("integration-test-level-debugMode");
        KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_B,
                'b');
        inputManager.keyPressed(keyEvent);
        assertEquals(0, app.getGame().getGameScore());
    }

   @Test
   public void testEnteredDoor (){
       startLevel("integration-test-level-enteredDoor");
       setupLoopTimeRemaining(10);
       while (loopTimeRemaining()) {
           KeyEvent keyEvent = new KeyEvent(game, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_S,
                   's');
           inputManager.keyPressed(keyEvent);
       }
       assertFalse(app.isGameRunning());
   }
}
