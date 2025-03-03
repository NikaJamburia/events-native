package ge.nika.testimpl;

import ge.nika.api.model.Event;

public record TestEvent(String stringField, Long longField) implements Event {
}
