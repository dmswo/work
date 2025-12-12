package spring.work.global.kafka.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.kafka.dto.cdc.DebeziumCdcMessage;
import spring.work.global.kafka.dto.cdc.PostCdcDto;
import spring.work.global.kafka.service.ConsumerHelperService;
import spring.work.global.utils.EmailSender;
import spring.work.user.document.PostDocument;
import spring.work.user.repository.ElasticSearchRepository;
import spring.work.user.service.UserAuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerHelperServiceImpl implements ConsumerHelperService {

    private final EmailSender emailSender;
    private final UserAuthService userAuthService;
    private final ElasticSearchRepository elasticSearchRepository;

    @Override
    @KafkaListener(topics = "mail-topic")
    public void sendMail(MailDto messageDto) {
        System.out.println("Kafka Consumer received: " + messageDto);
        emailSender.sendEmail(messageDto);
    }

    @Override
    @KafkaListener(topics = "mail-topic.DLT")
    public void failSendMail(MailDto messageDto) {
        userAuthService.sendMailFailHistory(messageDto);
    }

    @Override
    @KafkaListener(
            topics = "work.work.post",
            groupId = "cdc-post-group",
            containerFactory = "cdcKafkaListenerContainerFactory"
    )
    public void cdcPostUpdate(String message) {
        log.info("Received Debezium Message: {}", message);

        ObjectMapper mapper = new ObjectMapper();

        try {
            DebeziumCdcMessage<PostCdcDto> cdcMessage =
                    mapper.readValue(message, new TypeReference<DebeziumCdcMessage<PostCdcDto>>() {});

            PostCdcDto after = cdcMessage.getPayload().getAfter();

            if (after != null) {
                log.info("Post Updated => {}", after);
            }

            // CDC를 통해 elasticsearch 저장
            PostDocument doc = PostDocument.builder()
                    .seq(after.getSeq())
                    .title(after.getTitle())
                    .content(after.getContent())
                    .viewCnt(after.getViewCnt())
                    .build();
            elasticSearchRepository.save(doc);
        } catch (Exception e) {
            // 1) 에러 로그
            log.error("CDC 메시지 처리 실패. message={}", message, e);

            // 2) DLQ 전송(추후 개발해보기)
            // kafkaTemplate.send("post-cdc-dlq", message);
        }
    }
}
