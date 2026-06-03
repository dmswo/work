package spring.work.notification.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.PageResponse;
import spring.work.global.exception.BusinessException;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.dto.response.NotificationListResponse;
import spring.work.notification.dto.response.UnreadNotificationResponse;
import spring.work.notification.entity.Notification;
import spring.work.notification.repository.NotificationRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private UserRepository userRepository;

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

    @Test
    @DisplayName("POST_LIKE 알림은 좋아요 메시지로 변환된다")
    void applyMessage_postLike() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<NotificationListResponse> content = List.of(NotificationListResponse.builder()
                .id(1L)
                .senderNickname("나홀로")
                .type(NotificationType.POST_LIKE)
                .build());

        Page<NotificationListResponse> page = new PageImpl<>(content, pageable, 1);

        given(notificationRepository.getNotifications(pageable)).willReturn(page);

        // When
        PageResponse<NotificationListResponse> result = notificationService.getNotifications(pageable);

        // Then
        assertThat(result.getContent().get(0).getMessage()).isEqualTo("나홀로님이 좋아요를 눌렀습니다");
    }

    @Test
    @DisplayName("알림 목록 조회 성공")
    void getNotifications_success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<NotificationListResponse> content = List.of(NotificationListResponse.builder()
                .id(1L)
                .senderNickname("나홀로")
                .type(NotificationType.POST_LIKE)
                .build());

        Page<NotificationListResponse> page = new PageImpl<>(content, pageable, 1);

        given(notificationRepository.getNotifications(pageable)).willReturn(page);

        // When
        PageResponse<NotificationListResponse> result = notificationService.getNotifications(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("알림 읽을때 존재하지 않는 알림이면 예외가 발생한다")
    void throw_exception_when_notification_read_notification_not_found() {
        // Given
        Long notificationId = 1L;
        String userId = "dmswo";
        Users user = Users.builder()
                .seq(1L)
                .userId(userId)
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(notificationRepository.findByIdAndReceiverSeq(notificationId, user.getSeq())).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> notificationService.readNotification(notificationId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.NOTIFICATION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("알림 읽음 처리 성공")
    void readNotification_success() {
        // Given
        Long notificationId = 1L;
        String userId = "dmswo";
        Users user = Users.builder()
                .seq(1L)
                .userId(userId)
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .isRead(false)
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(notificationRepository.findByIdAndReceiverSeq(notificationId, user.getSeq())).willReturn(Optional.of(notification));

        // When
        notificationService.readNotification(notificationId, userId);

        // Then
        assertThat(notification.isRead()).isTrue();
    }

    @Test
    @DisplayName("읽지 않은 알림건수 조회 성공")
    void getUnreadNotificationCount_success() {
        // Given
        String userId = "dmswo";
        Users user = Users.builder()
                .seq(1L)
                .userId(userId)
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(notificationRepository.countByReceiver_SeqAndIsReadFalse(1L)).willReturn(3L);

        // When
        UnreadNotificationResponse result = notificationService.getUnreadNotificationCount(userId);

        // Then
        assertThat(result.getUnreadCount()).isEqualTo(3L);
    }
}