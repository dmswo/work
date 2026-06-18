package spring.work.event.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import spring.work.event.common.EventFailStatus;
import spring.work.event.common.EventType;
import spring.work.event.consumer.fail.entity.EventFail;
import spring.work.event.consumer.fail.repository.EventFailRepository;
import spring.work.event.consumer.fail.service.impl.EventFailServiceImpl;
import spring.work.event.consumer.fail.service.retry.EventRetryHandler;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.kafka.dto.NotificationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventFailServiceImplTest {

    @Mock private EventFailRepository eventFailRepository;
    @Mock private ObjectMapper objectMapper;
    @Mock private EventRetryHandler handler;

    @InjectMocks
    private EventFailServiceImpl eventFailService;

    private Map<EventType, EventRetryHandler> handlerMap;

    @BeforeEach
    void setUp() {
        handlerMap = new HashMap<>();
        handlerMap.put(EventType.MAIL, handler);
        ReflectionTestUtils.setField(eventFailService, "handlerMap", handlerMap);
    }

    @Test
    @DisplayName("이벤트 재시도시 존재하지 않는 이벤트면 예외가 발생한다")
    void throw_exception_when_retryFailEvent_event_not_found() {
        // Given
        Long eventFailId = 1L;

        given(eventFailRepository.findById(eventFailId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventFailService.retryFailEvent(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.EVENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이벤트 재시도시 존재하지 않는 이벤트 타입이면 예외가 발생한다")
    void throw_exception_when_retryFailEvent_eventType_not_found() {
        // Given
        Long eventFailId = 1L;
        EventFail event = EventFail.builder()
                .eventType(EventType.MAIL)
                .build();

        given(eventFailRepository.findById(eventFailId)).willReturn(Optional.of(event));

        handlerMap.remove(EventType.MAIL);

        // When & Then
        assertThatThrownBy(() -> eventFailService.retryFailEvent(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.UNSUPPORTED_EVENT_TYPE.getMessage());
    }

    @Test
    @DisplayName("실패 이벤트 재시도 성공")
    void retryFailEvent_success() {
        // Given
        Long eventFailId = 1L;
        EventFail event = EventFail.builder()
                .eventType(EventType.MAIL)
                .status(EventFailStatus.FAILED)
                .payload("payload")
                .build();

        given(eventFailRepository.findById(eventFailId)).willReturn(Optional.of(event));

        // When
        eventFailService.retryFailEvent(eventFailId);

        // Then
        assertThat(event.getStatus()).isEqualTo(EventFailStatus.RETRY_SUCCESS);
        then(handler).should().retry("payload");
    }

    @Test
    @DisplayName("이벤트 직렬화 실패 시 예외 발생")
    void saveEventFail_serializationFail_throwsException() throws JsonProcessingException {
        // Given
        NotificationEvent event = NotificationEvent
                .builder()
                .build();

        given(objectMapper.writeValueAsString(any())).willThrow(new JsonProcessingException("직렬화 실패") {});

        // When & Then
        assertThatThrownBy(() -> eventFailService.saveEventFail(EventType.NOTIFICATION, "notification-topic", event, "에러"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to save EventFail");
    }

    @Test
    @DisplayName("실패이벤트 저장 성공")
    void saveEventFail_success() throws JsonProcessingException {
        // Given
        NotificationEvent event = NotificationEvent
                .builder()
                .build();

        given(objectMapper.writeValueAsString(any())).willReturn("{\"test\":\"value\"}");

        // When
        eventFailService.saveEventFail(EventType.NOTIFICATION, "notification-topic", event, "에러");

        // Then
        ArgumentCaptor<EventFail> captor = ArgumentCaptor.forClass(EventFail.class);
        then(eventFailRepository).should().save(captor.capture());
        EventFail savedEventFail = captor.getValue();

        assertThat(savedEventFail.getEventType()).isEqualTo(EventType.NOTIFICATION);
        assertThat(savedEventFail.getTopic()).isEqualTo("notification-topic");
        assertThat(savedEventFail.getErrorMessage()).isEqualTo("에러");
        assertThat(savedEventFail.getPayload()).isEqualTo("{\"test\":\"value\"}");
        assertThat(savedEventFail.getStatus()).isEqualTo(EventFailStatus.FAILED);
    }

}