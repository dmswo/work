package spring.work.event.producer.outbox.service;

import spring.work.event.producer.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutboxLifecycleService {
    List<OutboxEvent> makeProcessing();
    void increaseRetry(Long seq, String errorMessage);
    void makeDeadLetter(Long seq);
    void remove(Long seq);
}
