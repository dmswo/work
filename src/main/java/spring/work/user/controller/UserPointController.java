package spring.work.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.global.dto.ApiResponse;
import spring.work.user.dto.response.GetUserPointResponse;
import spring.work.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/point")
@Tag(name="/user/point", description = "유저 포인트 관련 API")
public class UserPointController {
    private final UserService userService;

    @Operation(summary = "회원 포인트 조회 API", description = "회원 포인트 조회 API")
    @GetMapping("/{userId}")
    public ApiResponse<GetUserPointResponse> getUserPoint(@PathVariable("userId") String userId) {
        return ApiResponse.successResponse(userService.getUserPoint(userId));
    }
}
