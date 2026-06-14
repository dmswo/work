package spring.work.event.fail.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.constant.EventFailStatus;
import spring.work.event.constant.EventType;
import spring.work.event.fail.entity.EventFail;
import spring.work.event.fail.repository.EventFailRepository;
import spring.work.event.fail.service.EventFailService;
import spring.work.event.fail.service.retry.EventRetryHandler;
import spring.work.global.exception.BusinessException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static spring.work.global.constant.ExceptionCode.EVENT_NOT_FOUND;
import static spring.work.global.constant.ExceptionCode.UNSUPPORTED_EVENT_TYPE;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventFailServiceImpl implements EventFailService {

    private final EventFailRepository eventFailRepository;
    private final ObjectMapper objectMapper;
    private final List<EventRetryHandler> handlers;

    private Map<EventType, EventRetryHandler> handlerMap;

    @PostConstruct
    public void init() {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        EventRetryHandler::getEventType,
                        Function.identity()
                ));
    }

    @Transactional
    @Override
    public void retryFailEvent(Long eventFailId) {
        EventFail eventFail = eventFailRepository.findById(eventFailId).orElseThrow(() -> new BusinessException(EVENT_NOT_FOUND));

        EventRetryHandler handler = handlerMap.get(eventFail.getEventType());

        if (handler == null) {
            throw new BusinessException(UNSUPPORTED_EVENT_TYPE);
        }

        handler.retry(eventFail.getPayload());

        eventFail.changeStatus(EventFailStatus.RETRY_SUCCESS);

        log.info("Retry success. eventFailId={}, eventType={}",
                eventFailId, eventFail.getEventType());
    }

    @Transactional
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
