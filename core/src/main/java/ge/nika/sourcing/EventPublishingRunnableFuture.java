package ge.nika.sourcing;

import ge.nika.api.model.EventCarrier;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class EventPublishingRunnableFuture<T> extends FutureTask<T> {

    public EventCarrier getWrappedEventCarrier() {
        assert wrappedRunnable != null;
        return wrappedRunnable.getEventCarrier();
    }

    private final EventPublishingRunnable wrappedRunnable;

    public EventPublishingRunnableFuture(Callable<T> callable) {
        super(callable);
        wrappedRunnable = null;
        throw new IllegalArgumentException("Only runnable wrapping supported!");
    }

    public EventPublishingRunnableFuture(EventPublishingRunnable runnable, T result) {
        super(runnable, result);
        this.wrappedRunnable = runnable;
    }


}
