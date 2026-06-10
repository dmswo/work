package spring.work.global.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.work.global.kafka.dto.Event;

@Service
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public <T extends Event> void send(T event) {
        kafkaTemplate.send(event.getTopic(), event);
    }
}