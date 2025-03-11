package ge.nika.sourcing;

import ge.nika.api.model.Event;
import ge.nika.api.model.EventPublisher;
import ge.nika.api.model.EventCarrier;

import java.util.List;
import java.util.concurrent.*;

public class DefaultEventPublisher implements EventPublisher {

    private final ExecutorService executor = EventPublishingThreadExecutorService.singleThreaded();
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
        var runnable = new EventPublishingRunnable(eventCarrier, eventQueue);
        executor.submit(runnable);
    }

    @Override
    public List<EventCarrier> shutDown() {
        var queuedTasks = executor.shutdownNow();

        var queuedEventCarriers = queuedTasks
                .stream()
                .map(it -> ((EventPublishingRunnableFuture) it).getWrappedRunnable().getEventCarrier())
                .toList();

        try {
            executor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return queuedEventCarriers;
    }
}
