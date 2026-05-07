package spring.work.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.post.document.PostDocument;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long seq;
    private String title;
    private String content;
    private int viewCnt;

    public static PostResponse from(PostDocument doc) {
        return PostResponse.builder()
                .seq(doc.getSeq())
                .title(doc.getTitle())
                .content(doc.getContent())
                .viewCnt(doc.getViewCnt())
                .build();
    }
}
