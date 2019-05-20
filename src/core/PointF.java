package core;

/**
 * 2D coordinate with floating point values
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

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String toString() {
        return "(" + String.format("%5.2f", x) + ", " + String.format("%5.2f", y) + ")";
    }
}
