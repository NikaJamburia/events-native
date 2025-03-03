package ge.nika.api.model;

import ge.nika.Utils;

public record EventCarrier(String eventClassName, String jsonData) {

    public static EventCarrier of(Event any) {
        return new EventCarrier(any.getClass().getName(), Utils.toJson(any));
    }

    public static EventCarrier copy(EventCarrier other) {
        return new EventCarrier(other.eventClassName(), other.jsonData());
    }
}
