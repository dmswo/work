package spring.work.event.outbox.service;

import spring.work.event.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutboxLifecycleService {
    List<OutboxEvent> makeProcessing();
    void increaseRetry(Long seq, String errorMessage);
    void makeSuccess(Long seq);
    void makeDeadLetter(Long seq);
    void remove(Long seq);
}
