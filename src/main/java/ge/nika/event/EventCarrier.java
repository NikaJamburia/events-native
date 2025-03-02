package ge.nika.event;

import ge.nika.Utils;
import ge.nika.api.Event;

public record EventCarrier(String eventClassName, String jsonData) {

    public static EventCarrier of(Event any) {
        return new EventCarrier(any.getClass().getName(), Utils.toJson(any));
    }

    public static EventCarrier copy(EventCarrier other) {
        return new EventCarrier(other.eventClassName(), other.jsonData());
    }
}
