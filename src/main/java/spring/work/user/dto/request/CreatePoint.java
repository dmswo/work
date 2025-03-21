package spring.work.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePoint {
    private String userId;
}
