package entity.factory;

import app.game.Game;
import entity.Door;
import org.junit.Test;
import tileset.DungeonTileset;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class DoorFactoryTest {
    @Test
    public void testCreate() {
        for (DungeonTileset.Door doorType : DungeonTileset.Door.values()) {
            DoorFactory factory = new DoorFactory(new Game(null, null, null), doorType);

            Point2D.Double position = new Point2D.Double(312, 480);

            Door door = factory.create(position);

            assertEquals(position, door.getPosition());
        }
    }
}
