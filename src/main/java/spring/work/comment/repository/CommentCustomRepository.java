package spring.work.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.work.comment.dto.response.CommentListResponse;

import java.util.List;
import java.util.Map;

public interface CommentCustomRepository {
    Page<CommentListResponse> commentList(Long postId, Pageable pageable);
    Page<CommentListResponse> commentReplyList(Long commentId, Pageable pageable);
    Map<Long, Long> countByPostIds(List<Long> postIds);
}
