package camelinaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * For testing purposes only, hence why its a bit rude how its coded.
 */
public class DummyRiderFailurePublisher implements RiderFailurePublisher {

    private List<Event> events = new ArrayList<Event>();

    public class Event {
        public String appId;
        public String eventId;
        public Date timestamp;
        public String message;
    }

    public void publish(String appId, String eventId, Date timestamp, String message) {
        Event event = new Event();
        event.appId = appId;
        event.eventId = eventId;
        event.timestamp = timestamp;
        event.message = message;

        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }
}
