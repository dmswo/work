package spring.work.global.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MailDto;
import spring.work.global.utils.EmailSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerHelperService {

    private final EmailSender emailSender;

    @Value("${rabbitmq.queue.mail}")
    private String mailQueue;

    @RabbitListener(queues = "${rabbitmq.queue.mail}")
    public void signUpMailMessage(MailDto messageDto) {
        emailSender.sendEmail(messageDto);
    }

    @RabbitListener(queues = "${rabbitmq.queue.error.mail}")
    public void signUpMailErrorMessage(MailDto messageDto) {

    }
}
