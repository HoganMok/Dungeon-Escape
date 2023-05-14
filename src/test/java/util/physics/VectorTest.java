package util.physics;

import org.junit.Test;
import org.junit.Before;
import java.awt.geom.Point2D;
import static org.junit.Assert.*;

public class VectorTest {
    private Point2D.Double point;
    double acceleration;

    @Before
    public void setUpTests() {
        point = new Point2D.Double(0, 0);
        acceleration = 400;
    }
    @Test
    public void testInstantiatingClass() {
        // Test instantiating the class.
        Vector vector = new Vector();
    }

    @Test
    public void testGetVectorScaledToMax() {
        /* ------------- Test 1: X & Y = 0 ------------- */
        point.setLocation(0, 0);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(0, 0), point);

        /* ------------- Test 2: X > 0 & Y = 0 ------------- */
        point.setLocation(10, 0);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(400, 0), point);

        /* ------------- Test 3: X < 0 & Y = 0 ------------- */
        point.setLocation(-10, 0);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(-400, 0), point);

        /* ------------- Test 4: X = 0 & Y > 0 ------------- */
        point.setLocation(0, 20);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(0, 400), point);

        /* ------------- Test 5: X = 0 & Y < 0 ------------- */
        point.setLocation(0, -20);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(0, -400), point);

        /* ------------- Test 6: X & Y > 0 ------------- */
        point.setLocation(1, 1);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(282.842712474619, 282.84271247461896), point);

        /* ------------- Test 7: X > 0 & Y < 0 ------------- */
        point.setLocation(1, -1);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(282.842712474619, -282.84271247461896), point);

        /* ------------- Test 8: X < 0 & Y > 0 ------------- */
        point.setLocation(-1, 1);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(-282.842712474619, 282.84271247461896), point);

        /* ------------- Test 9: X < 0 & Y < 0 ------------- */
        point.setLocation(-1, -1);
        point = Vector.getVectorScaledToMax(point, acceleration);
        assertEquals(new Point2D.Double(-282.842712474619, -282.84271247461896), point);
    }

    @Test
    public void testGetVectorMagnitude() {
        /* ------------- Test 10: Get magnitude X & Y = 0 ------------- */
        point.setLocation(0, 0);
        double mag = Vector.getVectorMagnitude(point);
        assertEquals(0.0, mag, 0);

        /* ------------- Test 11: Get magnitude X & Y > 0 ------------- */
        point.setLocation(3, 4);
        mag = Vector.getVectorMagnitude(point);
        assertEquals(5.0, mag, 0);
    }


}
