package ge.nika.sourcing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultEventPublisherTest {

    @Test
    void addsToEventQueueNonBlocking() throws InterruptedException {
        EventQueue eventQueue = new EventQueue(3);
        DefaultEventPublisher subject = new DefaultEventPublisher(eventQueue);

        subject.publish(TestUtils.randomEvent());
        subject.publish(TestUtils.randomEvent());

        Thread.sleep(100);

        assertEquals(2, eventQueue.getQueueSnapshot().size());

        subject.publish(TestUtils.randomEvent());
        assertEquals(2, eventQueue.getQueueSnapshot().size());

        eventQueue.take();
        Thread.sleep(100);
        assertEquals(2, eventQueue.getQueueSnapshot().size());
    }

    @Test
    void returnsListOfUnsubmittedEventsOnShutdown() throws InterruptedException {
        EventQueue eventQueue = new EventQueue(1);
        DefaultEventPublisher subject = new DefaultEventPublisher(eventQueue);

        var event1 = TestUtils.randomEventCarrier();
        var event2 = TestUtils.randomEventCarrier();
        var event3 = TestUtils.randomEventCarrier();
        var event4 = TestUtils.randomEventCarrier();

        subject.publish(event1);
        subject.publish(event2);
        subject.publish(event3);
        subject.publish(event4);


        // first event gets into event queue,
        // publishing thread will get blocked on second event as queue is full
        // shutdown will return event 3 and 4 as they are waiting in executors queue

        Thread.sleep(100);
        var result = subject.shutDown();

        assertEquals(2, result.size());
        assertTrue(result.contains(event3));
        assertTrue(result.contains(event4));
    }
}
