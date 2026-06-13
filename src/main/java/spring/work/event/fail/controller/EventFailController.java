package spring.work.event.fail.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.event.fail.service.EventFailService;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/event-fail")
@Tag(name="/event-fail", description = "이벤트 실패 API")
public class EventFailController {

    private final EventFailService eventFailService;

    @Operation(summary = "이벤트 실패 API", description = "이벤트 실패 API")
    @PostMapping("/{eventFailId}/retry")
    public ApiResponse<ResultCode> retryFailEvent(@PathVariable("eventFailId") Long eventFailId) {
        eventFailService.retryFailEvent(eventFailId);
        return ApiResponse.successResponse(ResultCode.OK);
    }
}
