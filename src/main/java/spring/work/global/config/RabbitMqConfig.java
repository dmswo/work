package spring.work.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import spring.work.global.rabbitmq.RabbitMqProperties;

@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {
    private final RabbitMqProperties rabbitMqProperties;

    @Value("${rabbitmq.queue.mail}")
    private String mailQueue;

    @Value("${rabbitmq.queue.dlq.mail}")
    private String dlqMailQueue;

    @Value("${rabbitmq.exchange.mail}")
    private String mailExchange;

    @Value("${rabbitmq.exchange.dlx.mail}")
    private String dlxMailExchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Bean
    @Primary
    public Queue mailQueue() {
        return QueueBuilder.durable(mailQueue)
                .withArgument("x-dead-letter-exchange", dlxMailExchange)
                .withArgument("x-dead-letter-routing-key", dlqMailQueue)
                .build();
    }

    @Bean
    public Queue dlqMailQueue() {
        return QueueBuilder.durable(dlqMailQueue).build();
    }

    @Bean
    @Primary
    public DirectExchange mailExchange() {
        return new DirectExchange(mailExchange);
    }

    @Bean
    public DirectExchange dlxMailExchange() {
        return new DirectExchange(dlxMailExchange);
    }

    @Bean
    @Primary
    public Binding binding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(routingKey);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqMailQueue()).to(dlxMailExchange()).with(dlqMailQueue);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
