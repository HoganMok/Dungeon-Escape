package level;
import app.game.Game;
import org.junit.*;
import static org.junit.Assert.*;

public class LevelTest {
    private MapManager mapManager;
    private Game game;

    @Before
    public void setUpTest(){
        game = new Game(null, null, null);
        mapManager = game.getMapManager();
        game.setupGame();
    }

    @Test
    public void testLevel(){
        Level level = new Level(game, "TestLevel");
        assertEquals("Test Level", level.getName());
        assertEquals(5, level.getTileHeight());
        assertEquals(5, level.getTileWidth());
    }

    @Test
    public void testLoadCurrentLevel(){
        mapManager.loadLevel("TestLevel");
        mapManager.switchToLevel("TestLevel");
        assertEquals(18, game.getEnemySpriteIds().size());
    }
}
