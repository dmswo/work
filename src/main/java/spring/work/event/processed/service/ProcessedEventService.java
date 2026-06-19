package spring.work.event.processed.service;

import spring.work.event.common.EventType;

public interface ProcessedEventService {
    boolean exists(String eventId);
    void save(String eventId, EventType eventType);
}
