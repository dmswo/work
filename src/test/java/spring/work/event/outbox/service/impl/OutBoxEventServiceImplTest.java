package spring.work.event.outbox.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.SendResult;
import spring.work.event.common.EventType;
import spring.work.event.common.OutBoxStatus;
import spring.work.event.outbox.entity.OutboxEvent;
import spring.work.event.outbox.service.OutboxLifecycleService;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.kafka.producer.EventProducer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class OutBoxEventServiceImplTest {

    @Mock private EventProducer eventProducer;
    @Mock private ObjectMapper objectMapper;
    @Mock private OutboxLifecycleService outboxLifecycleService;

    @InjectMocks
    private OutBoxEventServiceImpl outBoxEventService;

    @Test
    @DisplayName("이벤트 발행 실패 시 increaseRetry를 호출한다")
    void increase_retry_when_publishPendingEvents_fail() throws JsonProcessingException {
        // Given
        String payload = "{\"userId\":\"dmswo\"}";
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .seq(1L)
                .eventType(EventType.MAIL)
                .status(OutBoxStatus.PENDING)
                .payload(payload)
                .build();

        MailEvent mailEvent = MailEvent.builder()
                .userId("dmswo")
                .build();

        CompletableFuture<SendResult<String, Object>> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Kafka publish failed"));

        given(outboxLifecycleService.makeProcessing()).willReturn(List.of(outboxEvent));
        given(objectMapper.readValue(anyString(), eq(MailEvent.class))).willReturn(mailEvent);
        given(eventProducer.send(any(MailEvent.class))).willReturn(failedFuture);

        // When
        outBoxEventService.publishPendingEvents();

        // Then
        then(outboxLifecycleService).should().increaseRetry(eq(outboxEvent.getSeq()), anyString());
    }

    @Test
    @DisplayName("이벤트 발행 성공 시 Outbox 상태값을 변경한다")
    void update_outbox_status_when_publishPendingEvents_success() throws JsonProcessingException {
        // Given
        String payload = "{\"userId\":\"dmswo\"}";
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .seq(1L)
                .eventType(EventType.MAIL)
                .status(OutBoxStatus.PENDING)
                .payload(payload)
                .build();

        MailEvent mailEvent = MailEvent.builder()
                .userId("dmswo")
                .build();

        SendResult<String, Object> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, Object>> future =
                CompletableFuture.completedFuture(sendResult);

        given(outboxLifecycleService.makeProcessing()).willReturn(List.of(outboxEvent));
        given(objectMapper.readValue(anyString(), eq(MailEvent.class))).willReturn(mailEvent);
        given(eventProducer.send(any(MailEvent.class))).willReturn(future);

        // When
        outBoxEventService.publishPendingEvents();

        // Then
        then(outboxLifecycleService).should().makeSuccess(outboxEvent.getSeq());
    }
}