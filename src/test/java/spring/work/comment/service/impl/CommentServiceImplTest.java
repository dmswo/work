package spring.work.comment.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.work.comment.dto.request.CreateComment;
import spring.work.comment.dto.request.UpdateComment;
import spring.work.comment.entity.Comment;
import spring.work.comment.repository.CommentRepository;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PostRepository postRepository;
    @Mock private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

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
        then(commentRepository).should(times(1)).save(captor.capture());
        Comment savedComment = captor.getValue();

        assertThat(savedComment.getContent()).isEqualTo("content1");
        assertThat(savedComment.getUser().getUserId()).isEqualTo("dmswo");
        assertThat(savedComment.getPost().getSeq()).isEqualTo(1L);
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
        then(commentRepository).should(times(1)).findById(commentId);
        then(commentRepository).should(times(1)).delete(comment);
    }
}