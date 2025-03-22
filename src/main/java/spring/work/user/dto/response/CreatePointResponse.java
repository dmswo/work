package spring.work.user.dto.response;

import lombok.Data;

@Data
public class CreatePointResponse {
    private String userId;
    private Long pointBal;
}
