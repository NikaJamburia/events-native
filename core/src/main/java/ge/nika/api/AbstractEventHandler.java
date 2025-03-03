package ge.nika.api;

import ge.nika.api.model.Event;
import ge.nika.api.model.EventHandler;
import ge.nika.api.model.EventCarrier;
import ge.nika.Utils;

public abstract class AbstractEventHandler<T extends Event> implements EventHandler {
    @Override
    public void accept(EventCarrier eventCarrier) {
        T eventData = Utils.fromJson(eventCarrier.jsonData(), handledEventClass());
        handle(eventData);
    }

    @Override
    public String eventClassName() {
        return handledEventClass().getName();
    }

    protected abstract Class<T> handledEventClass();
    protected abstract void handle(T event);
}
