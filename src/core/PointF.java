package core;

/**
 * 2D coordinate with floating point values.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class PointF {

    private float x, y;

    PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String toString() {
        return "(" + String.format("%5.2f", x) + ", " + String.format("%5.2f", y) + ")";
    }
}
