package spring.work.event.outbox.service;

import spring.work.event.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutboxStatusService {
    List<OutboxEvent> makeProcessing();
    void makeSuccess(Long seq);
    void makeFailed(Long seq);
}
