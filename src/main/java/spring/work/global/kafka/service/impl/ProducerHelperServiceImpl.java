package spring.work.global.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.kafka.service.ProducerHelperService;

@Service
@RequiredArgsConstructor
public class ProducerHelperServiceImpl implements ProducerHelperService {
    private final KafkaTemplate<String, MailDto> kafkaTemplate;
    private static final String MAIL = "mail-topic";

    @Override
    public void sendMail(MailDto messageDto) {
        kafkaTemplate.send(MAIL, messageDto);
        System.out.println("Kafka Producer sent: " + messageDto);
    }
}
