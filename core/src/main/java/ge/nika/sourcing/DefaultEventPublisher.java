package ge.nika.sourcing;

import ge.nika.api.model.Event;
import ge.nika.api.model.EventPublisher;
import ge.nika.api.model.EventCarrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultEventPublisher implements EventPublisher {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final EventQueue eventQueue;

    public DefaultEventPublisher(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public void publish(Event event) {
        publish(EventCarrier.of(event));
    }

    @Override
    public void publish(EventCarrier eventCarrier) {
        executor.submit(() -> {
            try {
                eventQueue.add(eventCarrier);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutDown() {
        executor.shutdown();
        try {
            executor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
