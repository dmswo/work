package spring.work.global.externalApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRequestImpl implements ApiRequest{
    private final WebClient.Builder webClientBuilder;

    @Override
    public Object get(Object o) {
        WebClient webClient = webClientBuilder.build();
        Mono<String> response = webClient.get()
                .uri("http://localhost:8081/api/v1")
                .retrieve()
                .bodyToMono(String.class);

        return response.block();
    }

    @Override
    public Object post(Object o) {
        WebClient webClient = webClientBuilder.build();

        Mono<String> response = webClient.post()
                .uri("http://localhost:8081/api/v1")
                .bodyValue(o)
                .retrieve()
                .bodyToMono(String.class);

        return response.block();
    }

    @Override
    public Object patch(Object o) {
        WebClient webClient = webClientBuilder.build();

        Mono<String> response = webClient.patch()
                .uri("http://localhost:8081/api/v1")
                .bodyValue(o)
                .retrieve()
                .bodyToMono(String.class);

        return response.block();
    }
}
