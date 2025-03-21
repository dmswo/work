package spring.work.global.externalApi.workPoint;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import spring.work.global.externalApi.ApiRequest;
import spring.work.global.externalApi.ApiRequester;
import spring.work.global.externalApi.WebClientResponse;
import spring.work.global.externalApi.workPoint.dto.PointCommonResponse;
import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;
import spring.work.user.dto.request.CreatePoint;
import spring.work.user.dto.response.CreatePointResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class PointRequesterImpl implements PointRequester {

    private final PointProperty pointProperty;
    private final ApiRequester apiRequester;

    private HttpHeaders makeHttpHeaders() {
        return new HttpHeaders();
    }

    @Override
    public UserPointInfoApiResponse getUserPoint(String userId) {
        ParameterizedTypeReference<PointCommonResponse<UserPointInfoApiResponse>> responseType = new ParameterizedTypeReference<>() {};
        HttpHeaders headers = makeHttpHeaders();
        WebClientResponse<PointCommonResponse<UserPointInfoApiResponse>> response = apiRequester.requestGet(
                ApiRequest.of(pointProperty.getHostUrl(), pointProperty.getGetUserPointUri(userId), headers, null, responseType));
        return response.getBody().getData();
    }

    @Override
    public CreatePointResponse createUserPoint(CreatePoint createPoint) {
        ParameterizedTypeReference<PointCommonResponse<CreatePointResponse>> responseType = new ParameterizedTypeReference<>() {};
        HttpHeaders headers = makeHttpHeaders();
        WebClientResponse<PointCommonResponse<CreatePointResponse>> response = apiRequester.requestPost(
                ApiRequest.of(pointProperty.getHostUrl(), pointProperty.getCreateUserPointUri(), headers, createPoint, responseType));
        return response.getBody().getData();
    }
}
