package spring.work.global.externalApi.workPoint;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PointProperty {

    private static final String USERID = "{userId}";

    @Value("${external.work-point.host-url}")
    @Getter
    private String hostUrl;

    @Value("${Authorization.work-point.x-api-key-header}")
    @Getter
    private String apiKeyHeader;

    @Value("${Authorization.work-point.x-api-key-value}")
    @Getter
    private String apiKeyValue;

    @Value("${external.work-point.endpoint.get-user-point}")
    @Getter
    private String getUserPointUri;

    @Value("${external.work-point.endpoint.create-user-point}")
    @Getter
    private String createUserPointUri;

    public String getGetUserPointUri(String userId) {
        return getUserPointUri.replace(USERID, userId);
    }
}
