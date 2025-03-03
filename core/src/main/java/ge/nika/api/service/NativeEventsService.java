package ge.nika.api.service;

import ge.nika.api.model.EventPublisher;
import ge.nika.api.persistence.EventQueueSnapshot;
import ge.nika.api.persistence.EventQueueSnapshotRepository;
import ge.nika.sourcing.EventConsumer;
import ge.nika.sourcing.EventQueue;

import java.time.Instant;
import java.util.UUID;

public class NativeEventsService {

    private final EventConsumer eventConsumer;
    private final EventPublisher eventPublisher;
    private final EventQueue eventQueue;
    private final EventQueueSnapshotRepository eventQueueSnapshotRepository;

    private volatile Boolean wasShutdown = false;

    public NativeEventsService(
        EventConsumer eventConsumer,
        EventPublisher eventPublisher,
        EventQueue eventQueue,
        EventQueueSnapshotRepository eventQueueSnapshotRepository
    ) {
        this.eventConsumer = eventConsumer;
        this.eventPublisher = eventPublisher;
        this.eventQueue = eventQueue;
        this.eventQueueSnapshotRepository = eventQueueSnapshotRepository;
    }

    void startConsumingEvents() {
        if (wasShutdown) {
            throw new IllegalStateException("NativeEventsService is non restartable!");
        }
        var latestSnapshot = eventQueueSnapshotRepository.getLatestSnapshot();
        latestSnapshot.events().forEach(eventPublisher::publish);
        eventConsumer.start();
    }

    void shutdown(Instant timestamp) {
        if (wasShutdown) {
            return;
        }

        eventPublisher.shutDown();
        eventConsumer.shutdown();

        eventQueueSnapshotRepository.save(
            new EventQueueSnapshot(UUID.randomUUID(), timestamp, eventQueue.getQueueSnapshot())
        );

        wasShutdown = true;
    }
}
