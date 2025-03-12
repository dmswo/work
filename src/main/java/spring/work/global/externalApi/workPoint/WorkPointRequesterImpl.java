package spring.work.global.externalApi.workPoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import spring.work.global.externalApi.ApiRequest;
import spring.work.global.externalApi.ApiRequester;
import spring.work.global.externalApi.WebClientResponse;
import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;

import java.net.http.HttpHeaders;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkPointRequesterImpl implements WorkPointRequester{

    private final WorkPointProperty workPointProperty;
    private final ApiRequester apiRequester;

    @Override
    public UserPointInfoApiResponse getUserPoint(String userId) {
        ParameterizedTypeReference<Object> responseType = new ParameterizedTypeReference<>() {};
        HttpHeaders headers = HttpHeaders.of(null, null);
        WebClientResponse<Object> objectWebClientResponse = apiRequester.requestGet(
                ApiRequest.of(workPointProperty.getHostUrl(), workPointProperty.getGetUserPointUri(userId), headers, null, responseType));
        return null;
    }
}
