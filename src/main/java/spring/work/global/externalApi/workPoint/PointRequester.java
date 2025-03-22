package spring.work.global.externalApi.workPoint;

import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;
import spring.work.user.dto.request.CreatePoint;
import spring.work.user.dto.response.CreatePointResponse;

public interface PointRequester {
    UserPointInfoApiResponse getUserPoint(String userId);
    CreatePointResponse createUserPoint(CreatePoint createPoint);
}
