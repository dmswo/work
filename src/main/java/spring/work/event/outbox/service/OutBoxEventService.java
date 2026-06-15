package spring.work.event.outbox.service;

public interface OutBoxEventService {
    void publishPendingEvents();
}
