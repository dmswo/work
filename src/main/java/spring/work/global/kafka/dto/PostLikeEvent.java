package spring.work.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.postlike.constant.LikeActionType;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeEvent implements Event{
    private String eventId; // 멱등성을 위해 이벤트Id 사용
    private Long postId;
    private Long postOwnerId;
    private Long likerId;

    private LikeActionType action;

    @Override
    public String getTopic() {
        return "post-like-topic";
    }

    public static PostLikeEvent from(Long postId, Long postOwnerId, Long likerId, LikeActionType action) {
        return PostLikeEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .postId(postId)
                .postOwnerId(postOwnerId)
                .likerId(likerId)
                .action(action)
                .build();
    }
}
