package spring.work.global.externalApi.workPoint;

import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;

public interface PointRequester {
    UserPointInfoApiResponse getUserPoint(String userId);
}
