package entity.factory;

import app.game.Game;
import entity.movable.item.BonusReward;
import entity.movable.item.RegularReward;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class RegularRewardFactoryTest {
    private RegularRewardFactory factory;

    @Before
    public void setUpTests() {
        factory = new RegularRewardFactory(new Game(null, null, null));
    }

    @Test
    public void testCreate() {
        Point2D.Double position = new Point2D.Double(218, 320);

        RegularReward regularReward = factory.create(position);

        assertEquals(position, regularReward.getPosition());
    }
}
