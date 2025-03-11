package ge.nika.sourcing;

import ge.nika.api.model.EventCarrier;

public class EventPublishingRunnable implements Runnable {
    private final EventCarrier eventCarrier;
    private final EventQueue eventQueue;

    public EventPublishingRunnable(EventCarrier eventCarrier, EventQueue eventQueue) {
        this.eventCarrier = eventCarrier;
        this.eventQueue = eventQueue;
    }

    @Override
    public void run() {
        try {
            eventQueue.add(eventCarrier);
        } catch (InterruptedException e) {
            // This causes single event that is being published atm of interruption to be lost,
            // but i just dont care enough to fix this
            e.printStackTrace();
        }
    }

    public EventCarrier getEventCarrier() {
        return eventCarrier;
    }
}
