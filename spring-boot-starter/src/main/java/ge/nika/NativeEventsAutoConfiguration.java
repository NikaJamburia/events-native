package ge.nika;

import ge.nika.api.model.EventHandler;
import ge.nika.api.model.EventPublisher;
import ge.nika.api.persistence.EventQueueSnapshotRepository;
import ge.nika.api.service.NativeEventsService;
import ge.nika.handler.EventHandlersRunner;
import ge.nika.persistence.NonSavingEventQueueSnapshotRepository;
import ge.nika.sourcing.DefaultEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(NativeEventsConfigurationProperties.class)
public class NativeEventsAutoConfiguration {

    private final NativeEventsConfigurationProperties properties;

    @Autowired
    public NativeEventsAutoConfiguration(NativeEventsConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public EventHandlersRunner eventHandlersRunner(List<EventHandler> eventHandlers) {
        return new EventHandlersRunner(eventHandlers);
    }

    @Bean
    public EventPublisher eventPublisher(EventHandlersRunner eventHandlersRunner) {
        return new DefaultEventPublisher(eventHandlersRunner);
    }

    @Bean
    public EventQueueSnapshotRepository eventQueueSnapshotRepository() {
        return switch (properties.eventQueueSnapshotPersistenceStrategy()) {
            case NO_PERSISTENCE -> new NonSavingEventQueueSnapshotRepository();
            case PERSIST_TO_FILE -> throw new IllegalArgumentException("Saving to file not supported yet");
        };
    }

    @Bean
    public NativeEventsService nativeEventsService(
        EventPublisher eventPublisher,
        EventQueueSnapshotRepository eventQueueSnapshotRepository
    ) {
        return new NativeEventsService( eventPublisher, eventQueueSnapshotRepository);
    }
}
