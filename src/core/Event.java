package core;

import core.types.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Event {

    /* Segments related to this event */
    private final List<Segment> segments;
    /* Type of event (START, END, INTERSECTION) */
    private final EventType type;
    /* Event position (x, y) */
    private final PointF point;

    /**
     * Event constructor.
     *
     * @param type     event type
     * @param point    event position
     * @param segments segments of the event
     */
    Event(EventType type, PointF point, Segment... segments) {
        this.segments = new ArrayList<>();
        Collections.addAll(this.segments, segments);
        this.type = type;
        this.point = point;
    }

    List<Segment> getSegments() {
        return segments;
    }

    public PointF getPoint() {
        return point;
    }

    public EventType getType() {
        return type;
    }
}
