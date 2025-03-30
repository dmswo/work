package spring.work.global.externalApi;

import org.springframework.util.MultiValueMap;

public interface ApiRequester {
    <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestGet(ApiRequest<MultiValueMap<String, String>, RESPONSE> request);
    <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPost(ApiRequest<REQUEST, RESPONSE> request);
    <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestPatch(ApiRequest<REQUEST, RESPONSE> request);
}
