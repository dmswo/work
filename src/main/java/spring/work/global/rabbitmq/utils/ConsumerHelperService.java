package spring.work.global.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MailDto;
import spring.work.global.utils.EmailSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerHelperService {

    private final EmailSender emailSender;

    @RabbitListener(queues = "${rabbitmq.queue.mail}")
    public void signUpMailMessage(MailDto messageDto) {
        emailSender.sendEmail(messageDto);
    }

    @RabbitListener(queues = "${rabbitmq.queue.dlq.mail}")
    public void signUpMailErrorMessage(MailDto messageDto) {
        System.out.println(messageDto);
    }
}
