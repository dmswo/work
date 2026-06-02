package spring.work.comment.controller;

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
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.dto.response.CommentListResponse;
import spring.work.comment.service.CommentService;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.global.dto.PageResponse;
import spring.work.global.security.auth.AuthUser;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
@Tag(name="/posts/{postId}/comments", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 저장 API", description = "댓글 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> saveComment(@RequestBody @Valid CreateComment request,
                                               @PathVariable("postId") Long postId,
                                               @AuthenticationPrincipal AuthUser authUser) {
        commentService.saveComment(request, postId, authUser.getUserId());
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "댓글 수정 API", description = "댓글 수정 API")
    @PatchMapping("/{commentId}")
    public ApiResponse<ResultCode> updateComment(@PathVariable("commentId") Long commentId, @RequestBody @Valid UpdateComment request) {
        commentService.updateComment(commentId, request);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API")
    @DeleteMapping("/{commentId}")
    public ApiResponse<ResultCode> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "댓글 리스트 조회 API", description = "댓글 리스트 조회 API")
    @GetMapping
    public ApiResponse<PageResponse<CommentListResponse>> getComments(
            @PathVariable("postId") Long postId,
            @ParameterObject
            @PageableDefault(size = 10,
                    sort = "seq",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.successResponse(commentService.getComments(postId, pageable));
    }

    @Operation(summary = "대댓글 작성 API", description = "대댓글 작성 API")
    @PostMapping("/{commentId}/replies")
    public ApiResponse<ResultCode> saveReply(@RequestBody @Valid CreateComment request,
                                             @PathVariable("postId") Long postId,
                                             @PathVariable("commentId") Long commentId,
                                             @AuthenticationPrincipal AuthUser authUser) {
        commentService.saveReply(request, postId, commentId, authUser.getUserId());
        return ApiResponse.successResponse(ResultCode.OK);
    }

    @Operation(summary = "대댓글 조회 API", description = "대댓글 조회 API")
    @GetMapping("/{commentId}/replies")
    public ApiResponse<PageResponse<CommentListResponse>> getReplies(
            @PathVariable("commentId") Long commentId,
            @ParameterObject
            @PageableDefault(size = 10,
                    sort = "seq",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.successResponse(commentService.getReplies(commentId, pageable));
    }
}
