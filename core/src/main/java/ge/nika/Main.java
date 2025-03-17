package ge.nika;

import ge.nika.api.model.EventPublisher;
import ge.nika.handler.EventHandlersRunner;
import ge.nika.sourcing.DefaultEventPublisher;
import ge.nika.testimpl.TestEvent;
import ge.nika.testimpl.TestEventHandler;

import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        EventHandlersRunner eventHandlersRunner = new EventHandlersRunner(List.of(new TestEventHandler()));

        EventPublisher publisher = new DefaultEventPublisher(eventHandlersRunner);

        publisher.publish(new TestEvent("bee", 1L));
        publisher.publish(new TestEvent("bee", 1L));
        publisher.publish(new TestEvent("bee", 1L));

        Thread.sleep(1000);
        publisher.shutdown();
    }

}
