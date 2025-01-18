package spring.work.global.rabbitmq.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerHelperService {

    private final JavaMailSender emailSender;

    @Value("${rabbitmq.queue.event}")
    private String eventQueue;

    @Value("${rabbitmq.queue.product}")
    private String productQueue;

    @Value("${rabbitmq.queue.ticket}")
    private String ticketQueue;

    @Value("${rabbitmq.queue.mail}")
    private String mailQueue;

    @RabbitListener(queues = "${rabbitmq.queue.event}")
    public void receiveEventMessage(MessageDto messageDto) {
        log.info("receiveEventMessage : {}",messageDto.toString());
    }

    @RabbitListener(queues = "${rabbitmq.queue.product}")
    public void receiveProductMessage(MessageDto messageDto) {
        log.info("receiveProductMessage : {}",messageDto.toString());
    }

    @RabbitListener(queues = "${rabbitmq.queue.ticket}")
    public void receiveTicketMessage(MessageDto messageDto) {
        log.info("receiveTicketMessage : {}",messageDto.toString());
    }

    @RabbitListener(queues = "${rabbitmq.queue.ticket}")
    public void receiveTicketMessage2(MessageDto messageDto) {
        log.info("receiveTicketMessage2 : {}",messageDto.toString());
    }

    @RabbitListener(queues = "${rabbitmq.queue.ticket}")
    public void receiveTicketMessage3(MessageDto messageDto) {
        log.info("receiveTicketMessage3 : {}",messageDto.toString());
    }

    @RabbitListener(queues = "${rabbitmq.queue.mail}")
    public void receiveMailMessage(MessageDto messageDto) {

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
