package spring.work.global.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.kafka.service.ConsumerHelperService;
import spring.work.global.utils.EmailSender;
import spring.work.user.service.UserAuthService;

@Service
@RequiredArgsConstructor
public class ConsumerHelperServiceImpl implements ConsumerHelperService {

    private final EmailSender emailSender;
    private final UserAuthService userAuthService;

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
}
