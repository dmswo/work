package spring.work.comment.service;


import org.springframework.data.domain.Pageable;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.dto.response.CommentListResponse;
import spring.work.global.dto.PageResponse;

public interface CommentService {
    void saveComment(CreateComment request, String userId);
    void updateComment(Long postId, UpdateComment request);
    void deleteComment(Long postId);
    PageResponse<CommentListResponse> getComments(Long postId, Pageable pageable);
}
