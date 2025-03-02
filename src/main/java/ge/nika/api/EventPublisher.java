package ge.nika.api;

import ge.nika.event.EventCarrier;

public interface EventPublisher {
    void publish(Event event);
    void shutDown();
}
