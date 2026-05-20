package spring.work.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.entity.Comment;
import spring.work.comment.repository.CommentRepository;
import spring.work.comment.service.CommentService;
import spring.work.global.constant.ExceptionCode;
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
    public void saveComment(CreateComment request, String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        Post post = postRepository.findById(request.getPostSeq())
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
}
