package ge.nika.sourcing;

import ge.nika.api.model.Event;
import ge.nika.api.model.EventPublisher;
import ge.nika.api.model.EventCarrier;
import ge.nika.handler.EventHandlersRunner;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class DefaultEventPublisher implements EventPublisher {

    private final EventPublishingThreadExecutorService executor = EventPublishingThreadExecutorService.singleThreaded();
    private final EventHandlersRunner eventHandlersRunner;

    public DefaultEventPublisher(EventHandlersRunner eventHandlersRunner) {
        this.eventHandlersRunner = eventHandlersRunner;
    }

    @Override
    public void publish(Event event) {
        publish(EventCarrier.of(event));
    }

    @Override
    public void publish(EventCarrier eventCarrier) {
        var runnable = new EventPublishingRunnable(eventCarrier, eventHandlersRunner);
        executor.submit(runnable);
    }

    @Override
    public List<EventCarrier> shutdown() {
        var queuedTasks = executor.shutdownNow();

        var queuedEventCarriers = getEventCarriers(queuedTasks.stream());

        try {
            executor.awaitTermination(1000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return queuedEventCarriers;
    }

    public List<EventCarrier> getEventQueueSnapshot() {
        return getEventCarriers(executor.getQueue().stream());

    }

    private List<EventCarrier> getEventCarriers(Stream<Runnable> runnables) {
        return runnables
                .map(r -> (EventPublishingRunnableFuture) r)
                .map(EventPublishingRunnableFuture::getWrappedEventCarrier)
                .toList();
    }
}
