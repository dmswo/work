package spring.work.global.rabbitmq.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MailDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerHelperService {

    private final JavaMailSender emailSender;

    @Value("${rabbitmq.queue.mail}")
    private String mailQueue;

    @RabbitListener(queues = "${rabbitmq.queue.mail}")
    public void receiveMailMessage(MailDto messageDto) {

        MimeMessage mailMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage);
            helper.setTo(messageDto.getToEmail());
            helper.setSubject(messageDto.getSubject());
            helper.setText(messageDto.getContent());

            emailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("e.message : {}", e.getMessage());
        }
    }
}
