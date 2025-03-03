package ge.nika;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "native-events.spring.properties")
public record NativeEventsConfigurationProperties(
    Integer eventQueueSize,
    EventQueueSnapshotPersistenceStrategy eventQueueSnapshotPersistenceStrategy
) { }
