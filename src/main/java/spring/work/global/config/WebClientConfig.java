package spring.work.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${external.work-point.host-url}")
    private String hostUrl;

    @Value("${Authorization.work-point.x-api-key-header}")
    private String apiHeader;

    @Value("${Authorization.work-point.x-api-key-value}")
    private String apiKey;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .build();
    }
}
