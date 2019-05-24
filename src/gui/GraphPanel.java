package gui;

import core.Event;
import core.PointF;
import core.Segment;
import core.types.EventType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;

/**
 * Panel in which the segments are drawn.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class GraphPanel extends JPanel {

    /* Panel width and height */
    static final int SIZE = 800;
    /* Draw padding */
    private static final int PADDING = 50;

    /* Radius of the events dots to be drawn */
    private static final int EVENT_SIZE = 3;
    /* Intersection events dot fill color */
    private static final Color INTERSECTION_EVENT_COLOR = Color.getHSBColor(200.0f / 360.0f, 1.0f, 1.0f);

    /* Normal stroke */
    private final BasicStroke normal;
    /* Bold stroke */
    private final BasicStroke thicc;

    /* List of all segments */
    private final List<Segment> segments;
    /* Window center coordinate reference */
    private final Point center;

    /* List of active segments (sweep line status) */
    private final List<Segment> activeSegments;
    /* List of events */
    private final List<Event> events;
    /* List of intersections */
    private final List<PointF> intersections;

    /* Sweep line coordinate */
    private float sweepLine;

    /* Draw scaling to fit segments in the limited drawing area */
    private float scaling;
    /* Greatest value of any segment from the coordinate (0, 0), used for scaling */
    private float max;

    /**
     * GraphPanel constructor.
     * Initializes all data structures, sets its preferred size
     * and calculates the scaling factor.
     *
     * @param segments list of all segments
     */
    GraphPanel(List<Segment> segments) {
        setPreferredSize(new Dimension(SIZE, SIZE));

        normal = new BasicStroke(1.0f);
        thicc = new BasicStroke(2.0f);

        this.segments = segments;
        center = new Point((SIZE - 2 * PADDING) / 2, (SIZE - 2 * PADDING) / 2);

        activeSegments = new ArrayList<>();
        events = new ArrayList<>();
        intersections = new ArrayList<>();

        sweepLine = Float.MAX_VALUE * -1;

        scaling = 1;
        float t1_x = -1, t1_y = -1, t2_x = -1, t2_y = -1;
        for (Segment s : segments) {
            if (Math.abs(s.getT1()) > Math.abs(t1_x))
                t1_x = Math.abs(s.getT1());

            if (Math.abs(s.calc(s.getT1())) > Math.abs(t1_y))
                t1_y = Math.abs(s.calc(s.getT1()));

            if (Math.abs(s.getT2()) > Math.abs(t2_x))
                t2_x = Math.abs(s.getT2());

            if (Math.abs(s.calc(s.getT2())) > Math.abs(t2_y))
                t2_y = Math.abs(s.calc(s.getT2()));
        }

        max = Math.max(t2_x, Math.max(t2_y, Math.max(t1_x, t1_y)));
        scaling = (SIZE - 2 * PADDING) / (max * 2);
    }

    /**
     * Pain method.
     * Draws x and y axis, segments, events, sweep line,
     * intersections and reference values.
     *
     * @param g graphic context
     */
    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(thicc);
        g.setColor(Color.lightGray);
        // x axis
        g2.drawLine(0, PADDING + center.y, SIZE, PADDING + center.y);
        // y axis
        g2.drawLine(PADDING + center.x, 0, PADDING + center.x, SIZE);

        // reference value
        int v = (int) max;
        // reference value graphic width and height
        FontMetrics fm = g.getFontMetrics();
        int vWidth = fm.stringWidth(v + "");
        int vHeight = fm.getHeight();

        // x and y reference value ticks
        for (int k = (int) -max; k <= (int) max; k++) {
            if (k == 0)
                continue;
            g2.drawLine((int) (PADDING + center.x + k * scaling), PADDING + center.y, (int) (PADDING + center.x + k * scaling), PADDING + center.y + 10);
            g2.drawLine(PADDING + center.x - 10, (int) (PADDING + center.y - k * scaling), PADDING + center.x, (int) (PADDING + center.y - k * scaling));
        }
        // x reference value string
        g2.drawString(v + "", (int) (PADDING + center.x + v * scaling) - vWidth / 2, PADDING + center.y + 10 + vHeight);
        // y reference value string
        g2.drawString(v + "", PADDING + center.x - 10 - 5 - vWidth, (int) (PADDING + center.y - v * scaling) + vHeight / 4);

        g2.setStroke(normal);

        // segments
        g.setColor(Color.black);
        for (Segment s : segments) {
            draw(g2, s);
        }

        g2.setStroke(thicc);
        // sweep line
        g.setColor(Color.orange);
        g2.drawLine(
                PADDING + center.x + (int) (scaling * sweepLine),
                0,
                PADDING + center.x + (int) (scaling * sweepLine),
                SIZE
        );
        int sweepLineWidth = fm.stringWidth(String.format("%.2f", sweepLine));
        g2.setColor(Color.black);
        g2.drawString(String.format("%.2f", sweepLine), PADDING + center.x + (int) (scaling * sweepLine) - sweepLineWidth - 5, SIZE - 5);
        g2.setStroke(normal);

        // active segments
        g.setColor(Color.green);
        for (Segment s : activeSegments) {
            draw(g2, s);
        }

        // events
        for (Event e : events) {
            if (e.getType() == EventType.INTERSECTION)
                g.setColor(INTERSECTION_EVENT_COLOR);
            else
                g.setColor(Color.blue);
            PointF p = e.getPoint();
            g2.fillOval(
                    PADDING + center.x + Math.round(p.getX() * scaling - EVENT_SIZE),
                    PADDING + center.y - Math.round(p.getY() * scaling + EVENT_SIZE),
                    EVENT_SIZE * 2,
                    EVENT_SIZE * 2
            );
        }

        // intersections
        g.setColor(Color.red);
        for (PointF p : intersections) {
            g2.fillOval(
                    PADDING + center.x + (int) (p.getX() * scaling - EVENT_SIZE),
                    PADDING + center.y - (int) (p.getY() * scaling + EVENT_SIZE),
                    EVENT_SIZE * 2,
                    EVENT_SIZE * 2
            );
        }
    }

    /**
     * Draws a segment.
     * Distinguices between lines and curves.
     *
     * @param g graphic context
     * @param s segment to be drawn
     */
    private void draw(Graphics2D g, Segment s) {
        if (s.getA() == 0) {
            // linear
            g.drawLine(
                    PADDING + center.x + (int) (scaling * s.getT1()),
                    PADDING + center.y - (int) (scaling * s.calc(s.getT1())),
                    PADDING + center.x + (int) (scaling * s.getT2()),
                    PADDING + center.y - (int) (scaling * s.calc(s.getT2()))
            );
        } else {
            // quadratic
            for (int x = (int) (scaling * s.getT1()); x < (int) (scaling * s.getT2()); x++) {
                g.drawLine(
                        PADDING + center.x + x,
                        PADDING + center.y - (int) (scaling * s.calc(x / scaling)),
                        PADDING + center.x + x + 1,
                        PADDING + center.y - (int) (scaling * s.calc((x + 1) / scaling))
                );
            }
        }
    }

    /**
     * Updates the data structures and refreshes the screen.
     *
     * @param x             sweep line x coordinate
     * @param segments      list of active segments
     * @param events        list of events
     * @param intersections list of intersections
     */
    void update(float x, NavigableSet<Segment> segments, Queue<Event> events, List<PointF> intersections) {
        sweepLine = x;

        activeSegments.clear();
        activeSegments.addAll(segments);

        this.events.clear();
        this.events.addAll(events);

        this.intersections.clear();
        this.intersections.addAll(intersections);
    }
}
