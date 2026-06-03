package spring.work.notification.service;

import org.springframework.data.domain.Pageable;
import spring.work.global.dto.PageResponse;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.dto.response.NotificationListResponse;
import spring.work.notification.dto.response.UnreadNotificationResponse;
import spring.work.user.entity.Users;


public interface NotificationService {
    void sendNotification(Users receiver, Users sender, NotificationType type, Long targetId);
    PageResponse<NotificationListResponse> getNotifications(Pageable pageable);
    void readNotification(Long notificationId, String userId);
    UnreadNotificationResponse getUnreadNotificationCount(String userId);
}
