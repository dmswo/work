package spring.work.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.work.notification.constant.NotificationType;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationListResponse {
    private Long id;
    private String senderNickname;
    private String message;
    private boolean isRead;
    private NotificationType type;
    private LocalDateTime createdAt;
}
