package spring.work.event.consumer.fail.service.retry;

import spring.work.event.common.EventType;

public interface EventRetryHandler {
    EventType getEventType();
    void retry(String payload);
}
