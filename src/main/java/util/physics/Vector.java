package util.physics;

import java.awt.geom.Point2D;

/**
 * The type Vector.
 *
 * @author Ewan Brinkman
 * @author Hoi Chun Hogan Mok
 * @version 1.0
 */
public class Vector {
    /**
     * Gets a velocity vector and scales its magnitude to equal max.
     *
     * @param vector the velocity vector
     * @param max    the magnitude to scale the vector to
     * @return the velocity vector after scaling to the maximum velocity
     */
    public static Point2D.Double getVectorScaledToMax(Point2D.Double vector, double max) {
        double scaledX = vector.getX();
        double scaledY = vector.getY();

        if (vector.getX() != 0 && vector.getY() == 0) {
            // Limit the x velocity to a maximum value.
            if (vector.getX() > 0) {
                scaledX = max;
            } else {
                scaledX = -max;
            }
        } else if (vector.getX() == 0 && vector.getY() != 0) {
            // Limit the y velocity to a maximum value.
            if (vector.getY() > 0) {
                scaledY = max;
            } else {
                scaledY = -max;
            }
        } else if (vector.getX() != 0) {
            // Because of how the if statements are ordered, this case happens when x != 0 && y != 0. If x != 0, it will
            // always be the case that y !=0 because of the if statement ordering, so this if statement can be
            // simplified.

            // Now, the x and y velocity components are both not 0. Scale the velocity vector to be at most max in
            // magnitude. The general idea to follow is this:
            // 1. Convert the velocity vector to polar form (magnitude and direction).
            // 2. Scale the vector down to the maximum magnitude allowed.
            // 3. Convert the vector back to the rectangular form (an x and y component).
            // A simplified version of the algorithm is performed to save time.

            // Save which quadrant the velocity vector is in. This is done because the absolute value of the velocity vector
            // angle is used, which discards the information of which quadrant the velocity vector is in.
            int xSign = vector.getX() < 0 ? -1 : 1;
            int ySign = vector.getY() < 0 ? -1 : 1;

            double angle = Math.abs(getVectorAngle(vector));

            scaledX = max * Math.cos(angle) * xSign;
            scaledY = max * Math.sin(angle) * ySign;
        }

        return new Point2D.Double(scaledX, scaledY);
    }

    /**
     * Gets the arctangent angle between x and y in the vector.
     *
     * @param vector the velocity vector
     * @return the arctangent angle
     */
    public static double getVectorAngle(Point2D.Double vector) {
        return Math.atan(vector.getY() / vector.getX());
    }

    /**
     * Gets magnitude of a vector.
     *
     * @param vector the velocity vector
     * @return the vector's magnitude
     */
    public static double getVectorMagnitude(Point2D.Double vector) {
        // A vector's magnitude is simply its distance to the origin.
        return vector.distance(0, 0);
    }
}
