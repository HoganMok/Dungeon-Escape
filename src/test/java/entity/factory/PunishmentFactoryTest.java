package entity.factory;

import app.game.Game;
import entity.movable.item.Punishment;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class PunishmentFactoryTest {
    private PunishmentFactory factory;

    @Before
    public void setUpTests() {
        factory = new PunishmentFactory(new Game(null, null, null));
    }

    @Test
    public void testCreate() {
        Point2D.Double position = new Point2D.Double(890, 70);

        Punishment punishment = factory.create(position);

        assertEquals(position, punishment.getPosition());
    }
}
