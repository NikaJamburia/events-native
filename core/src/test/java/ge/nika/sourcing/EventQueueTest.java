package ge.nika.sourcing;

import ge.nika.api.model.EventCarrier;
import ge.nika.testimpl.TestEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static ge.nika.sourcing.TestUtils.randomEventCarrier;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventQueueTest {

    @Test
    void publisherWaitsForSpaceAndConsumerWaitsForEvents() throws InterruptedException {
        final EventQueue subject = new EventQueue(1);

        var event1 = new TestEvent("Nika", 1L);
        var event2 = new TestEvent("Nika", 2L);

        // Add event to queue
        subject.add(EventCarrier.of(event1));
        assertEquals(1, subject.getQueueSnapshot().size());

        // Try to add event in other thread
        Thread secondAdd = new Thread(() -> {
            try {
                subject.add(EventCarrier.of(event2));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        secondAdd.start();

        // Size is still 1 as previous event was not consumed and queue is full, the above thread is waiting
        assertEquals(1, subject.getQueueSnapshot().size());

        // New thread consumes event
        var consumed = subject.take();
        assertEquals(consumed, EventCarrier.of(event1));
        secondAdd.join();

        // Size is again 1 as blocked thread inserts new event
        assertEquals(1, subject.getQueueSnapshot().size());

        // Another thread consumes event
        consumed = subject.take();
        assertEquals(consumed, EventCarrier.of(event2));
        // Now size is zero
        assertEquals(0, subject.getQueueSnapshot().size());

        // A thread attempts to take when the queue is empty
        Thread takeAttemptWhileEmpty = new Thread(() -> {
            try {
                subject.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        takeAttemptWhileEmpty.start();

        // Add here
        subject.add(EventCarrier.of(event1));

        // Wait for takeAttemptWhileEmpty
        takeAttemptWhileEmpty.join();

        // Size is still zero as event should have been taken by now
        assertEquals(0, subject.getQueueSnapshot().size());
    }

    @Test
    void isFIFO() throws InterruptedException {
        final EventQueue subject = new EventQueue(2);

        var event1 = EventCarrier.of(new TestEvent("Nika", 1L));
        var event2 = EventCarrier.of(new TestEvent("Nika", 2L));

        subject.add(event1);
        subject.add(event2);

        assertEquals(
                event1,
                subject.take()
        );

        assertEquals(
                event2,
                subject.take()
        );
    }

    @Test
    void readsAndWritesAreSynchronized() throws InterruptedException {
        final EventQueue subject = new EventQueue(10);


        // Given 100 random events
        List<EventCarrier> eventsToPublish = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            eventsToPublish.add(randomEventCarrier());
        }
        assertEquals(100, eventsToPublish.size());

        // There are reading and writing threads running in parallel, all events should be consumed after they are done
        Thread publisher = new Thread(() -> {
            eventsToPublish.forEach(e -> {
                try {
                    subject.add(e);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });

        Thread consumer = new Thread(() -> {
            List<EventCarrier> consumedEvents = new ArrayList<>();

            while (consumedEvents.size() != 100) {
                try {
                    consumedEvents.add(subject.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            assertEquals(100, consumedEvents.size());
            // Every event in eventsToPublish should be consumed
            eventsToPublish.forEach(eventToPublish -> {
                var matched = consumedEvents.stream()
                        .filter(consumed -> consumed == eventToPublish)
                        .toList();
                assertEquals(1, matched.size());
            });
        });

        publisher.join();
        consumer.join();

        assertEquals(0, subject.getQueueSnapshot().size());
    }
}
