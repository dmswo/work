package spring.work.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.work.global.response.ApiResponse;
import spring.work.user.request.SignupReqDto;
import spring.work.user.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="/User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", description = "회원가입 API")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody @Valid SignupReqDto signupReqDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.successWithNoContent(userService.signup(signupReqDto)));
    }

}
