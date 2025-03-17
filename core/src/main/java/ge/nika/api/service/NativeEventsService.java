package ge.nika.api.service;

import ge.nika.api.model.EventPublisher;
import ge.nika.api.persistence.EventQueueSnapshot;
import ge.nika.api.persistence.EventQueueSnapshotRepository;

import java.time.Instant;
import java.util.UUID;

public class NativeEventsService {

    private final EventPublisher eventPublisher;
    private final EventQueueSnapshotRepository eventQueueSnapshotRepository;

    private volatile Boolean wasShutdown = false;

    public NativeEventsService(
        EventPublisher eventPublisher,
        EventQueueSnapshotRepository eventQueueSnapshotRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.eventQueueSnapshotRepository = eventQueueSnapshotRepository;
    }

    void startConsumingEvents() {
        if (wasShutdown) {
            throw new IllegalStateException("NativeEventsService is non restartable!");
        }
        var latestSnapshot = eventQueueSnapshotRepository.getLatestSnapshot();
        latestSnapshot.events().forEach(eventPublisher::publish);
    }

    void shutdown(Instant timestamp) {
        if (wasShutdown) {
            return;
        }

        var unpublishedEvents = eventPublisher.shutdown();

        eventQueueSnapshotRepository.save(
            new EventQueueSnapshot(UUID.randomUUID(), timestamp, unpublishedEvents)
        );

        wasShutdown = true;
    }
}
