package core.comparators;

import core.Event;

import java.util.Comparator;

/**
 * Events comparator based on the events x coordinate.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class EventComparator implements Comparator<Event> {

    /**
     * Compares the two events based on
     * their x coordinate.
     *
     * @param e1 First event
     * @param e2 Second event
     * @return 0 if the coordinates are equal, a negative value if
     * e1 is before e2, a positive value otherwise.
     */
    @Override
    public int compare(Event e1, Event e2) {
        return Float.compare(e1.getPoint().getX(), e2.getPoint().getX());
    }
}
