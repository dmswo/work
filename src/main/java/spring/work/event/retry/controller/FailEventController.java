package spring.work.event.retry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.event.retry.service.FailEventService;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/fail-event")
@Tag(name="/fail-event", description = "실패 이벤트 API")
public class FailEventController {

    private final FailEventService failEventService;

    @Operation(summary = "실패 이벤트 API", description = "실패 이벤트 API")
    @PostMapping("/{failEventId}/retry")
    public ApiResponse<ResultCode> retryFailEvent(@PathVariable("failEventId") Long failEventId) {
        failEventService.retryFailEvent(failEventId);
        return ApiResponse.successResponse(ResultCode.OK);
    }
}
