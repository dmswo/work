package spring.work.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.service.CommentService;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.ApiResponse;
import spring.work.global.security.auth.AuthUser;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/commment")
@Tag(name="/commment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 저장 API", description = "댓글 저장 API")
    @PostMapping
    public ApiResponse<ResultCode> saveComment(@RequestBody @Valid CreateComment request, @AuthenticationPrincipal AuthUser authUser) {
        commentService.saveComment(request, authUser.getUserId());
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
}
