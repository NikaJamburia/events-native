package ge.nika.sourcing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultEventPublisherTest {

    private final EventQueue eventQueue = new EventQueue(3);
    private final DefaultEventPublisher subject = new DefaultEventPublisher(eventQueue);

    @Test
    void addsToEventQueueNonBlocking() throws InterruptedException {
        subject.publish(TestUtils.randomEvent());
        subject.publish(TestUtils.randomEvent());

        Thread.sleep(100);

        Assertions.assertEquals(2, eventQueue.getQueueSnapshot().size());

        subject.publish(TestUtils.randomEvent());
        Assertions.assertEquals(2, eventQueue.getQueueSnapshot().size());

        eventQueue.take();
        Thread.sleep(100);
        Assertions.assertEquals(2, eventQueue.getQueueSnapshot().size());
    }
}
