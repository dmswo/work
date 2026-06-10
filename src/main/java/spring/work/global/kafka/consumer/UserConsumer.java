package spring.work.global.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.utils.EmailSender;
import spring.work.user.service.UserAuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailSender emailSender;
    private final UserAuthService userAuthService;

    @KafkaListener(topics = "mail-topic")
    public void sendMail(MailEvent event) {
        log.info("Kafka Consumer sendMail received: {}", event);
        emailSender.sendEmail(event);
    }

    @KafkaListener(topics = "mail-topic.DLT")
    public void failSendMail(MailEvent event) {
        log.info("Kafka Consumer failSendMail received: {}", event);
        userAuthService.sendMailFailHistory(event);
    }
}