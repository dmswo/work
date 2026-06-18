package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import spring.work.event.common.EventType;
import spring.work.event.consumer.fail.service.FailEventService;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.utils.EmailSender;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailSender emailSender;
    private final FailEventService failEventService;

    @KafkaListener(topics = "mail-topic")
    public void sendMail(MailEvent event) {
        log.info("Kafka Consumer sendMail received: {}", event);
        emailSender.sendEmail(event);
    }

    @KafkaListener(topics = "mail-topic.DLT")
    public void failSendMail(MailEvent event, @Headers MessageHeaders headers) {
        log.info("Kafka Consumer failSendMail received: {}", event);

        String originalTopic = getHeaderAsString(headers, "kafka_dlt-original-topic");
        String errorMessage = extractRootMessage(getHeaderAsString(headers, "kafka_dlt-exception-message"));

        failEventService.saveEventFail(EventType.MAIL, originalTopic, event, errorMessage);
    }

    private String extractRootMessage(String message) {
        if (message == null) {
            return null;
        }

        int idx = message.lastIndexOf("; ");
        if (idx != -1 && idx + 2 < message.length()) {
            return message.substring(idx + 2);
        }

        return message;
    }

    private String getHeaderAsString(MessageHeaders headers, String key) {
        Object value = headers.get(key);

        if (value == null) {
            return null;
        }

        if (value instanceof byte[] bytes) {
            return new String(bytes, StandardCharsets.UTF_8);
        }

        return value.toString();
    }
}