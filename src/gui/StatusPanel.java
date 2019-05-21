package gui;

import core.Event;
import core.PointF;
import core.Segment;
import core.comparators.EventComparator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;

/**
 * Panel showing the data structures status.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class StatusPanel extends JPanel {

    private static final int WIDTH = 200;
    private static final int HEIGHT = GraphPanel.SIZE;
    private static final int PADDING = 5;

    private final Font label, data;

    private final List<Segment> sweepLineStatus;
    private final List<Event> events;
    private final List<PointF> intersections;

    StatusPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        int fontSize = 12;
        label = new Font("Courier New", Font.BOLD, fontSize);
        data = new Font("Courier New", Font.PLAIN, fontSize);

        sweepLineStatus = new ArrayList<>();
        events = new ArrayList<>();
        intersections = new ArrayList<>();
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.darkGray);
        g.drawLine(0, 0, 0, getHeight() - 1);

        g.setFont(label);
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight();

        g.setColor(Color.lightGray);
        g.drawLine(PADDING + 26, 0, PADDING + 26, getHeight());
        g.drawLine(PADDING + 86, 0, PADDING + 86, getHeight());
        g.drawLine(0, PADDING + height + 5, getWidth(), PADDING + height + 5);

        g.setColor(Color.black);
        g.drawString("SLS", PADDING, PADDING + height);
        g.drawString("Events", PADDING + 35, PADDING + height);
        g.drawString("Intersections", PADDING + 95, PADDING + height);


        g.setFont(data);

        // Sweep Line Status
        for (int i = sweepLineStatus.size() - 1; i >= 0; i--) {
            g.drawString(
                    String.format("%2d", sweepLineStatus.get(i).getId()),
                    PADDING + 5,
                    PADDING + 5 + height * (sweepLineStatus.size() + 1 - i)
            );
        }

        events.sort(new EventComparator());
        for (int i = 0; i < events.size(); i++) {
            g.drawString(
                    String.format("%5.2f", events.get(i).getPoint().getX()),
                    PADDING + 35,
                    PADDING + 5 + height * (i + 2)
            );
        }

        for (int i = 0; i < intersections.size(); i++) {
            g.drawString(
                    intersections.get(i).toString(),
                    PADDING + 95,
                    PADDING + 5 + height * (i + 2)
            );
        }
    }


    /**
     * Updates the data structures and refreshes the screen.
     *
     * @param segments      list of active segments
     * @param events        list of events
     * @param intersections list of intersections
     */
    void update(NavigableSet<Segment> segments, Queue<Event> events, List<PointF> intersections) {

        sweepLineStatus.clear();
        sweepLineStatus.addAll(segments);

        this.events.clear();
        this.events.addAll(events);

        this.intersections.clear();
        this.intersections.addAll(intersections);
    }
}
