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
public class NotificationEvent implements Event {
    private Long eventId; // 멱등성을 위해 이벤트Id 사용
    private Long receiverId;
    private Long senderId;
    private NotificationType type;
    private Long targetId; // LIKE, COMMENT 등

    @Override
    public String getTopic() {
        return "notification-topic";
    }

    public static NotificationEvent from(Long receiverId, Long senderId, NotificationType type, Long targetId) {
        return NotificationEvent.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .type(type)
                .targetId(targetId)
                .build();
    }
}
