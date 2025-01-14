package spring.work.global.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RabbitMqProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
