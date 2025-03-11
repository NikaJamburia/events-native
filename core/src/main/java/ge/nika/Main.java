package ge.nika;

import ge.nika.api.model.EventPublisher;
import ge.nika.handler.EventHandlersRunner;
import ge.nika.sourcing.EventConsumer;
import ge.nika.sourcing.DefaultEventPublisher;
import ge.nika.sourcing.EventQueue;
import ge.nika.testimpl.TestEvent;
import ge.nika.testimpl.TestEventHandler;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        EventQueue eventQueue = new EventQueue(10);
        EventPublisher publisher = new DefaultEventPublisher(eventQueue);
        EventHandlersRunner eventHandlersRunner = new EventHandlersRunner(List.of(new TestEventHandler()));
        EventConsumer consumer = new EventConsumer(eventQueue, eventHandlersRunner);

        publisher.publish(new TestEvent("bee", 1L));
        publisher.publish(new TestEvent("bee", 1L));
        publisher.publish(new TestEvent("bee", 1L));

        consumer.start();
        Thread.sleep(1000);
        publisher.shutdown();
        consumer.shutdown();
    }

}
