package spring.work.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.global.dto.PageResponse;
import spring.work.global.security.auth.AuthUser;
import spring.work.notification.dto.response.NotificationListResponse;
import spring.work.notification.dto.response.UnreadNotificationResponse;
import spring.work.notification.service.NotificationService;
import spring.work.notification.service.NotificationSseService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name="/notifications", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationSseService notificationSseService;

    @Operation(summary = "알림 목록 조회 API", description = "알림 목록 리스트를 조회합니다.")
    @GetMapping
    public ApiResponse<PageResponse<NotificationListResponse>> getNotifications (
            @ParameterObject
            @PageableDefault(size = 10,
                    sort = "seq",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.successResponse(notificationService.getNotifications(pageable));
    }

    @Operation(summary = "알림 읽음 처리 API", description = "특정 알림을 읽음 상태로 변경합니다.")
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<ResultCode> readNotification(
            @PathVariable("notificationId") Long notificationId,
            @AuthenticationPrincipal AuthUser authUser) {
        notificationService.readNotification(notificationId, authUser.getUserId());
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "읽지 않은 알림 건수 조회 API", description = "읽지 않은 알림 건수를 집계합니다.")
    @GetMapping("/unread-count")
    public ApiResponse<UnreadNotificationResponse> getUnreadNotificationCount(@AuthenticationPrincipal AuthUser authUser) {
        return ApiResponse.successResponse(notificationService.getUnreadNotificationCount(authUser.getUserId()));
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal AuthUser authUser) {
        return notificationSseService.subscribe(authUser.getUserId());
    }
}
