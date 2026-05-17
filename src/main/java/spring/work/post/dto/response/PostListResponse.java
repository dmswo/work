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
public class PostListResponse {
    private Long seq;
    private String title;
    private String content;
    private Long viewCnt;

    private String nickname;

    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .seq(post.getSeq())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCnt(post.getViewCnt())
                .nickname(post.getUser().getNickname())
                .build();
    }
}
