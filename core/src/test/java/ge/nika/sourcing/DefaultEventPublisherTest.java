package ge.nika.sourcing;

import ge.nika.handler.EventHandlersRunner;
import ge.nika.testimpl.BlockingEventHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultEventPublisherTest {

    @Test
    void addsToEventQueueNonBlocking() throws InterruptedException {
        var blockingEventHandler = new BlockingEventHandler();
        var eventHandlersRunner = new EventHandlersRunner(List.of(
                blockingEventHandler
        ));
        var subject = new DefaultEventPublisher(eventHandlersRunner);

        subject.publish(TestUtils.randomEvent());
        subject.publish(TestUtils.randomEvent());
        Thread.sleep(100);

        // 1 event is being processed while others are enqueued
        assertEquals(1, subject.getEventQueueSnapshot().size());

        // Another event is added to the queue
        subject.publish(TestUtils.randomEvent());
        assertEquals(2, subject.getEventQueueSnapshot().size());

        // Event handler stops blocking so next event can be taken from queue and executed
        blockingEventHandler.stopBlocking();
        Thread.sleep(100);
        assertEquals(1, subject.getEventQueueSnapshot().size());

        // Event handler stops blocking so next event can be taken from queue and executed
        blockingEventHandler.stopBlocking();
        Thread.sleep(100);

        assertEquals(0, subject.getEventQueueSnapshot().size());

    }

    @Test
    void returnsListOfUnsubmittedEventsOnShutdown() throws InterruptedException {
        var blockingEventHandler = new BlockingEventHandler();
        var eventHandlersRunner = new EventHandlersRunner(List.of(
                blockingEventHandler
        ));
        var subject = new DefaultEventPublisher(eventHandlersRunner);

        var event1 = TestUtils.randomEventCarrier();
        var event2 = TestUtils.randomEventCarrier();
        var event3 = TestUtils.randomEventCarrier();
        var event4 = TestUtils.randomEventCarrier();

        subject.publish(event1);
        subject.publish(event2);
        subject.publish(event3);
        subject.publish(event4);


        // first event gets into event queue,
        // handler thread will get by BlockingEventHandler on second event as queue is full
        // shutdown will return event 2, 3 and 4 as they are waiting in executors queue

        Thread.sleep(100);
        var result = subject.shutdown();

        assertEquals(3, result.size());
        assertTrue(result.contains(event2));
        assertTrue(result.contains(event3));
        assertTrue(result.contains(event4));
    }
}
