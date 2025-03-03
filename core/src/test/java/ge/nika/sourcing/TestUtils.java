package ge.nika.sourcing;

import ge.nika.api.model.EventCarrier;
import ge.nika.testimpl.TestEvent;

import java.util.Random;
import java.util.UUID;

public class TestUtils {
    public static EventCarrier randomEventCarrier() {
        return EventCarrier.of(randomEvent());
    }

    public static TestEvent randomEvent() {
        return new TestEvent(UUID.randomUUID().toString(), new Random().nextLong());
    }
}
