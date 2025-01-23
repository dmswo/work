package spring.work.global.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import spring.work.global.exception.BusinessException;
import spring.work.global.rabbitmq.dto.MailDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSender {
    private final JavaMailSender emailSender;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.dlq.mail}")
    private String errorMailQueue;

    public void sendEmail(MailDto messageDto) {
        MimeMessage mailMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage);
            helper.setTo(messageDto.getToEmail());
            helper.setSubject(messageDto.getSubject());
            helper.setText(messageDto.getContent());
            emailSender.send(mailMessage);
        } catch (Exception e) {
            log.error("e.message : {}", e.getMessage());
            rabbitTemplate.convertAndSend(errorMailQueue, messageDto);
        }
    }
}
