package ge.nika.api;

import ge.nika.event.EventCarrier;
import ge.nika.Utils;
import ge.nika.handler.EventHandler;

public abstract class AbstractEventHandler<T extends Event> implements EventHandler {
    @Override
    public void accept(EventCarrier eventCarrier) {
        T eventData = Utils.fromJson(eventCarrier.jsonData(), handledEventClassName());
        handle(eventData);
    }

    @Override
    public String eventClassName() {
        return handledEventClassName().getName();
    }

    protected abstract Class<T> handledEventClassName();
    protected abstract void handle(T eventData);
}
