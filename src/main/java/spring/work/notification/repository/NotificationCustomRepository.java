package spring.work.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.work.notification.dto.response.NotificationListResponse;

public interface NotificationCustomRepository {
    Page<NotificationListResponse> getNotifications(Pageable pageable);
}
