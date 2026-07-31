package spring.work.event.processed.service;

import spring.work.event.common.EventType;

public interface ProcessedEventService {
    boolean exists(String eventId, String consumerGroup);
    void save(String eventId, String consumerGroup, EventType eventType);
}
