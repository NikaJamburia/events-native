package ge.nika;

import ge.nika.api.model.Event;
import ge.nika.api.model.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NativeEvents {

    private static EventPublisher eventPublisher;

    @Autowired
    public NativeEvents(EventPublisher eventPublisher) {
        NativeEvents.eventPublisher = eventPublisher;
    }

    public static void publish(Event event) {
        eventPublisher.publish(event);
    }
}
