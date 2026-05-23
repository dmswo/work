package spring.work.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.global.dto.PageResponse;
import spring.work.global.security.auth.AuthUser;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.dto.response.PostResponse;
import spring.work.post.service.PostService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name="/post", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 저장 API", description = "게시글 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> savePost(@RequestBody @Valid CreatePost request, @AuthenticationPrincipal AuthUser authUser) {
        postService.savePost(request, authUser.getUserId());
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "게시글 수정 API", description = "게시글 수정 API")
    @PatchMapping("/{postId}")
    public ApiResponse<ResultCode> updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePost request) {
        postService.updatePost(postId, request);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "게시글 삭제 API", description = "게시글 삭제 API")
    @DeleteMapping("/{postId}")
    public ApiResponse<ResultCode> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "게시글 리스트 조회 API", description = "게시글 리스트 조회 API")
    @GetMapping
    public ApiResponse<PageResponse<PostListResponse>> getPosts(
            @ParameterObject
            PostSearchCondition condition,
            @ParameterObject
            @PageableDefault(size = 10,
                    sort = "seq",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.successResponse(postService.getPosts(condition, pageable));
    }

    @Operation(summary = "게시글 단건 조회 API", description = "게시글 단건 조회 API")
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable("postId") Long postId) {
        return ApiResponse.successResponse(postService.getPost(postId));
    }
}