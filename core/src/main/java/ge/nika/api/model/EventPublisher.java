package ge.nika.api.model;

import java.util.List;

public interface EventPublisher {
    void publish(Event event);
    void publish(EventCarrier eventCarrier);
    List<EventCarrier> shutDown();
}
