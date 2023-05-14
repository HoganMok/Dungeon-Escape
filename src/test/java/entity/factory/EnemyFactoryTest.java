package entity.factory;

import app.game.Game;
import entity.movable.Enemy;
import org.junit.Test;
import tileset.DungeonTileset;

import java.awt.geom.Point2D;

import static org.junit.Assert.*;

public class EnemyFactoryTest {
    @Test
    public void testCreate() {
        for (DungeonTileset.Enemy enemyType : DungeonTileset.Enemy.values()) {
            EnemyFactory factory = new EnemyFactory(new Game(null, null, null), enemyType);

            Point2D.Double position = new Point2D.Double(23, 13);

            Enemy enemy = factory.create(position);

            assertEquals(position, enemy.getPosition());
        }
    }
}
