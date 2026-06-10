package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.kafka.dto.NotificationEvent;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.service.NotificationService;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

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
    public void failSendNotification(NotificationEvent event) {

    }
}
