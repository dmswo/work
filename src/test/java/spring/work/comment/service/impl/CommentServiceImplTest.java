package spring.work.comment.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.dto.response.CommentListResponse;
import spring.work.comment.entity.Comment;
import spring.work.comment.repository.CommentRepository;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.PageResponse;
import spring.work.global.exception.BusinessException;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PostRepository postRepository;
    @Mock private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("댓글 저장시 존재하지 않는 사용자이면 예외가 발생한다")
    void throw_exception_when_comment_save_user_not_found() {
        // Given
        String userId = "dmswo";
        CreateComment post = CreateComment.builder()
                .postSeq(1L)
                .content("댓글내용")
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.saveComment(post, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("댓글 저장시 존재하지 않는 게시물이면 예외가 발생한다")
    void throw_exception_when_comment_save_post_not_found() {
        // Given
        String userId = "dmswo";
        CreateComment post = CreateComment.builder()
                .postSeq(1L)
                .content("댓글내용")
                .build();
        Users user = Users.builder()
                .userId(userId)
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(postRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.saveComment(post, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_NOT_FOUND.getMessage());
        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("댓글 저장 성공")
    void comment_save_success() {
        // Given
        CreateComment comment = CreateComment.builder()
                .postSeq(1L)
                .content("content1")
                .build();
        String userId = "dmswo";

        Users users = Users.builder()
                .userId(userId)
                .build();

        Post post = Post.builder()
                .seq(1L)
                .title("title")
                .content("content")
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(users));
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // When
        commentService.saveComment(comment, userId);

        // Then
        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        then(commentRepository).should().save(captor.capture());
        Comment savedComment = captor.getValue();

        assertThat(savedComment.getContent()).isEqualTo("content1");
        assertThat(savedComment.getUser().getUserId()).isEqualTo(userId);
        assertThat(savedComment.getPost().getSeq()).isEqualTo(post.getSeq());
    }

    @Test
    @DisplayName("댓글 수정시 존재하지 않는 댓글이면 예외가 발생한다")
    void throw_exception_when_comment_update_comment_not_found() {
        // Given
        Long commentId = 1L;
        UpdateComment comment = UpdateComment.builder()
                .content("수정댓글")
                .build();
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.updateComment(commentId, comment))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void comment_update_success() {
        // Given
        UpdateComment updateComment = UpdateComment.builder()
                .content("content1")
                .build();

        Comment comment = Comment.builder()
                .seq(1L)
                .content("content").build();

        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));

        // When
        commentService.updateComment(1L, updateComment);

        // Then
        assertThat(comment.getContent()).isEqualTo("content1");
    }

    @Test
    @DisplayName("댓글 삭제시 존재하지 않는 댓글이면 예외가 발생한다")
    void throw_exception_when_comment_delete_comment_not_found() {
        // Given
        Long commentId = 1L;
        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.deleteComment(commentId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void comment_delete_success() {
        // Given
        Long commentId = 1L;
        Comment comment = Comment.builder()
                .seq(commentId)
                .content("content").build();

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When
        commentService.deleteComment(commentId);

        // Then
        then(commentRepository).should().findById(commentId);
        then(commentRepository).should().delete(comment);
    }

    @Test
    @DisplayName("댓글 리스트 조회 성공")
    void getComments_success() {
        // Given
        Long postId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Users user = Users.builder()
                .userId("dmswo106")
                .nickname("가오리")
                .build();
        Comment content = Comment.builder()
                .content("댓글내용")
                .user(user)
                .build();

        Page<Comment> page = new PageImpl<>(List.of(content), pageable, 1);

        given(commentRepository.findByPost_Seq(postId, pageable)).willReturn(page);

        // When
        PageResponse<CommentListResponse> result = commentService.getComments(postId, pageable);

        // Then
        CommentListResponse commentListResponse = result.getContent().get(0);
        then(commentRepository).should().findByPost_Seq(postId, pageable);
        assertThat(commentListResponse.getContent()).isEqualTo(content.getContent());
        assertThat(commentListResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}