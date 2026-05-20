package spring.work.comment.service;


import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;

public interface CommentService {
    void saveComment(CreateComment request, String userId);
    void updateComment(Long postId, UpdateComment request);
    void deleteComment(Long postId);
}
