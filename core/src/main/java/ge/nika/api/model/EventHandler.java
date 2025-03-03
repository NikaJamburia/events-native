package ge.nika.api.model;

public interface EventHandler {
    void accept(EventCarrier eventCarrier);
    String eventClassName();
}


