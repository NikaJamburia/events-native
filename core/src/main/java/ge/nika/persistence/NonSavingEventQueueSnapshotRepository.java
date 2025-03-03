package ge.nika.persistence;

import ge.nika.api.persistence.EventQueueSnapshot;
import ge.nika.api.persistence.EventQueueSnapshotRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class NonSavingEventQueueSnapshotRepository implements EventQueueSnapshotRepository {

    private final UUID someUUID = UUID.randomUUID();
    @Override
    public EventQueueSnapshot getLatestSnapshot() {
        return new EventQueueSnapshot(someUUID, Instant.now(), List.of());
    }

    @Override
    public void save(EventQueueSnapshot eventQueueSnapshot) {
    }
}
