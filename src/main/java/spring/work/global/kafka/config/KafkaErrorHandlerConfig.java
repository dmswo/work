package spring.work.global.kafka.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import spring.work.global.exception.BusinessException;

@Configuration
public class KafkaErrorHandlerConfig {
    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> template) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(template,
                        (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));

        // 1초 간격, 3회 재시도
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        DefaultErrorHandler handler =
                new DefaultErrorHandler(recoverer, backOff);

        // BusinessException 재시도하지 않고 바로 DLT
        handler.addNotRetryableExceptions(BusinessException.class);

        return handler;
    }
}
