package spring.work.event.outbox.service;

import spring.work.event.common.EventType;
import spring.work.event.outbox.entity.OutboxEvent;

public interface OutBoxEventService {
    OutboxEvent createOutbox(EventType eventType, Object event);
    void publishPendingEvents();
}
