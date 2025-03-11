package ge.nika.sourcing;

import java.util.concurrent.*;

public class EventPublishingThreadExecutorService extends ThreadPoolExecutor {
    private EventPublishingThreadExecutorService(
        int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        BlockingQueue<Runnable> workQueue
    ) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public static EventPublishingThreadExecutorService singleThreaded() {
        return new EventPublishingThreadExecutorService(
            1, 1, 0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        if (runnable instanceof EventPublishingRunnable) {
            return new EventPublishingRunnableFuture<>((EventPublishingRunnable) runnable, value);
        } else {
            throw new IllegalArgumentException("EventPublishingThreadExecutorService supports only EventPublishingRunnable task creation!");
        }
    }
}

