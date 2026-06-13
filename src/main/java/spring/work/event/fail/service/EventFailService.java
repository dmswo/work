package spring.work.event.fail.service;

import spring.work.event.fail.constant.EventType;

public interface EventFailService {
    void retryFailEvent(Long eventFailId);
    void saveEventFail(EventType eventType, String topic, Object event, String errorMessage);
}
