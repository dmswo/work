package spring.work.user.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import spring.work.global.utils.EmailSender;
import spring.work.user.kafka.dto.MailDto;
import spring.work.user.service.UserAuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {

    private final EmailSender emailSender;
    private final UserAuthService userAuthService;

    @KafkaListener(topics = "mail-topic")
    public void sendMail(MailDto messageDto) {
        System.out.println("Kafka Consumer received: " + messageDto);
        emailSender.sendEmail(messageDto);
    }

    @KafkaListener(topics = "mail-topic.DLT")
    public void failSendMail(MailDto messageDto) {
        userAuthService.sendMailFailHistory(messageDto);
    }
}
