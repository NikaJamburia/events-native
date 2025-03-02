package ge.nika.testimpl;

import ge.nika.api.Event;

public record TestEvent(String stringField, Long longField) implements Event {
}
