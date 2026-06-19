package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.common.EventType;
import spring.work.event.consumer.fail.service.FailEventService;
import spring.work.event.consumer.processed.service.ProcessedEventService;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.utils.EmailSender;
import spring.work.global.utils.UtilService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailSender emailSender;
    private final FailEventService failEventService;
    private final ProcessedEventService processedEventService;
    private final UtilService utilService;

    @Transactional
    @KafkaListener(topics = "mail-topic")
    public void sendMail(MailEvent event) {
        log.info("Kafka Consumer sendMail received: {}", event);

        // 1. 이미 처리한 이벤트인지 확인
        if (processedEventService.exists(event.getEventId())) {
            log.info("이미 처리된 이벤트입니다. eventId={}", event.getEventId());
            return;
        }

        // 2. 메일 발송
        emailSender.sendEmail(event);

        // 3. 성공한 경우에만 처리 완료 기록
        processedEventService.save(event.getEventId(), EventType.MAIL);
    }

    @KafkaListener(topics = "mail-topic.DLT")
    public void failSendMail(MailEvent event, @Headers MessageHeaders headers) {
        log.info("Kafka Consumer failSendMail received: {}", event);

        String originalTopic = utilService.getHeaderAsString(headers, "kafka_dlt-original-topic");
        String errorMessage = utilService.extractRootMessage(utilService.getHeaderAsString(headers, "kafka_dlt-exception-message"));

        failEventService.saveEventFail(EventType.MAIL, originalTopic, event, errorMessage);
    }
}