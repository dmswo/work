package spring.work.event.fail.service.retry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.event.constant.EventType;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.kafka.producer.EventProducer;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailRetryHandler implements EventRetryHandler {

    private final ObjectMapper objectMapper;
    private final EventProducer eventProducer;

    @Override
    public EventType getEventType() {
        return EventType.MAIL;
    }

    @Override
    public void retry(String payload) {
        try {
            MailEvent event = objectMapper.readValue(payload, MailEvent.class);

            eventProducer.send(event);

            log.info("Mail retry published. to={}", event.getToEmail());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("메일 이벤트 역직렬화 실패", e);
        }
    }
}
