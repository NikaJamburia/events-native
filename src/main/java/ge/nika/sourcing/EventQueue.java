package ge.nika.sourcing;

import ge.nika.event.EventCarrier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventQueue {

    private int maxSize;
    private final Queue<EventCarrier> queue = new LinkedList<>();

    private final Lock lock = new ReentrantLock(true);
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public EventQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(EventCarrier eventCarrier) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() >= maxSize) {
                notFull.await();
            }
            queue.add(eventCarrier);
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public EventCarrier take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == 0) {
                notEmpty.await();
            }
            EventCarrier eventCarrier = queue.remove();
            notFull.signalAll();
            return eventCarrier;
        } finally {
            lock.unlock();
        }
    }

    public List<EventCarrier> getQueueSnapshot() {
        try {
            lock.lock();
            return queue.stream().map(EventCarrier::copy).toList();
        } finally {
            lock.unlock();
        }
    }
}


