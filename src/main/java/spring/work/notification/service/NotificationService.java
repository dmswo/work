package spring.work.notification.service;

import spring.work.notification.constant.NotificationType;
import spring.work.user.entity.Users;

public interface NotificationService {
    void sendNotification(Users receiver, Users sender, NotificationType type, Long targetId);
}
