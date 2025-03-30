package spring.work.global.externalApi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRequesterImpl implements ApiRequester {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestGet(ApiRequest<MultiValueMap<String, String>, RESPONSE> apiRequest) {
        MultiValueMap<String, String> request = apiRequest.getRequest();
        log.info("httpUrl : {} {}, requestHeader : {}, requestBody : {}", HttpMethod.GET, apiRequest.getEndPoint(), apiRequest.getHeaders(), request);
        WebClient.RequestBodySpec requestBodySpec = webClient.mutate()
                .baseUrl(apiRequest.getHostUrl())
                .build()
                .method(HttpMethod.GET)
                .uri(apiRequest.getEndPoint())
                .headers(header -> header.addAll(apiRequest.getHeaders()));

        if (request != null) {
            requestBodySpec.bodyValue(request);
        }

        WebClientResponse<RESPONSE> response = requestBodySpec
                .exchangeToMono(clientResponse -> processResponse(clientResponse, apiRequest.getResponse()))
                .onErrorMap(ex -> new BusinessException(ExceptionCode.FAIL))
                .block();

        return response;
    }

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPost(ApiRequest<REQUEST, RESPONSE> request) {
        return requestCommand(HttpMethod.POST, request);
    }

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPatch(ApiRequest<REQUEST, RESPONSE> request) {
        return requestCommand(HttpMethod.PATCH, request);
    }

    private <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestCommand(HttpMethod httpMethod, ApiRequest<REQUEST, RESPONSE> apiRequest) {
        REQUEST request = apiRequest.getRequest();
        log.info("httpUrl : {} {}, requestHeader : {}, requestBody : {}", httpMethod, apiRequest.getEndPoint(), apiRequest.getHeaders(), request);
        WebClient.RequestBodySpec requestBodySpec = webClient.mutate()
                .baseUrl(apiRequest.getHostUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .method(httpMethod)
                .uri(apiRequest.getEndPoint())
                .headers(header -> header.addAll(apiRequest.getHeaders()));

        if (request != null) {
            requestBodySpec.bodyValue(request);
        }

        WebClientResponse<RESPONSE> response = requestBodySpec
                .exchangeToMono(clientResponse -> processResponse(clientResponse, apiRequest.getResponse()))
                .onErrorMap(ex -> new BusinessException(ExceptionCode.FAIL))
                .block();

        return response;
    }

    private <RESPONSE> Mono<WebClientResponse<RESPONSE>> processResponse(ClientResponse clientResponse, ParameterizedTypeReference<RESPONSE> responseType) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(body -> {
                    log.info("Raw Response Body: {}", body);
                    try {
                        TypeFactory typeFactory = objectMapper.getTypeFactory();
                        JavaType javaType = typeFactory.constructType(responseType.getType());
                        RESPONSE parsedBody = objectMapper.readValue(body, javaType);
                        return Mono.just(new WebClientResponse<>((HttpStatus) clientResponse.statusCode(), clientResponse.headers().asHttpHeaders(), parsedBody));
                    } catch (JsonProcessingException e) {
                        log.error("Error parsing response body: {}", body, e);
                        return Mono.error(new BusinessException(ExceptionCode.FAIL));
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Empty response body with status code: {}", clientResponse.statusCode());
                    return Mono.just(new WebClientResponse<>((HttpStatus) clientResponse.statusCode(), clientResponse.headers().asHttpHeaders(), null));
                }));
    }
}
