package ge.nika.sourcing;

import ge.nika.event.EventCarrier;
import ge.nika.handler.EventHandlersRunner;

public class EventConsumer {
    private EventQueue eventQueue;
    private EventHandlersRunner eventHandlersRunner;

    private final Thread thread = new Thread(() -> {
        try {
            while (!Thread.interrupted()) {
                EventCarrier eventCarrier = eventQueue.take();
                System.out.println("Consumed Event " + eventCarrier.jsonData());
                eventHandlersRunner.runHandlers(eventCarrier);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });

    public EventConsumer(EventQueue eventQueue, EventHandlersRunner eventHandlersRunner) {
        this.eventQueue = eventQueue;
        this.eventHandlersRunner = eventHandlersRunner;
    }

    public void start() {
        System.out.println("Starting event consumer");
        thread.start();
    }

    public void stop() {
        System.out.println("Stopping event consumer");
        thread.interrupt();
        var snapshot = eventQueue.getQueueSnapshot();
        System.out.println("Events still in queue: " + snapshot);
    }
}
