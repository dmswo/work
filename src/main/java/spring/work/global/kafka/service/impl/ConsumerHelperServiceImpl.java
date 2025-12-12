package spring.work.global.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.kafka.service.ConsumerHelperService;
import spring.work.global.utils.EmailSender;
import spring.work.user.service.UserAuthService;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    @KafkaListener(
            topics = "work.work.post",
            groupId = "cdc-post-group",
            containerFactory = "cdcKafkaListenerContainerFactory"
    )
    public void cdcPostUpdate(String message) {
        log.info("Received Debezium Message: {}", message);
    }
}
