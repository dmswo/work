package spring.work.event.service;

import spring.work.event.constant.EventType;

public interface EventFailService {
    void retryFailEvent(Long eventFailId);
    void saveEventFail(EventType eventType, String topic, Object event, String errorMessage);
}
