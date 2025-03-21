package spring.work.global.externalApi.workPoint;

import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;
import spring.work.user.dto.request.CreatePoint;

public interface PointRequester {
    UserPointInfoApiResponse getUserPoint(String userId);
    UserPointInfoApiResponse createUserPoint(CreatePoint createPoint);
}
