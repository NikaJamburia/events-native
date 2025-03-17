package ge.nika.testimpl;

import ge.nika.sourcing.TestUtils;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.State.WAITING;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockingEventHandlerTest {

    @Test
    void threadCallingHandleGetsBlockedUntillStopBlockingIsCalled() throws InterruptedException {
        var subject = new BlockingEventHandler();
        var workerThread = new Thread(() ->
                subject.handle(TestUtils.randomEvent())
        );

        workerThread.start();
        Thread.sleep(1000);

        assertEquals(WAITING, workerThread.getState());

        subject.stopBlocking();
        workerThread.join();
        assertEquals(Thread.State.TERMINATED, workerThread.getState());
    }
}
