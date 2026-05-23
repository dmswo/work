package spring.work.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.comment.dto.response.CommentResponse;
import spring.work.post.entity.Post;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long seq;
    private String title;
    private String content;
    private Long viewCnt;
    List<CommentResponse> comments;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .viewCnt(post.getViewCnt())
                .comments(post.getComments().stream()
                        .map(CommentResponse::from).toList())
                .build();
    }
}
