package spring.work.global.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitMqService {

    @Value("${rabbitmq.queue.event}")
    private String eventQueue;

    @Value("${rabbitmq.queue.product}")
    private String productQueue;

    @Value("${rabbitmq.queue.ticket}")
    private String ticketQueue;

    private final RabbitTemplate rabbitTemplate;

    /**
     * 1. Queue 로 메세지를 발행
     * 2. Producer 역할 -> Direct Exchange 전략
     **/
    public void eventSendMessage(MessageDto messageDto) {
        log.info("eventSendMessage : {}",messageDto.toString());
        rabbitTemplate.convertAndSend(eventQueue, messageDto);
    }

    public void productSendMessage(MessageDto messageDto) {
        log.info("productSendMessage : {}",messageDto.toString());
        this.rabbitTemplate.convertAndSend(productQueue, messageDto);
    }

    public void ticketSendMessage(MessageDto messageDto) {
        log.info("ticketSendMessage : {}",messageDto.toString());
        this.rabbitTemplate.convertAndSend(ticketQueue, messageDto);
    }

    /**
     * 1. Queue 에서 메세지를 구독
     **/
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
}
