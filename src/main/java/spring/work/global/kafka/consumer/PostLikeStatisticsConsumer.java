package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.event.common.EventType;
import spring.work.event.processed.service.ProcessedEventService;
import spring.work.global.kafka.dto.PostLikeEvent;
import spring.work.statistics.service.UserActivityStatisticsService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeStatisticsConsumer {

    private final ProcessedEventService processedEventService;
    private final UserActivityStatisticsService userActivityStatisticsService;
    private static final String STATISTIC_GROUP = "post-like-statistic-consumer-group";

    @Transactional
    @KafkaListener(topics = "post-like-topic"
            , groupId = STATISTIC_GROUP
            , concurrency = "3")
    public void updateUserActivityStatistics(PostLikeEvent event) {
        log.info("Kafka Consumer updateUserActivityStatistics received: {}", event);

        // 1. 이미 처리한 이벤트인지 확인
        if (processedEventService.exists(event.getEventId(), STATISTIC_GROUP)) {
            log.info("이미 처리된 이벤트입니다. eventId={}", event.getEventId());
            return;
        }

        // 2. 통계 기록 저장
        userActivityStatisticsService.increaseLikeCount(event.getLikerId());

        // 3. 성공한 경우에만 처리 완료 기록
        processedEventService.save(event.getEventId(), STATISTIC_GROUP, EventType.POST_LIKE);
    }
}


