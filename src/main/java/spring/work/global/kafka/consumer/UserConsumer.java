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
import spring.work.global.ai.service.AiService;
import spring.work.global.ai.dto.MailContent;
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
    private final AiService aiService;

    private static final String CONSUMER_GROUP = "mail-consumer-group";

    @Transactional
    @KafkaListener(topics = "mail-topic"
            , groupId = "mail-consumer-group"
            , concurrency = "3")
    public void sendMail(MailEvent event) {
        log.info("Kafka Consumer sendMail received: {}", event);

        // 1. 이미 처리한 이벤트인지 확인
        if (processedEventService.exists(event.getEventId(), CONSUMER_GROUP)) {
            log.info("이미 처리된 이벤트입니다. eventId={}", event.getEventId());
            return;
        }

        MailContent mail;

        // 2. OpenAi 회원가입 정보 만들어 오기
        try {
            mail = aiService.createWelcomeMail(event);
        } catch (Exception e) {
            log.error("AI 메일 생성 실패", e);

            mail = MailContent.builder()
                    .subject(event.getUserId() + "님의 Work 가입을 축하합니다.")
                    .content("""
                    안녕하세요.

                    Work 프로젝트 가입을 환영합니다.
                    즐거운 하루 되세요.
                    """)
                    .build();
        }

        // 3. 메일 발송
        emailSender.sendEmail(event, mail);

        // 4. 성공한 경우에만 처리 완료 기록
        processedEventService.save(event.getEventId(), CONSUMER_GROUP, EventType.MAIL);
    }

    @KafkaListener(topics = "mail-topic.DLT", groupId = "mail-dlt-consumer-group")
    public void failSendMail(MailEvent event, @Headers MessageHeaders headers) {
        log.info("Kafka Consumer failSendMail received: {}", event);

        String originalTopic = utilService.getHeaderAsString(headers, "kafka_dlt-original-topic");
        String errorMessage = utilService.extractRootMessage(utilService.getHeaderAsString(headers, "kafka_dlt-exception-message"));

        failEventService.saveEventFail(EventType.MAIL, originalTopic, event, errorMessage);
    }
}