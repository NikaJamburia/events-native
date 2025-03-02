package ge.nika.handler;

import ge.nika.event.EventCarrier;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventHandlersRunner {
    private Map<String, List<EventHandler>> handlersMap;

    public EventHandlersRunner(List<EventHandler> handlers) {
        handlersMap = handlers.stream()
                .collect(Collectors.groupingBy(EventHandler::eventClassName));
    }

    public void runHandlers(EventCarrier eventCarrier) {
        handlersMap.get(eventCarrier.eventClassName())
                .forEach(eventHandler -> eventHandler.accept(eventCarrier));
    }
}
