package spring.work.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostListResponse {
    @Schema(description = "게시글 ID")
    private Long seq;

    @Schema(description = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용")
    private String content;

    @Schema(description = "조회수")
    private Long viewCnt;

    @Schema(description = "작성자 닉네임")
    private String nickname;

    @Schema(description = "작성자 ID")
    private String createdUser;

    @Schema(description = "좋아요 수")
    private Long likeCount;

    @Schema(description = "현재 사용자의 좋아요 여부")
    private boolean liked;

    @Schema(description = "게시글 작성일시", example = "2026-09-19 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수")
    private Long commentCount;
}
