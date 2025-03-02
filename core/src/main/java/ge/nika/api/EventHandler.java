package ge.nika.api;

import ge.nika.event.EventCarrier;

public interface EventHandler {
    void accept(EventCarrier eventCarrier);
    String eventClassName();
}


