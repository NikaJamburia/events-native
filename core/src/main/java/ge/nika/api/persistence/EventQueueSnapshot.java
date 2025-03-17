package ge.nika.api.persistence;

import ge.nika.api.model.EventCarrier;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EventQueueSnapshot(
        UUID id,
        Instant timestamp,
        List<EventCarrier> events
) { }
