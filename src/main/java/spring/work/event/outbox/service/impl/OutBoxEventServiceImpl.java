package spring.work.event.outbox.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.event.constant.EventType;
import spring.work.event.constant.OutBoxStatus;
import spring.work.event.outbox.entity.OutboxEvent;
import spring.work.event.outbox.service.OutBoxEventService;
import spring.work.event.outbox.service.OutboxLifecycleService;
import spring.work.global.kafka.dto.Event;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.kafka.dto.NotificationEvent;
import spring.work.global.kafka.producer.EventProducer;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutBoxEventServiceImpl implements OutBoxEventService {

    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;
    private final OutboxLifecycleService outboxLifecycleService;

    @Override
    public OutboxEvent createOutbox(EventType eventType, Object event) {
        try {
            return OutboxEvent.builder()
                    .eventType(eventType)
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutBoxStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Outbox payload 직렬화 실패", e);
        }
    }

    @Override
    public void publishPendingEvents() {
        List<OutboxEvent> pendingList = outboxLifecycleService.makeProcessing();

        for (OutboxEvent outboxEvent : pendingList) {
            try {
                Event event = switch (outboxEvent.getEventType()) {
                    case MAIL ->
                            objectMapper.readValue(outboxEvent.getPayload(), MailEvent.class);

                    case NOTIFICATION ->
                            objectMapper.readValue(outboxEvent.getPayload(), NotificationEvent.class);
                };

                // Kafka 발행(비동기)
                eventProducer.send(event);

            } catch (Exception e) {
                outboxLifecycleService.increaseRetry(outboxEvent.getSeq());
                log.error("Outbox 발행 실패. seq={}", outboxEvent.getSeq(), e);
            }
        }
    }
}