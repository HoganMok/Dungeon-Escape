package entity.factory;

import app.game.Game;
import entity.movable.Player;
import org.junit.Test;
import tileset.DungeonTileset;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class PlayerFactoryTest {
    @Test
    public void testCreate() {
        for (DungeonTileset.Player playerType : DungeonTileset.Player.values()) {
            PlayerFactory factory = new PlayerFactory(new Game(null, null, null), playerType);

            Point2D.Double position = new Point2D.Double(80, 20);

            Player player = factory.create(position);

            assertEquals(position, player.getPosition());
        }
    }
}
