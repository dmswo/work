package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import spring.work.event.common.EventType;
import spring.work.event.consumer.fail.service.EventFailService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.kafka.dto.NotificationEvent;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.service.NotificationService;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final EventFailService eventFailService;

    @KafkaListener(topics = "notification-topic")
    public void sendNotification(NotificationEvent event) {
        log.info("Kafka Consumer sendNotification received: {}", event);

        Users receiver = userRepository.findById(event.getReceiverId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Users sender = userRepository.findById(event.getSenderId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        NotificationType type = event.getType();
        Long targetId = event.getTargetId();

        notificationService.sendNotification(receiver, sender, type, targetId);
    }

    @KafkaListener(topics = "notification-topic.DLT")
    public void failSendNotification(NotificationEvent event, @Headers MessageHeaders headers) {
        log.info("Kafka Consumer failSendNotification received: {}", event);

        String originalTopic = getHeaderAsString(headers, "kafka_dlt-original-topic");
        String errorMessage = extractRootMessage(getHeaderAsString(headers, "kafka_dlt-exception-message"));

        eventFailService.saveEventFail(EventType.NOTIFICATION, originalTopic, event, errorMessage);
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
