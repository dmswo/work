package spring.work.event.outbox.service;

import spring.work.event.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutboxLifecycleService {
    List<OutboxEvent> makeProcessing();
    void increaseRetry(Long seq);
    void makeDeadLetter(Long seq);
}
