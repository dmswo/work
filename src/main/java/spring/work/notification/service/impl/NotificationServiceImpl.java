package spring.work.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.notification.entity.Notification;
import spring.work.notification.repository.NotificationRepository;
import spring.work.notification.service.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}
