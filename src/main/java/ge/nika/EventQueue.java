package ge.nika;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventQueue {

    private int maxSize;
    private final Queue<Event> queue = new LinkedList<>();

    private final Lock lock = new ReentrantLock(true);
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public EventQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(Event event) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() >= maxSize) {
                notFull.await();
            }
            queue.add(event);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Event take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == 0) {
                notEmpty.await();
            }
            Event event = queue.remove();
            notFull.signalAll();
            return event;
        } finally {
            lock.unlock();
        }
    }

}


