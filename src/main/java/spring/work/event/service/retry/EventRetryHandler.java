package spring.work.event.service.retry;

import spring.work.event.constant.EventType;

public interface EventRetryHandler {
    EventType getEventType();
    void retry(String payload);
}
