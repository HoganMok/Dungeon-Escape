package entity.factory;

import app.game.Game;
import entity.movable.item.BonusReward;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class BonusRewardFactoryTest {
    private BonusRewardFactory factory;

    @Before
    public void setUpTests() {
        factory = new BonusRewardFactory(new Game(null, null, null));
    }

    @Test
    public void testCreate() {
        Point2D.Double position = new Point2D.Double(35, 60);

        BonusReward bonusReward = factory.create(position);

        assertEquals(position, bonusReward.getPosition());
    }
}
