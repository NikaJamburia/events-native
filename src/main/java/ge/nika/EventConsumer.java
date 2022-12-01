package ge.nika;

public class EventConsumer {
    private EventQueue eventQueue;
    private final Thread thread = new Thread(() -> {
        try {
            while (!Thread.interrupted()) {
                Event event = eventQueue.take();
                System.out.println("Consumed Event " + event.jsonData());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });

    public EventConsumer(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void start() {
        System.out.println("Starting event consumer");
        thread.start();
    }

    public void stop() {
        System.out.println("Stopping event consumer");
        thread.interrupt();
    }
}
