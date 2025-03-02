package ge.nika.api;

public interface EventPublisher {
    void publish(Event event);
    void shutDown();
}
