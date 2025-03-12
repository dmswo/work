package spring.work.global.externalApi;

public interface ApiRequester {
    <REQUEST, RESPONSE> WebClientResponse<RESPONSE> requestGet(ApiRequest<REQUEST, RESPONSE> request);
    <REQUEST, RESPONSE> WebClientResponse<RESPONSE>  requestPost(ApiRequest<REQUEST, RESPONSE> request);
    <REQUEST, RESPONSE> WebClientResponse<RESPONSE>  requestPatch(ApiRequest<REQUEST, RESPONSE> request);
}
