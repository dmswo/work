package spring.work.global.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerHelperService {

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
        log.info("receiveMailMessage : {}",messageDto.toString());
    }
}
