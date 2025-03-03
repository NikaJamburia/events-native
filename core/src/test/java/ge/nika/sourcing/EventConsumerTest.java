package ge.nika.sourcing;

import ge.nika.handler.EventHandlersRunner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static ge.nika.sourcing.TestUtils.randomEventCarrier;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventConsumerTest {

    @Test
    void managesAThreadForConsumingEventsFromQueueAndCallsEventHandlersRunner() throws InterruptedException {
        EventQueue eventQueue = new EventQueue(1);
        EventHandlersRunner eventHandlersRunner = Mockito.mock();
        EventConsumer subject = new EventConsumer(eventQueue, eventHandlersRunner);

        subject.start();

        eventQueue.add(randomEventCarrier());
        eventQueue.add(randomEventCarrier());
        eventQueue.add(randomEventCarrier());

        Thread.sleep(1000);

        // Events were consumed
        assertEquals(0, eventQueue.getQueueSnapshot().size());

        subject.shutdown();
        eventQueue.add(randomEventCarrier());
        Thread.sleep(1000);

        // No longer consumed after stopping
        assertEquals(1, eventQueue.getQueueSnapshot().size());

        Mockito.verify(eventHandlersRunner, Mockito.times(3))
                .runHandlers(Mockito.any());

    }
}
