package spring.work.user.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import spring.work.user.kafka.dto.MailDto;

@Service
@RequiredArgsConstructor
public class UserProducer {

    private final KafkaTemplate<String, MailDto> kafkaTemplate;
    private static final String MAIL = "mail-topic";

    public void sendMail(MailDto messageDto) {
        kafkaTemplate.send(MAIL, messageDto);
        System.out.println("Kafka Producer sent: " + messageDto);
    }
}
