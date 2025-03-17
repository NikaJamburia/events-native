package ge.nika.api.service;

import ge.nika.api.persistence.EventQueueSnapshot;
import ge.nika.api.persistence.EventQueueSnapshotRepository;
import ge.nika.handler.EventHandlersRunner;
import ge.nika.sourcing.DefaultEventPublisher;
import ge.nika.sourcing.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class NativeEventsServiceTest {

    @Test
    void startThrowsExceptionIfServiceWasAlreadyShutdown() {
        var subject = new NativeEventsService(
                Mockito.mock(),
                Mockito.mock()
        );

        subject.shutdown(Instant.now());

        Assertions.assertThrows(
            IllegalStateException.class,
            subject::startConsumingEvents,
            "NativeEventsService is non restartable!"
        );
    }

    @Test
    void publishesLatestSnapshotedEventsAndAndStartsConsumer() throws InterruptedException {
        var snapshotEvents = List.of(
                TestUtils.randomEventCarrier(),
                TestUtils.randomEventCarrier(),
                TestUtils.randomEventCarrier()
        );

        var eventQueueSnapshotRepository = Mockito.mock(EventQueueSnapshotRepository.class);
        var eventHandlersRunner = Mockito.mock(EventHandlersRunner.class);
        var subject = new NativeEventsService(
                new DefaultEventPublisher(eventHandlersRunner),
                eventQueueSnapshotRepository
        );

        Mockito.when(eventQueueSnapshotRepository.getLatestSnapshot()).thenReturn(
            new EventQueueSnapshot(
                UUID.randomUUID(),
                Instant.now(),
                snapshotEvents
            )
        );

        subject.startConsumingEvents();

        // Snapshotted events have been published and consumed
        Thread.sleep(100);

        Mockito.verify(eventHandlersRunner, Mockito.times(3))
                .runHandlers(Mockito.any());
    }

    @Test
    void stopsEventPublisherAndConsumerAndSavesEventQueueSnapshot() {
        var eventQueueSnapshotRepository = Mockito.mock(EventQueueSnapshotRepository.class);
        var eventPublisher = Mockito.mock(DefaultEventPublisher.class);

        var eventsInPublisher = List.of(
                TestUtils.randomEventCarrier(),
                TestUtils.randomEventCarrier(),
                TestUtils.randomEventCarrier()
        );

        var subject = new NativeEventsService(
                eventPublisher,
                eventQueueSnapshotRepository
        );

        ArgumentCaptor<EventQueueSnapshot> snapshotCaptor = ArgumentCaptor.forClass(EventQueueSnapshot.class);

        Mockito.when(eventPublisher.shutdown()).thenReturn(eventsInPublisher);

        subject.shutdown(Instant.now());

        Mockito.verify(eventPublisher).shutdown();
        Mockito.verify(eventQueueSnapshotRepository).save(snapshotCaptor.capture());

        var snapshotEvents = snapshotCaptor.getValue().events();
        Assertions.assertEquals(3, snapshotEvents.size());
        Assertions.assertTrue(snapshotEvents.containsAll(eventsInPublisher));
    }
}
