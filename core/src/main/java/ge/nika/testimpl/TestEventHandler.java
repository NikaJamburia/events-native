package ge.nika.testimpl;

import ge.nika.api.AbstractEventHandler;

import java.util.ArrayList;
import java.util.List;

public class TestEventHandler extends AbstractEventHandler<TestEvent> {

    private final List<TestEvent> executedEvents = new ArrayList<>();

    @Override
    protected void handle(TestEvent event) {
        System.out.println("Handling " + event);
        executedEvents.add(event);
    }

    @Override
    protected Class<TestEvent> handledEventClass() {
        return TestEvent.class;
    }

    public List<TestEvent> getExecutedEvents() {
        return executedEvents;
    }
}