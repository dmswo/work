package spring.work.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.comment.entity.Comment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
    private String content;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;


    public static CommentListResponse from(Comment commment) {
        return CommentListResponse.builder()
                .content(commment.getContent())
                .nickname(commment.getUser().getNickname())
                .createdAt(commment.getCreatedAt())
                .build();
    }
}
