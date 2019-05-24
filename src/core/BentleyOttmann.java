package core;

import core.comparators.EventComparator;
import core.comparators.SegmentComparator;
import core.types.EventType;
import parser.InputData;
import parser.Instruction;

import java.util.*;

/**
 * Bentley-Ottmann algorithm for segments intersection.
 * Supports linear and quadratic functions as segments.
 * <p>
 * Partially based on the BentleyOttmann implementation
 * found at https://github.com/valenpe7/bentley-ottmann
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class BentleyOttmann {

    /* Priority queue of future events */
    private final Queue<Event> events;
    /* Balanced binary tree of segments intersecting the sweep line */
    private final NavigableSet<Segment> sweepLine;
    /* List of intersections found */
    private final List<PointF> intersections;

    /* Number of segments in the input data */
    private final int startingSegments;

    /* Sweep line x coordinate */
    private float x;

    /**
     * BentleyOttmann constructor.
     * Initializes all data structures and
     * creates the START and END events for
     * each segment in the input data.
     *
     * @param data input data
     */
    public BentleyOttmann(InputData data) {

        startingSegments = data.getSegments().size();

        events = new PriorityQueue<>(new EventComparator());
        sweepLine = new TreeSet<>(new SegmentComparator());
        intersections = new ArrayList<>();

        for (Segment s : data.getSegments()) {
            events.add(new Event(EventType.START, new PointF(s.getT1(), s.calc(s.getT1())), s));
            events.add(new Event(EventType.END, new PointF(s.getT2(), s.calc(s.getT2())), s));
        }

        x = Float.MAX_VALUE * -1;
    }

    /**
     * Executes the given instruction.
     * If wait is true, this function returns only
     * when the SPACE is pressed on a keyboard.
     *
     * @param i instruction to execute
     */
    public void executeInstruction(Instruction i) {

        switch (i.getType()) {
            case STEP:
                step(false);
                return;
            case STEP_P:
                step(true);
                return;
            case STATUS:
                status();
                return;
            case RUN:
                run();
        }
    }

    /**
     * Executes the STEP instruction.
     * If there are still events to process,
     * it checks the type of the event (START,
     * END, INTERSECTION) and executes the necessary
     * steps.
     * <p>
     * START
     * Updates the binary tree based on the sweep line
     * position, adds the segment to it
     * and checks for the existence of segments
     * above and below. If they do exist, an
     * intersection check is executed.
     * If both the upper and lower segments exist,
     * any future event between the two is
     * removed to avoid duplicates.
     * <p>
     * END
     * Checks for any intersections between the upper
     * and lower segments and removes the current one from
     * the active segments list.
     * <p>
     * INTERSECTION
     * Swaps the segments intersecting and checks for any
     * intersection between the two and their new neighbours.
     * Creates a new intersection entry.
     *
     * @param print if true, prints the event type and
     *              the number of intersections found
     */
    private void step(boolean print) {

        if (events.isEmpty()) {
            System.out.println("error: no more events");
            return;
        }

        Event e = events.poll();
        x = e.getPoint().getX();
        Segment s = e.getSegments().get(0);
        Segment lower, higher;

        int n_int = 0;

        switch (e.getType()) {
            case START:
                updateSegments(x);
                sweepLine.add(s);

                lower = sweepLine.lower(s);
                higher = sweepLine.higher(s);

                if (lower != null)
                    n_int += checkIntersection(lower, s, x);
                if (higher != null)
                    n_int += checkIntersection(higher, s, x);
                if (lower != null && higher != null)
                    removeFutureEvent(lower, higher);
                break;
            case END:
                lower = sweepLine.lower(s);
                higher = sweepLine.higher(s);
                if (lower != null && higher != null)
                    n_int += checkIntersection(lower, higher, x);
                sweepLine.remove(s);
                break;
            case INTERSECTION:
                if (e.getSegments().size() == 2) {
                    Segment s1 = e.getSegments().get(0);
                    Segment s2 = e.getSegments().get(1);
                    swap(s1, s2);
                    if (s1.getY() < s2.getY()) {
                        lower = sweepLine.lower(s2);
                        higher = sweepLine.higher(s1);
                        if (lower != null) {
                            n_int += checkIntersection(lower, s2, x);
                            removeFutureEvent(lower, s1);
                        }
                        if (higher != null) {
                            n_int += checkIntersection(higher, s1, x);
                            removeFutureEvent(higher, s2);
                        }
                    } else {
                        lower = sweepLine.lower(s1);
                        higher = sweepLine.higher(s2);
                        if (lower != null) {
                            n_int += checkIntersection(lower, s1, x);
                            removeFutureEvent(lower, s2);
                        }
                        if (higher != null) {
                            n_int += checkIntersection(higher, s2, x);
                            removeFutureEvent(higher, s1);
                        }
                    }
                    intersections.add(e.getPoint());
                } else {
                    // TODO: implement multi-event
                }
                break;
        }

        if (print) {
            System.out.print("event: ");
            switch (e.getType()) {
                case START:
                    System.out.print("S");
                    break;
                case END:
                    System.out.print("E");
                    break;
                case INTERSECTION:
                    System.out.print("I");
                default:
                    break;
            }
            System.out.println(String.format("%6.2f %d", x, n_int));
        }
    }

    /**
     * Executes the STATUS instruction.
     * Prints "status: %1 %2...\n" where
     * [%1] is the number of active segments and
     * [%2...] are the ids of such segments from top
     * to bottom.
     */
    private void status() {

        System.out.print("status: ");
        System.out.print(sweepLine.size() + ":");
        for (Segment s : sweepLine.descendingSet()) {
            System.out.print(" " + s.getId());
        }
        System.out.println();
    }

    /**
     * Executes the RUN instruction.
     * Runs the STEP instruction until
     * the event queue is empty.
     * It then prints "summary: %1 segments, %2 intersections\n" where
     * [%1] is the number of segments in the input data and
     * [%2] is the number of intersections found.
     */
    private void run() {
        while (!events.isEmpty())
            step(false);
        System.out.println(String.format("summary: %d segments, %d intersections", startingSegments, intersections.size()));
    }

    /**
     * Updates the sweep line status based on
     * the given x coordinate.
     *
     * @param x sweep line coordinate
     */
    private void updateSegments(float x) {
        for (Segment segment : sweepLine) {
            segment.update(x);
        }
    }

    /**
     * Checks for intersections between the two given segments
     * based the sweep line coordinate x.
     *
     * @param s1 First segment
     * @param s2 Second segment
     * @param x  sweep line coordinate
     * @return number of intersections found
     */
    private int checkIntersection(Segment s1, Segment s2, float x) {

        float a1 = s1.getA();
        float b1 = s1.getB();
        float c1 = s1.getC();

        float a2 = s2.getA();
        float b2 = s2.getB();
        float c2 = s2.getC();

        // a1 and a2 are 0 or equal
        if (a1 == 0.0f && a2 == 0.0f || Float.compare(a1, a2) == 0) {
            if (Float.compare(b1, b2) == 0)
                return 0; // omitting parallel overlapping segments
            float intersection = (c2 - c1) / (b1 - b2);
            if (intersection > x && intersection >= s1.getT1() && intersection <= s1.getT2() && intersection >= s2.getT1() && intersection <= s2.getT2()) {
                events.add(new Event(EventType.INTERSECTION, new PointF(intersection, s1.calc(intersection)), s1, s2));
                //System.out.println("+ EVENT at " + i + "(" + s1.getId() + ", " + s2.getId() + ")");
                return 1;
            }
            return 0;

        } else {
            int n_int = 0;

            float a = a1 - a2;
            float b = b1 - b2;
            float c = c1 - c2;

            // positive a
            if (a < 0) {
                a *= -1;
                b *= -1;
                c *= -1;
            }

            // delta
            float d = b * b - 4 * a * c;
            if (d < 0)
                return 0;

            float x1 = (-b - (float) Math.sqrt(d)) / (2.0f * a);
            float x2 = (-b + (float) Math.sqrt(d)) / (2.0f * a);

            if (x1 >= x && x1 <= s1.getT2() && x1 <= s2.getT2()) {
                events.add(new Event(EventType.INTERSECTION, new PointF(x1, s1.calc(x1)), s1, s2));
                n_int++;
                //System.out.println("+ EVENT at " + x1 + "(" + s1.getId() + ", " + s2.getId() + ")");
            }

            if (Float.compare(x1, x2) != 0 && x2 >= x && x2 <= s1.getT2() && x2 <= s2.getT2()) {
                events.add(new Event(EventType.INTERSECTION, new PointF(x2, s1.calc(x2)), s1, s2));
                n_int++;
                //System.out.println("+ EVENT at " + x2 + "(" + s1.getId() + ", " + s2.getId() + ")");
            }
            return n_int;
        }
    }

    /**
     * Removes intersection events from the
     * event queue for the given two segments.
     *
     * @param s1 First segment
     * @param s2 Second segment
     */
    private void removeFutureEvent(Segment s1, Segment s2) {
        Iterator<Event> it = events.iterator();
        while (it.hasNext()) {
            Event e = it.next();
            if (e.getType() == EventType.INTERSECTION) {
                if (e.getSegments().contains(s1) && e.getSegments().contains(s2)) {
                    //System.out.println("- EVENT at " + e.getPoint().getX() + "(" + s1.getId() + ", " + s2.getId() + ")");
                    it.remove();
                }
            }
        }
    }

    /**
     * Swaps the two given segments in
     * the event queue.
     *
     * @param s1 First segment
     * @param s2 Second segment
     */
    private void swap(Segment s1, Segment s2) {
        sweepLine.remove(s1);
        sweepLine.remove(s2);
        float y = s1.getY();
        s1.setY(s2.getY());
        s2.setY(y);
        sweepLine.add(s1);
        sweepLine.add(s2);
    }

    /**
     * Returns the x coordinate of the
     * sweep line.
     *
     * @return sweep line x coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the event queue.
     *
     * @return event queue
     */
    public Queue<Event> getEvents() {
        return events;
    }

    /**
     * Returns the sweepLine.
     *
     * @return sweepLine
     */
    public NavigableSet<Segment> getSweepLine() {
        return sweepLine;
    }

    /**
     * Returns the list of intersections.
     *
     * @return list of intersections
     */
    public List<PointF> getIntersections() {
        return intersections;
    }
}
