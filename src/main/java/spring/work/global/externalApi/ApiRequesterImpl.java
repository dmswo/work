package spring.work.global.externalApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRequesterImpl implements ApiRequester {

    private final WebClient webClient;

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestGet(ApiRequest<REQUEST, RESPONSE> request) {
        return requestCommand(HttpMethod.GET, request);
    }

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPost(ApiRequest<REQUEST, RESPONSE> request) {
        return requestCommand(HttpMethod.POST, request);
    }

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPatch(ApiRequest<REQUEST, RESPONSE> request) {
        return requestCommand(HttpMethod.PATCH, request);
    }

    private <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestCommand(HttpMethod httpMethod, ApiRequest<REQUEST, RESPONSE> request) {
        WebClient result = WebClient.builder()
                .baseUrl(request.getHostUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

        WebClient.RequestBodySpec requestBodySpec = result.mutate()
                .build()
                .method(httpMethod)
                .uri(request.getEndPoint())
                .headers(header -> header.addAll(request.getHeaders()));

        // 2. exchangeToMono 방식 이해하기
//        WebClientResponse<?> block = requestBodySpec.exchangeToMono(response -> {
//            HttpHeaders responseHeaders = response.headers().asHttpHeaders();
//            HttpStatus status = (HttpStatus) response.statusCode();
//
//            if (status.is2xxSuccessful()) {
//                return response.bodyToMono(request.getResponse())
//                        .map(body -> new WebClientResponse<>(status, responseHeaders, body));
//            } else {
//                return response.createException()
//                        .flatMap(error -> Mono.just(new WebClientResponse<>(status, responseHeaders, error)));
//            }
//        }).block();

        return null;
    }
}
