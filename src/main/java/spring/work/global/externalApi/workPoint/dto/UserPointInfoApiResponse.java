package spring.work.global.externalApi.workPoint.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPointInfoApiResponse {
    private String userId;
    private Long pointBal;
}
