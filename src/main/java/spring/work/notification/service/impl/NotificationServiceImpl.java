package spring.work.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.dto.PageResponse;
import spring.work.global.exception.BusinessException;
import spring.work.notification.constant.NotificationType;
import spring.work.notification.dto.response.NotificationListResponse;
import spring.work.notification.dto.response.NotificationSseResponse;
import spring.work.notification.dto.response.UnreadNotificationResponse;
import spring.work.notification.entity.Notification;
import spring.work.notification.repository.NotificationRepository;
import spring.work.notification.service.NotificationService;
import spring.work.notification.service.NotificationSseService;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.util.List;

import static spring.work.global.constant.ExceptionCode.NOTIFICATION_NOT_FOUND;
import static spring.work.global.constant.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationSseService notificationSseService;

    @Transactional
    @Override
    public void sendNotification(Users receiver, Users sender, NotificationType type, Long targetId) {

        // 알림 정책 (본인 게시글 좋아요는 알림 제외)
        if (receiver.getSeq().equals(sender.getSeq())) {
            return;
        }

        Notification notification = Notification.create(receiver, sender, type, targetId);
        notificationRepository.save(notification);

        // SSE 전송
        try {
            notificationSseService.send(receiver.getUserId(), NotificationSseResponse.from(notification));
        } catch (Exception e) {
            log.error("SSE 전송 실패", e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<NotificationListResponse> getNotifications(Pageable pageable) {
        Page<NotificationListResponse> notifications = notificationRepository.getNotifications(pageable);

        List<NotificationListResponse> content = notifications.getContent();
        for (NotificationListResponse dto : content) {
            applyMessage(dto);
        }

        return PageResponse.from(notifications);
    }

    private void applyMessage(NotificationListResponse dto) {
        dto.setMessage(
                switch (dto.getType()) {
                    case POST_LIKE -> dto.getSenderNickname() + "님이 좋아요를 눌렀습니다";
//                    case COMMENT -> dto.getSenderNickname() + "님이 댓글을 남겼습니다";
//                    case FOLLOW -> dto.getSenderNickname() + "님이 팔로우했습니다";
                }
        );
    }

    @Transactional
    @Override
    public void readNotification(Long notificationId, String userId) {
        Users user = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Notification notification =
                notificationRepository.findByIdAndReceiverSeq(notificationId, user.getSeq())
                        .orElseThrow(() -> new BusinessException(NOTIFICATION_NOT_FOUND));

        notification.readNotification();
    }

    @Transactional(readOnly = true)
    @Override
    public UnreadNotificationResponse getUnreadNotificationCount(String userId) {
        Users user = userRepository.findByUserId(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        Long count = notificationRepository.countByReceiver_SeqAndIsReadFalse(user.getSeq());

        return UnreadNotificationResponse.builder()
                .unreadCount(count)
                .build();
    }
}
