package spring.work.global.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.work.notification.constant.NotificationType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent implements Event{
    private String eventId; // 멱등성을 위해 이벤트Id 사용
    private Long receiverId;
    private Long senderId;
    private NotificationType type;
    private Long targetId; // LIKE, COMMENT 등

    @Override
    public String getTopic() {
        return "";
    }
}
