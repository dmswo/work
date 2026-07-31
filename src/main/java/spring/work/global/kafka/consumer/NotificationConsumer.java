package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.common.EventType;
import spring.work.event.retry.service.FailEventService;
import spring.work.event.processed.service.ProcessedEventService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.kafka.dto.CommentEvent;
import spring.work.global.kafka.dto.PostLikeEvent;
import spring.work.global.utils.UtilService;
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
    private final FailEventService failEventService;
    private final ProcessedEventService processedEventService;
    private final UtilService utilService;

    private static final String NOTIFICATION_GROUP = "notification-consumer-group";

    @Transactional
    @KafkaListener(topics = "post-like-topic"
            , groupId = "notification-consumer-group"
            , concurrency = "3")
    public void sendNotificationPostLike(PostLikeEvent event) {
        log.info("Kafka Consumer sendNotificationPostLike received: {}", event);

        // 1. 이미 처리한 이벤트인지 확인
        if (processedEventService.exists(event.getEventId(), NOTIFICATION_GROUP)) {
            log.info("이미 처리된 이벤트입니다. eventId={}", event.getEventId());
            return;
        }

        // 2. 알림 발송
        Users receiver = userRepository.findById(event.getPostOwnerId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Users sender = userRepository.findById(event.getLikerId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Long targetId = event.getPostId();

        notificationService.sendNotification(receiver, sender, NotificationType.POST_LIKE, targetId);

        // 3. 성공한 경우에만 처리 완료 기록
        processedEventService.save(event.getEventId(), NOTIFICATION_GROUP, EventType.POST_LIKE);
    }

    @KafkaListener(topics = "post-like-topic.DLT", groupId = "notification-dlt-consumer-group")
    public void failSendNotificationPostLike(PostLikeEvent event, @Headers MessageHeaders headers) {
        log.info("Kafka Consumer failSendNotificationPostLike received: {}", event);

        String originalTopic = utilService.getHeaderAsString(headers, "kafka_dlt-original-topic");
        String errorMessage = utilService.extractRootMessage(utilService.getHeaderAsString(headers, "kafka_dlt-exception-message"));

        failEventService.saveEventFail(EventType.POST_LIKE, originalTopic, event, errorMessage);
    }

    @Transactional
    @KafkaListener(topics = "comment-topic"
            , groupId = "notification-consumer-group"
            , concurrency = "3")
    public void sendNotificationComment(CommentEvent event) {
        log.info("Kafka Consumer sendNotificationComment received: {}", event);

//        // 1. 이미 처리한 이벤트인지 확인
//        if (processedEventService.exists(event.getEventId(), NOTIFICATION_GROUP)) {
//            log.info("이미 처리된 이벤트입니다. eventId={}", event.getEventId());
//            return;
//        }
//
//        // 2. 댓글 알림 발송
//
//        // 3. 성공한 경우에만 처리 완료 기록
//        processedEventService.save(event.getEventId(), NOTIFICATION_GROUP, EventType.NOTIFICATION);
    }
}
