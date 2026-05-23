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
public class CommentResponse {
    private String content;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;


    public static CommentResponse from(Comment commment) {
        return CommentResponse.builder()
                .content(commment.getContent())
                .nickname(commment.getUser().getNickname())
                .createdAt(commment.getCreatedAt())
                .build();
    }
}
