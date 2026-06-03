package spring.work.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.notification.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
