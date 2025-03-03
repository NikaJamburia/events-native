package ge.nika.api.model;

public interface EventPublisher {
    void publish(Event event);
    void publish(EventCarrier eventCarrier);
    void shutDown();
}
