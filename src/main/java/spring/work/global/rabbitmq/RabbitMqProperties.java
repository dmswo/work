package spring.work.global.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@AllArgsConstructor
public class RabbitMqProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
