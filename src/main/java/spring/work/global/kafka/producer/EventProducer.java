package spring.work.global.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import spring.work.global.kafka.dto.Event;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends Event> CompletableFuture<SendResult<String, Object>> send(T event) {
        return kafkaTemplate.send(event.getTopic(), event);
    }
}