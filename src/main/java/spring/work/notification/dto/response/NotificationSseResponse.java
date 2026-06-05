package spring.work.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.entity.Notification;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSseResponse {
    private Long notificationId;
    private String senderNickname;
    private NotificationType type;
    private Long targetId;

    public static NotificationSseResponse from(Notification notification) {
        return NotificationSseResponse.builder()
                .notificationId(notification.getId())
                .senderNickname(notification.getSender().getNickname())
                .type(notification.getType())
                .targetId(notification.getTargetId())
                .build();
    }
}
