package spring.work.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.user.dto.request.post.CreatePost;
import spring.work.user.dto.request.post.UpdatePost;
import spring.work.user.dto.response.post.PostResponse;
import spring.work.user.service.UserPostService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name="/posts", description = "유저 게시글 관련 API")
public class UserPostController {

    private final UserPostService userPostService;

    @Operation(summary = "회원 게시글 저장 API", description = "회원 게시글 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> saveUserPost(@RequestBody @Valid CreatePost post) {
        userPostService.saveUserPost(post);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "회원 게시글 수정 API", description = "회원 게시글 수정 API")
    @PatchMapping("/{postId}")
    public ApiResponse<ResultCode> updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePost post) {
        userPostService.updateUserPost(postId, post);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "회원 게시글 조회 API", description = "회원 게시글 조회 API")
    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.successResponse(userPostService.getPosts());
    }
}
