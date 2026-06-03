package spring.work.notification.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.entity.Notification;
import spring.work.notification.repository.NotificationRepository;
import spring.work.user.entity.Users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("본인에게는 알림이 저장되지 않는다")
    void not_save_notification_when_sender_is_receiver() {
        // Given
        Users user = Users.builder()
                .seq(1L)
                .build();

        // When
        notificationService.sendNotification(user, user, NotificationType.POST_LIKE, 1L);

        // Then
        then(notificationRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("알림전송 성공")
    void sendNotification_success() {
        // Given
        Users user1 = Users.builder()
                .seq(1L)
                .userId("test1")
                .nickname("testUser1")
                .build();

        Users user2 = Users.builder()
                .seq(2L)
                .userId("test2")
                .nickname("testUser2")
                .build();

        // When
        notificationService.sendNotification(user1, user2, NotificationType.POST_LIKE, 1L);

        // Then
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        then(notificationRepository).should().save(captor.capture());
        Notification savedNotification = captor.getValue();

        assertThat(savedNotification.getReceiver()).isEqualTo(user1);
        assertThat(savedNotification.getSender()).isEqualTo(user2);
        assertThat(savedNotification.getType()).isEqualTo(NotificationType.POST_LIKE);
        assertThat(savedNotification.getTargetId()).isEqualTo(1L);
    }
}