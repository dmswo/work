package spring.work.global.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MailDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerHelperServiceImpl implements ProducerHelperService {

    @Value("${rabbitmq.exchange.mail}")
    private String mailExchange;

    @Value("${rabbitmq.routing.mail}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMail(MailDto mailDto) {
        rabbitTemplate.convertAndSend(mailExchange, routingKey, mailDto);
    }
}
