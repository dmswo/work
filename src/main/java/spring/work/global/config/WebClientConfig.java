package spring.work.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Value("${external.work-point.host-url}")
    private String hostUrl;

    @Value("${Authorization.work-point.x-api-key-header}")
    private String apiHeader;

    @Value("${Authorization.work-point.x-api-key-value}")
    private String apiKey;

    @Bean
    public DefaultUriBuilderFactory builderFactory(){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);
        return factory;
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .filter(addCommonHeadersForSpecificUrlFilter())
                .build();
    }

    private ExchangeFilterFunction addCommonHeadersForSpecificUrlFilter() {
        return (request, next) -> {
            if (request.url().toString().contains(hostUrl)) {

                Mono<ClientResponse> exchange = next.exchange(ClientRequest.from(request)
                        .header(apiHeader, apiKey)
                        .build());

                System.out.println("=== Request Headers ===");
                request.headers().forEach((name, values) ->
                        values.forEach(value -> System.out.println(name + " : " + value))
                );
                return exchange;
            }
            return next.exchange(request);
        };
    }
}
