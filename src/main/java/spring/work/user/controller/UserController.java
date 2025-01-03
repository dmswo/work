package spring.work.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.work.global.dto.ApiResponse;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.TokenInfo;
import spring.work.user.dto.request.Login;
import spring.work.user.dto.request.Signup;
import spring.work.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="/user", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/signup")
    public ApiResponse<ResultCode> signup(@RequestBody @Valid Signup signup) {
        return ApiResponse.successResponse(userService.signup(signup));
    }

    @Operation(summary = "로그인 API", description = "로그인 API")
    @PostMapping("/login")
    public ApiResponse<TokenInfo> login(@RequestBody @Valid Login login) {
        return ApiResponse.successResponse(userService.login(login));
    }
}
