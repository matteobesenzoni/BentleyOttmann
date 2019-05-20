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

public class StatusPanel extends JPanel {

    private static final int WIDTH = GraphPanel.SIZE;
    private static final int HEIGHT = 49;
    private static final int LEFT_PADDING = 5;

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
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setFont(label);
        FontMetrics fm = g.getFontMetrics();
        int height = fm.getHeight();

        g.drawString("Sweep Line Status:", LEFT_PADDING, height);
        g.drawString("Events:", LEFT_PADDING, height * 2);
        g.drawString("Intersections:", LEFT_PADDING, height * 3);

        g.setFont(data);

        int width = fm.stringWidth("Sweep Line Status:") + 5;
        for (int i = sweepLineStatus.size() - 1; i >= 0; i--) {
            g.drawString(
                    sweepLineStatus.get(i).getId() + (i == 0 ? "" : ","),
                    LEFT_PADDING + width + 15 * (sweepLineStatus.size() - 1 - i),
                    height);
        }

        width = fm.stringWidth("Events:") + 5;
        events.sort(new EventComparator());
        for (int i = 0; i < events.size(); i++) {
            g.drawString(String.format("%.2f", events.get(i).getPoint().getX()), LEFT_PADDING + width + 50 * i, height * 2);
        }

        width = fm.stringWidth("Intersections:") + 5;
        for (int i = 0; i < intersections.size(); i++) {
            g.drawString(intersections.get(i).toString(), LEFT_PADDING + width + 100 * i, height * 3);
        }
    }


    /**
     * Updates the data structures and refreshes the screen.
     *
     * @param segments      list of active segments
     * @param events        list of events
     * @param intersections list of intersections
     */
    public void update(NavigableSet<Segment> segments, java.util.Queue<Event> events, List<PointF> intersections) {

        sweepLineStatus.clear();
        sweepLineStatus.addAll(segments);

        this.events.clear();
        this.events.addAll(events);

        this.intersections.clear();
        this.intersections.addAll(intersections);
    }
}
