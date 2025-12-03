package spring.work.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.user.dto.request.CreatePost;
import spring.work.user.service.UserPostService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/post")
@Tag(name="/user/post", description = "유저 게시글 관련 API")
public class UserPostController {

    private final UserPostService userPostService;

    @Operation(summary = "회원 게시글 저장 API", description = "회원 게시글 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> saveUserPost(@RequestBody @Valid CreatePost post) {
        userPostService.saveUserPost(post);
        return ApiResponse.successResponse(ResultCode.OK);
    }
}
