package ge.nika.handler;

import ge.nika.testimpl.TestEventHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ge.nika.sourcing.TestUtils.randomEventCarrier;

public class EventHandlersRunnerTest {

    @Test
    void groupsGivenHandlersByEventClassAndRunsThem() {
        TestEventHandler testHandler = new TestEventHandler();
        var subject = new EventHandlersRunner(
                List.of(
                        testHandler
                )
        );

        subject.runHandlers(randomEventCarrier());
        subject.runHandlers(randomEventCarrier());
        subject.runHandlers(randomEventCarrier());

        Assertions.assertEquals(3, testHandler.getExecutedEvents().size());
    }
}
