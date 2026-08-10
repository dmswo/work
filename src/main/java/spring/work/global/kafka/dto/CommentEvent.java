package spring.work.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent implements Event{
    private String eventId; // 멱등성을 위해 이벤트Id 사용
    private Long postId;
    private Long postOwnerId;
    private Long replierId;

    @Override
    public String getTopic() {
        return "comment-topic";
    }

    public static CommentEvent from(Long postId, Long postOwnerId, Long replierId) {
        return CommentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .postId(postId)
                .postOwnerId(postOwnerId)
                .replierId(replierId)
                .build();
    }
}
