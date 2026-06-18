package spring.work.event.consumer.fail.service;

import spring.work.event.common.EventType;

public interface FailEventService {
    void retryFailEvent(Long failEventId);
    void saveEventFail(EventType eventType, String topic, Object event, String errorMessage);
}
