package spring.work.postlike.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.global.security.auth.AuthUser;
import spring.work.postlike.service.PostLikeService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/likes")
@Tag(name="/post-like", description = "게시글 좋아요 API")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 저장 API", description = "게시글 좋아요 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> savePostLike(@PathVariable("postId") Long postId, @AuthenticationPrincipal AuthUser authUser) {
        postLikeService.savePostLike(postId, authUser.getUserId());
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "게시글 좋아요 취소 API", description = "게시글 좋아요 취소 API")
    @DeleteMapping
    public ApiResponse<ResultCode> deletePostLike(@PathVariable("postId") Long postId, @AuthenticationPrincipal AuthUser authUser) {
        postLikeService.deletePostLike(postId, authUser.getUserId());
        return ApiResponse.successResponse(ResultCode.OK);
    }
}
