package sprite.draw;

import app.game.Game;
import audio.AudioManager;
import entity.Entity;
import entity.factory.EntityFactory;
import entity.factory.PunishmentFactory;
import level.MapManager;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CameraManagerTest {
    private JFrame frame;
    private Game game;
    private CameraManager cameraManager;
    private Entity<?> targetEntity;

    @Before
    public void setUpTests() {
        // Set up a game.
        AudioManager audioManager = new AudioManager();
        audioManager.mute();
        game = new Game(audioManager, null, null);
        game.setupGame();
        // The width and height of the level "test-level-camera" is 240 pixels.
        switchToLevel("test-level-camera");

        frame = new JFrame();
        frame.setSize(game.getPreferredSize());
        frame.add(game);
        frame.pack();

        cameraManager = new CameraManager(game);
    }

    private void switchToLevel(String levelId) {
        MapManager mapManager = game.getMapManager();
        mapManager.loadLevel(levelId);
        mapManager.switchToLevel(levelId);
    }

    private void setGameSize(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        frame.setSize(dimension);
        game.setSize(dimension);
    }

    private void setTarget() {
        // Set the target of the camera.
        EntityFactory entityFactory = new PunishmentFactory(game);
        targetEntity = entityFactory.create(new Point2D.Double(10, 20));
        cameraManager.setTarget(targetEntity.getId());
    }

    private Point2D.Double addPoints(List<Point2D.Double> points) {
        // Add the components of a list of points together.
        Point2D.Double pointsSum = new Point2D.Double(0, 0);
        for (Point2D.Double point : points) {
            pointsSum = addPoints(pointsSum, point);
        }
        return pointsSum;
    }

    private Point2D.Double addPoints(Point2D.Double point1, Point2D.Double point2) {
        // Add the components of two points together.
        return new Point2D.Double(point1.getX() + point2.getX(), point1.getY() + point2.getY());
    }

    @Test
    public void testGetAppliedCameraShiftPointLarge() {
        setGameSize(140, 140);

        Point2D.Double position = new Point2D.Double(13, 45);
        Point2D.Double offset = new Point2D.Double(-5, -10);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, null);

        assertEquals(addPoints(position, offset), newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointSmall() {
        setGameSize(340, 340);

        Point2D.Double position = new Point2D.Double(200, 150);
        Point2D.Double offset = new Point2D.Double(-10, -20);

        Point2D.Double screenOffset = new Point2D.Double(50, 50);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, null);

        // Calculate the correct final position.
        List<Point2D.Double> points = new ArrayList<>();
        points.add(position);
        points.add(offset);
        points.add(screenOffset);

        assertEquals(addPoints(points), newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointWithTargetSmall() {
        setTarget();

        setGameSize(120, 120);

        Point2D.Double position = new Point2D.Double(50, 60);
        Point2D.Double offset = new Point2D.Double(-3, 4);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, null);

        Point2D.Double expectedPosition = addPoints(position, offset);
        assertEquals(expectedPosition, newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointWithTargetLarge() {
        setTarget();

        setGameSize(500, 550);

        Point2D.Double position = new Point2D.Double(33, 27);
        Point2D.Double offset = new Point2D.Double(-3, 4);

        Point2D.Double screenOffset = new Point2D.Double(130, 155);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, null);

        // Calculate the correct final position.
        List<Point2D.Double> points = new ArrayList<>();
        points.add(position);
        points.add(offset);
        points.add(screenOffset);

        assertEquals(addPoints(points), newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftTargetSmall() {
        setTarget();

        setGameSize(140, 140);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(targetEntity);

        assertEquals(targetEntity.getPosition(), newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftOffScreenLeft() {
        setTarget();
        targetEntity.setPosition(-100, 50);

        setGameSize(170, 200);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(targetEntity);

        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftOffScreenRight() {
        setTarget();
        targetEntity.setPosition(350, 85);

        setGameSize(180, 100);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(targetEntity);

        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftOffScreenTop() {
        setTarget();
        targetEntity.setPosition(20, -200);

        setGameSize(150, 120);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(targetEntity);

        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftOffScreenBottom() {
        setTarget();
        targetEntity.setPosition(32, 245);

        setGameSize(100, 190);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(targetEntity);

        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftTargetLarge() {
        setTarget();

        setGameSize(400, 400);

        targetEntity.setPosition(80, 120);

        Point2D.Double screenOffset = new Point2D.Double(80, 80);

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(targetEntity);

        // Calculate the correct final position.
        List<Point2D.Double> points = new ArrayList<>();
        points.add(targetEntity.getPosition());
        points.add(targetEntity.getAnimationOffset());
        points.add(screenOffset);

        assertEquals(addPoints(points), newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointLargeWithAttachedSpriteLeft() {
        setGameSize(172, 138);

        Point2D.Double position = new Point2D.Double(120, 35);
        Point2D.Double offset = new Point2D.Double(2, 30);

        EntityFactory entityFactory = new PunishmentFactory(game);
        Entity<?> entity = entityFactory.create(new Point2D.Double(-500, 50));

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, entity);

        // The attached sprite is off the screen, so this point should not be drawn.
        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointLargeWithAttachedSpriteRight() {
        setGameSize(200, 300);

        Point2D.Double position = new Point2D.Double(120, 35);
        Point2D.Double offset = new Point2D.Double(2, 30);

        EntityFactory entityFactory = new PunishmentFactory(game);
        Entity<?> entity = entityFactory.create(new Point2D.Double(300, 70));

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, entity);

        // The attached sprite is off the screen, so this point should not be drawn.
        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointLargeWithAttachedSpriteTop() {
        setGameSize(200, 300);

        Point2D.Double position = new Point2D.Double(130, 20);
        Point2D.Double offset = new Point2D.Double(2, 30);

        EntityFactory entityFactory = new PunishmentFactory(game);
        Entity<?> entity = entityFactory.create(new Point2D.Double(45, -200));

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, entity);

        // The attached sprite is off the screen, so this point should not be drawn.
        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointLargeWithAttachedSpriteBottom() {
        setGameSize(200, 300);

        Point2D.Double position = new Point2D.Double(130, 20);
        Point2D.Double offset = new Point2D.Double(2, 30);

        EntityFactory entityFactory = new PunishmentFactory(game);
        Entity<?> entity = entityFactory.create(new Point2D.Double(60, 500));

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, entity);

        // The attached sprite is off the screen, so this point should not be drawn.
        assertNull(newPosition);
    }

    @Test
    public void testGetAppliedCameraShiftPointLargeWithAttachedSpriteOnScreen() {
        setGameSize(200, 300);

        Point2D.Double position = new Point2D.Double(130, 20);
        Point2D.Double offset = new Point2D.Double(2, 30);

        Point2D.Double screenOffset = new Point2D.Double(0, 30);

        EntityFactory entityFactory = new PunishmentFactory(game);
        Entity<?> entity = entityFactory.create(new Point2D.Double(80, 90));

        Point2D.Double newPosition = cameraManager.getAppliedCameraShift(position, offset, entity);

        // Calculate the correct final position.
        List<Point2D.Double> points = new ArrayList<>();
        points.add(position);
        points.add(offset);
        points.add(screenOffset);

        // The attached sprite is on the screen, so this point should be drawn.
        assertEquals(addPoints(points), newPosition);
    }
}
