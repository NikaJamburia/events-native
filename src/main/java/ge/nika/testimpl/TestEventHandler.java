package ge.nika.testimpl;

import ge.nika.api.AbstractEventHandler;

public class TestEventHandler extends AbstractEventHandler<TestEvent> {

    @Override
    protected void handle(TestEvent eventData) {
        System.out.println("Handling " + eventData);
    }

    @Override
    protected Class<TestEvent> handledEventClassName() {
        return TestEvent.class;
    }
}