package spring.work.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostResponse;
import spring.work.post.service.PostService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name="/posts", description = "유저 게시글 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(summary = "회원 게시글 저장 API", description = "회원 게시글 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> saveUserPost(@RequestBody @Valid CreatePost post) {
        postService.saveUserPost(post);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "회원 게시글 수정 API", description = "회원 게시글 수정 API")
    @PatchMapping("/{postId}")
    public ApiResponse<ResultCode> updatePost(@PathVariable("postId") Long postId, @RequestBody @Valid UpdatePost post) {
        postService.updateUserPost(postId, post);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "회원 게시글 조회 API", description = "회원 게시글 조회 API")
    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts() {
        return ApiResponse.successResponse(postService.getPosts());
    }
}