package spring.work.event.fail.service.retry;

import spring.work.event.fail.constant.EventType;

public interface EventRetryHandler {
    EventType getEventType();
    void retry(String payload);
}
