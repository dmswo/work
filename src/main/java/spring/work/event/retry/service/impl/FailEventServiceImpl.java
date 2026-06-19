package spring.work.event.retry.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.common.EventFailStatus;
import spring.work.event.common.EventType;
import spring.work.event.retry.entity.FailEvent;
import spring.work.event.retry.repository.FailEventRepository;
import spring.work.event.retry.service.FailEventService;
import spring.work.event.retry.service.retry.EventRetryHandler;
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
public class FailEventServiceImpl implements FailEventService {

    private final FailEventRepository failEventRepository;
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
    public void retryFailEvent(Long failEventId) {
        FailEvent failEvent = failEventRepository.findById(failEventId).orElseThrow(() -> new BusinessException(EVENT_NOT_FOUND));

        EventRetryHandler handler = handlerMap.get(failEvent.getEventType());

        if (handler == null) {
            throw new BusinessException(UNSUPPORTED_EVENT_TYPE);
        }

        handler.retry(failEvent.getPayload());

        failEvent.changeStatus(EventFailStatus.RETRY_SUCCESS);

        log.info("Retry success. failEventId={}, eventType={}",
                failEventId, failEvent.getEventType());
    }

    @Transactional
    @Override
    public void saveEventFail(EventType eventType, String topic, Object event, String errorMessage) {
        try {
            String payload = objectMapper.writeValueAsString(event);

            FailEvent failEvent = FailEvent.builder()
                    .eventType(eventType)
                    .topic(topic)
                    .payload(payload)
                    .errorMessage(errorMessage)
                    .status(EventFailStatus.FAILED)
                    .build();

            failEventRepository.save(failEvent);

            log.info("Failed event saved. type={}, topic={}", eventType, topic);

        } catch (Exception e) {
            log.error("Failed to save EventFail. type={}, topic={}", eventType, topic, e);

            // 상황에 따라 RuntimeException을 던지거나,
            // 로그만 남기고 종료할지 선택
            throw new RuntimeException("Failed to save EventFail", e);
        }
    }
}
