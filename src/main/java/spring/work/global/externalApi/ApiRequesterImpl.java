package spring.work.global.externalApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRequesterImpl implements ApiRequester {

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestGet(ApiRequest<REQUEST, RESPONSE> request) {
        return null;
    }

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPost(ApiRequest<REQUEST, RESPONSE> request) {
        return null;
    }

    @Override
    public <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPatch(ApiRequest<REQUEST, RESPONSE> request) {
        return null;
    }
}
