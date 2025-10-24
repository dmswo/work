package spring.work.user.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.work.global.externalApi.workPoint.PointRequester;
import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;
import spring.work.user.dto.response.UserPointResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserPointServiceImplTest {

    @Mock
    private PointRequester pointRequester;

    @InjectMocks
    private UserPointServiceImpl userPointService;

    @Test
    void getUserPoint() {
        // Given
        String userId = "dmswo106";
        UserPointInfoApiResponse response = UserPointInfoApiResponse.builder()
                .userId(userId)
                .pointBal(10000L)
                .build();
        given(pointRequester.getUserPoint(userId)).willReturn(response);

        // When
        UserPointResponse result = userPointService.getUserPoint(userId);

        // Then
        then(pointRequester).should(times(1)).getUserPoint(userId);
        then(pointRequester).shouldHaveNoMoreInteractions(); // "우리가 검증한 메서드 외에는 단 한 번도 호출되지 않았음을 보장한다" 라는 의미
        Assertions.assertThat(result.getUserId()).isEqualTo("dmswo106");
        Assertions.assertThat(result.getPointBal()).isEqualTo(10000L);
    }
}