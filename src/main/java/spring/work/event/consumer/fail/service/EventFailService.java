package spring.work.event.consumer.fail.service;

import spring.work.event.common.EventType;

public interface EventFailService {
    void retryFailEvent(Long eventFailId);
    void saveEventFail(EventType eventType, String topic, Object event, String errorMessage);
}
