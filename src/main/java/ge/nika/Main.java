package ge.nika;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        EventQueue eventQueue = new EventQueue(10);
        EventPublisher publisher = new EventPublisher(eventQueue);
        EventConsumer consumer = new EventConsumer(eventQueue);

        consumer.start();

        Thread.sleep(1000);
        publisher.publish(new Event("SomeEvent", "{ someEvent }"));

        Thread.sleep(3000);
        publisher.publish(new Event("SomeEvent", "{ someEvent 2 }"));

        Thread.sleep(1000);
        publisher.publish(new Event("SomeEvent", "{ someEvent 3 }"));

    }

}
