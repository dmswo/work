package spring.work.event.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.event.constant.EventFailStatus;
import spring.work.event.constant.EventType;
import spring.work.event.entity.EventFail;
import spring.work.event.repository.EventFailRepository;
import spring.work.event.service.EventFailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventFailServiceImpl implements EventFailService {

    private final EventFailRepository eventFailRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void saveEventFail(EventType eventType, String topic, Object event, String errorMessage) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            EventFail eventFail = EventFail.builder()
                    .eventType(eventType)
                    .topic(topic)
                    .payload(payload)
                    .errorMessage(errorMessage)
                    .status(EventFailStatus.FAILED)
                    .build();

            eventFailRepository.save(eventFail);

            log.info("Failed event saved. type={}, topic={}", eventType, topic);

        } catch (Exception e) {
            log.error("Failed to save EventFail. type={}, topic={}", eventType, topic, e);

            // 상황에 따라 RuntimeException을 던지거나,
            // 로그만 남기고 종료할지 선택
            throw new RuntimeException("Failed to save EventFail", e);
        }
    }
}
