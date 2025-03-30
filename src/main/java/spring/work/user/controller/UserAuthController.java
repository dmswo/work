package spring.work.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.global.dto.ApiResponse;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.TokenInfo;
import spring.work.global.utils.UtilService;
import spring.work.user.dto.request.*;
import spring.work.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="/user", description = "유저 관련 API")
public class UserAuthController {

    private final UserService userService;
    private final UtilService utilService;

    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<ResultCode> signup(@RequestBody @Valid Signup signup) {
        return ApiResponse.successResponse(userService.signup(signup));
    }

    @Operation(summary = "로그인 API", description = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<TokenInfo> login(@RequestBody @Valid Login login, HttpServletRequest request) {
        return ApiResponse.successResponse(userService.login(login, utilService.getClientIp((request))));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API")
    @PostMapping("/logout")
    public ApiResponse<ResultCode> logout(HttpServletRequest request) {
        return ApiResponse.successResponse(userService.logout(request));
    }

    @Operation(summary = "토큰 재발행 API", description = "토큰 재발행 API")
    @PostMapping("/reissue")
    public ApiResponse<TokenInfo> reissue(HttpServletRequest request) {
        return ApiResponse.successResponse(userService.reissue(request));
    }

    @Operation(summary = "토큰 재발행 API", description = "토큰 재발행 API")
    @PostMapping("/test")
    public ApiResponse<TokenInfo> test(HttpServletRequest request) {
        return ApiResponse.successResponse(userService.reissue(request));
    }
}
