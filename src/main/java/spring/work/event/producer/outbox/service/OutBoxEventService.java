package spring.work.event.producer.outbox.service;

import spring.work.event.common.EventType;
import spring.work.event.producer.outbox.entity.OutboxEvent;

public interface OutBoxEventService {
    OutboxEvent createOutbox(EventType eventType, Object event);
    void publishPendingEvents();
}
