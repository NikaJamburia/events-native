package ge.nika.testimpl;

import ge.nika.api.AbstractEventHandler;

public class TestEventHandler extends AbstractEventHandler<TestEvent> {

    @Override
    protected void handle(TestEvent event) {
        System.out.println("Handling " + event);
    }

    @Override
    protected Class<TestEvent> handledEventClass() {
        return TestEvent.class;
    }
}