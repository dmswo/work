package spring.work.event.processed.service;

import spring.work.event.common.EventType;

public interface ProcessedEventService {
    boolean exists(Long eventId);
    void save(Long eventId, EventType eventType);
}
