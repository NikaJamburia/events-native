package ge.nika;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventPublisher {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final EventQueue eventQueue;

    public EventPublisher(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void publish(Event ev) {
        executor.submit(() -> {
            try {
                eventQueue.add(ev);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
