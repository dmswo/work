package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.global.externalApi.workPoint.PointRequester;
import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;
import spring.work.user.dto.response.UserPointResponse;
import spring.work.user.service.UserPointService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPointServiceImpl implements UserPointService {

    private final PointRequester pointRequester;

    @Override
    public UserPointResponse getUserPoint(String userId) {
        UserPointInfoApiResponse userPoint = pointRequester.getUserPoint(userId);
        return UserPointResponse.builder()
                .userId(userPoint.getUserId())
                .pointBal(userPoint.getPointBal())
                .build();
    }
}
