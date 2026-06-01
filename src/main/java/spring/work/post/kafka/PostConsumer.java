package spring.work.post.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import spring.work.post.kafka.dto.DebeziumCdcMessage;
import spring.work.post.kafka.dto.PostCdcDto;
import spring.work.post.document.PostDocument;
import spring.work.user.repository.ElasticSearchRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostConsumer {

    private final ElasticSearchRepository elasticSearchRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "work.work.post",
            groupId = "cdc-post-group",
            containerFactory = "cdcKafkaListenerContainerFactory"
    )
    public void handlePostCdc(ConsumerRecord<String, String> record) {
        String message = record.value();
        log.info("Received Debezium Message: {}", message);
        if (message == null) {
            log.info("CDC tombstone message ignored");
            return;
        }

        try {
            DebeziumCdcMessage<PostCdcDto> cdcMessage =
                    objectMapper.readValue(message, new TypeReference<DebeziumCdcMessage<PostCdcDto>>() {});

            String op = cdcMessage.getPayload().getOp();
            if ("u".equals(op) || "c".equals(op)) {
                PostCdcDto after = cdcMessage.getPayload().getAfter();
                if (after == null) {
                    log.warn("CDC after payload is null");
                    return;
                }

                PostDocument doc = PostDocument.builder()
                        .seq(after.getSeq())
                        .title(after.getTitle())
                        .content(after.getContent())
                        .viewCnt(after.getViewCnt())
                        .build();

                elasticSearchRepository.save(doc);

                log.info("ES sync completed. postId={}", after.getSeq());
            } else if ("d".equals(op)) {
                PostCdcDto before = cdcMessage.getPayload().getBefore();
                if (before == null) {
                    return;
                }

                elasticSearchRepository.deleteById(before.getSeq());

                log.info("ES delete completed. postId={}", before.getSeq());
            }
        } catch (Exception e) {
            // 1) 에러 로그
            log.error("CDC 메시지 처리 실패. message={}", message, e);

            // 2) DLQ 전송(추후 개발해보기)
            // kafkaTemplate.send("post-cdc-dlq", message);
        }
    }
}
