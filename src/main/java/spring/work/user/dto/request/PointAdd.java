package spring.work.user.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointAdd {
    private String userId;
    private int point;
}
