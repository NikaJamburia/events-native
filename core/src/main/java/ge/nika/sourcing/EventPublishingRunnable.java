package ge.nika.sourcing;

import ge.nika.api.model.EventCarrier;
import ge.nika.handler.EventHandlersRunner;

public class EventPublishingRunnable implements Runnable {
    private final EventCarrier eventCarrier;
    private final EventHandlersRunner eventHandlersRunner;

    public EventPublishingRunnable(EventCarrier eventCarrier, EventHandlersRunner eventHandlersRunner) {
        this.eventCarrier = eventCarrier;
        this.eventHandlersRunner = eventHandlersRunner;
    }

    @Override
    public void run() {
        eventHandlersRunner.runHandlers(eventCarrier);
    }

    public EventCarrier getEventCarrier() {
        return eventCarrier;
    }
}
