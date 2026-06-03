package spring.work.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.work.notification.entity.Notification;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository{
    Optional<Notification> findByIdAndReceiverSeq(Long notificationId, Long userSeq);
    Long countByReceiver_SeqAndIsReadFalse(Long userSeq);
}
