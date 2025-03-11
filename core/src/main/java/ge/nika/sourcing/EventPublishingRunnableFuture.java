package ge.nika.sourcing;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class EventPublishingRunnableFuture<T> extends FutureTask<T> {

    public EventPublishingRunnable getWrappedRunnable() {
        return wrappedRunnable;
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
