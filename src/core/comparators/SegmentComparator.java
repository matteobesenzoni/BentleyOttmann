package core.comparators;

import core.Segment;

import java.util.Comparator;

/**
 * Segments comparator based the segments y coordinate.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class SegmentComparator implements Comparator<Segment> {

    /**
     * Compares the two segments based on
     * their y coordinate.
     *
     * @param s1 First segment
     * @param s2 Second segment
     * @return 0 if the coordinates are equal, a negative value if
     * s1 is before s2, a positive value otherwise.
     */
    @Override
    public int compare(Segment s1, Segment s2) {
        return Float.compare(s2.getY(), s1.getY());
    }
}
