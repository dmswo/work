package spring.work.event.producer.outbox.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spring.work.event.producer.outbox.service.OutBoxEventService;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutBoxEventService outBoxEventService;

    @Scheduled(fixedDelay = 5000)
    public void publishOutbox() {
        outBoxEventService.publishPendingEvents();
    }
}