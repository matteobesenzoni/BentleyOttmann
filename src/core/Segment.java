package core;

public class Segment {

    /* Segment id */
    private final int id;
    /* Segment a, b and c values */
    private final float a, b, c;
    /* Segment limits (t1 < t2) */
    private final float t1, t2;

    /* Segment y coordinate based on sweep line */
    private float y;

    public Segment(int id, float a, float b, float c, float t1, float t2) {
        this.id = id;
        this.a = a;
        this.b = b;
        this.c = c;
        this.t1 = t1;
        this.t2 = t2;

        y = update(t1);
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getC() {
        return c;
    }

    public float getT1() {
        return t1;
    }

    public float getT2() {
        return t2;
    }

    public int getId() {
        return id;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns this segment y coordinate
     * based on the given x coordinate.
     *
     * @param x x coordinate
     * @return y coordinate
     */
    public float calc(float x) {
        return a * x * x + b * x + c;
    }

    /**
     * Calculates and returns this segment y coordinate
     * based on the given x coordinate.
     * This class y variable is set to the
     * calculated y coordinate.
     *
     * @param x x coordinate
     * @return y coordinate
     */
    public float update(float x) {
        y = calc(x);
        return y;
    }

    @Override
    public String toString() {
        return (int) a + " " + (int) b + " " + (int) c + " " + (int) t1 + " " + (int) t2;
    }
}
