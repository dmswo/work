package spring.work.global.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MessageDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerHelperServiceImpl implements ProducerHelperService {

    @Value("${rabbitmq.queue.event}")
    private String eventQueue;

    @Value("${rabbitmq.queue.product}")
    private String productQueue;

    @Value("${rabbitmq.queue.ticket}")
    private String ticketQueue;

    @Value("${rabbitmq.queue.mail}")
    private String mailQueue;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void eventSendMessage(MessageDto messageDto) {
        log.info("eventSendMessage : {}",messageDto.toString());
        rabbitTemplate.convertAndSend(eventQueue, messageDto);
    }

    @Override
    public void productSendMessage(MessageDto messageDto) {
        log.info("productSendMessage : {}",messageDto.toString());
        this.rabbitTemplate.convertAndSend(productQueue, messageDto);
    }

    @Override
    public void ticketSendMessage(MessageDto messageDto) {
        log.info("ticketSendMessage : {}",messageDto.toString());
        this.rabbitTemplate.convertAndSend(ticketQueue, messageDto);
    }

    @Override
    public void sendMail(MessageDto messageDto) {
        rabbitTemplate.convertAndSend(mailQueue, messageDto);
    }
}
