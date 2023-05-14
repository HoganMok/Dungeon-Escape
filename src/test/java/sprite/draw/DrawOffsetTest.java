package sprite.draw;

import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DrawOffsetTest {
    private enum DrawOffsetAnimationTest {
        TEST
    }

    @Test
    public void testGetOffsetNoFlip() {
        Map<DrawOffsetAnimationTest, Point2D.Double> offsets = new HashMap<>();
        Point2D.Double offset = new Point2D.Double(10, 20);
        offsets.put(DrawOffsetAnimationTest.TEST, offset);

        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(-5, 0);

        DrawOffset<DrawOffsetAnimationTest> drawOffset = new DrawOffset<>(offsets, flipAnimationHorizontallyOffset);

        assertEquals(new Point2D.Double(10, 20), drawOffset.getOffset(DrawOffsetAnimationTest.TEST, false));
    }

    @Test
    public void testGetOffsetWithFlip() {
        Map<DrawOffsetAnimationTest, Point2D.Double> offsets = new HashMap<>();
        Point2D.Double offset = new Point2D.Double(35, 15);
        offsets.put(DrawOffsetAnimationTest.TEST, offset);

        Point2D.Double flipAnimationHorizontallyOffset = new Point2D.Double(-10, -4);

        DrawOffset<DrawOffsetAnimationTest> drawOffset = new DrawOffset<>(offsets, flipAnimationHorizontallyOffset);

        assertEquals(new Point2D.Double(25, 11), drawOffset.getOffset(DrawOffsetAnimationTest.TEST, true));
    }
}
