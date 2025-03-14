package spring.work.global.externalApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import spring.work.global.exception.BusinessException;

import static spring.work.global.constant.ExceptionCode.EXTERNAL_API_ERROR;

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
        WebClient.RequestBodySpec requestBodySpec = webClient.mutate()
                .baseUrl(request.getHostUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .method(httpMethod)
                .uri(request.getEndPoint())
                .headers(header -> header.addAll(request.getHeaders()));

        // 1. retreive 방식 이해하기
        RESPONSE response = requestBodySpec
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
                )
                .bodyToMono(request.getResponse()) // 응답 타입 변환
                .block();// 동기 처리

        // 2. exchangeToMono 방식 이해하기

        return new WebClientResponse<>(response);
    }
}
