package spring.work.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.dto.response.CommentListResponse;
import spring.work.comment.entity.Comment;
import spring.work.comment.repository.CommentRepository;
import spring.work.comment.service.CommentService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.PageResponse;
import spring.work.global.exception.BusinessException;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public void saveComment(CreateComment request, Long postId, String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .build();
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void updateComment(Long commentId, UpdateComment request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        comment.modify(request);
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<CommentListResponse> getComments(Long postId, Pageable pageable) {
        Page<CommentListResponse> response = commentRepository.commentList(postId, pageable);
        return PageResponse.from(response);
    }

    @Transactional
    @Override
    public void saveReply(CreateComment request, Long postId, Long commentId, String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.POST_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.COMMENT_NOT_FOUND));

        Comment savedComment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .parent(comment)
                .build();
        commentRepository.save(savedComment);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<CommentListResponse> getReplies(Long commentId, Pageable pageable) {
        Page<CommentListResponse> response = commentRepository.commentReplyList(commentId, pageable);
        return PageResponse.from(response);
    }
}
