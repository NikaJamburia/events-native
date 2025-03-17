package ge.nika.testimpl;

import ge.nika.api.AbstractEventHandler;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Event handler that just blocks current thread when haldle() is called.
 * Thread gets awakened when stopBlocking is called. Only for testing purposes
 */
public class BlockingEventHandler extends AbstractEventHandler<TestEvent> {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition shouldBlock = lock.newCondition();

    @Override
    protected Class<TestEvent> handledEventClass() {
        return TestEvent.class;
    }

    @Override
    protected void handle(TestEvent event) {
        lock.lock();
        try {
            shouldBlock.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(this + " Finished Blocking");
        }
    }

    public void stopBlocking() {
        lock.lock();
        try {
            shouldBlock.signal();
        } finally {
            lock.unlock();
        }
    }
}
