package spring.work.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.notification.service.NotificationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(name="/notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 저장 API", description = "알림 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> saveNotification() {
        return ApiResponse.successResponse(ResultCode.OK);
    }
}
