package ge.nika.api.persistence;

public interface EventQueueSnapshotRepository {
    EventQueueSnapshot getLatestSnapshot();
    void save(EventQueueSnapshot eventQueueSnapshot);
}
