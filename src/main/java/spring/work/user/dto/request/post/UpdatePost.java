package spring.work.user.dto.request.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePost {
    @Schema(defaultValue = "dmswo106")
    private String userId;
    @Schema(defaultValue = "정말 재미있는 곶감 이야기")
    private String title;
    @Schema(defaultValue = "어느 마을에 이상한 사냥꾼이...")
    private String content;
}
