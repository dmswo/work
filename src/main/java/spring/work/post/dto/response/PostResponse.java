package spring.work.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.post.entity.Post;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long seq;
    private String title;
    private String content;
    private Long viewCnt;
    private Long commentCnt;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .seq(post.getSeq())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCnt(post.getViewCnt())
                .commentCnt((long) post.getComments().size())
                .build();
    }
}
