package gui;

import core.Event;
import core.PointF;
import core.Segment;
import parser.InputData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.NavigableSet;
import java.util.Queue;

public class Window extends JFrame {

    private final GraphPanel graphPanel;
    private final StatusPanel statusPanel;

    private boolean next, run;

    public Window(InputData data) {
        super("BentleyOttmann");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(0, 0));

        next = false;
        run = false;

        graphPanel = new GraphPanel(data.getSegments());
        // Binding ESCAPE to stop the application
        graphPanel.registerKeyboardAction(e -> System.exit(0), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Binding SPACE to execute the next instruction
        graphPanel.registerKeyboardAction(e -> next = true, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Binding R to execute all remaining instructions
        graphPanel.registerKeyboardAction(e -> {
            run = true;
            next = true;
        }, KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        statusPanel = new StatusPanel();

        add(graphPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void update(float x, NavigableSet<Segment> segments, Queue<Event> events, List<PointF> intersections) {
        graphPanel.update(x, segments, events, intersections);
        statusPanel.update(segments, events, intersections);
        repaint();

        if (!run && events.size() > 0) {
            while (!next) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            next = false;
        }

        if (events.size() == 0) {
            while (!next) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
