package spring.work.global.externalApi.workPoint;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkPointProperty {

    private static final String USERID = "{userId}";

    @Value("${external.work-point.host-url}")
    @Getter
    private String hostUrl;

    @Value("${external.work-point.endpoint.get-user-point}")
    private String getUserPointUri;

    public String getGetUserPointUri(String userId) {
        return getUserPointUri.replace(USERID, userId);
    }
}
