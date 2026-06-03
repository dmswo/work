package spring.work.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.entity.Notification;
import spring.work.notification.repository.NotificationRepository;
import spring.work.notification.service.NotificationService;
import spring.work.user.entity.Users;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void sendNotification(Users receiver, Users sender, NotificationType type, Long targetId) {

        // 알림 정책 (본인 게시글 좋아요는 알림 제외)
        if (receiver.getSeq().equals(sender.getSeq())) {
            return;
        }

        Notification notification = Notification.create(receiver, sender, type, targetId);
        notificationRepository.save(notification);
    }
}
