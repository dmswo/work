package spring.work.event.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.work.event.constant.EventFailStatus;
import spring.work.event.constant.EventType;
import spring.work.event.entity.EventFail;
import spring.work.event.repository.EventFailRepository;
import spring.work.global.kafka.dto.NotificationEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventFailServiceImplTest {

    @Mock private EventFailRepository eventFailRepository;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks
    private EventFailServiceImpl eventFailService;

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