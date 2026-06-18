package spring.work.postlike.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.work.event.common.EventType;
import spring.work.event.common.OutBoxStatus;
import spring.work.event.producer.outbox.entity.OutboxEvent;
import spring.work.event.producer.outbox.repository.OutBoxEventRepository;
import spring.work.event.producer.outbox.service.OutBoxEventService;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.kafka.producer.EventProducer;
import spring.work.global.redis.PostLikeRedisRepository;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.postlike.entity.PostLike;
import spring.work.postlike.repository.PostLikeRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private PostLikeRepository postLikeRepository;
    @Mock private PostLikeRedisRepository postLikeRedisRepository;
    @Mock private EventProducer eventProducer;
    @Mock private OutBoxEventService outBoxEventService;
    @Mock private OutBoxEventRepository outBoxEventRepository;

    @InjectMocks
    private PostLikeServiceImpl postLikeService;

    @Test
    @DisplayName("이미 좋아요를 누른 게시글이면 예외가 발생한다")
    void throw_exception_when_post_like_already_exists() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        given(postLikeRedisRepository.addLikeUser(postId, userId)).willReturn(false);

        // When & Then
        assertThatThrownBy(() -> postLikeService.savePostLike(postId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.ALREADY_POST_LIKED.getMessage());
        then(postRepository).shouldHaveNoInteractions();
        then(userRepository).shouldHaveNoInteractions();
        then(postLikeRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("좋아요 누를시 존재하지 않는 게시글이면 예외가 발생한다")
    void throw_exception_when_save_post_like_post_not_found() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        given(postLikeRedisRepository.addLikeUser(postId, userId)).willReturn(true);
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postLikeService.savePostLike(postId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_NOT_FOUND.getMessage());
        then(userRepository).shouldHaveNoInteractions();
        then(postLikeRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("좋아요 누를시 존재하지 않는 사용자면 예외가 발생한다")
    void throw_exception_when_save_post_like_user_not_found() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        Post post = Post.builder()
                .seq(postId)
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        given(postLikeRedisRepository.addLikeUser(postId, userId)).willReturn(true);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUserId(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postLikeService.savePostLike(postId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
        then(postLikeRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("좋아요 저장 성공")
    void savePostLike_success() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        Users user = Users.builder()
                .userId(userId)
                .nickname("가오리")
                .build();
        Post post = Post.builder()
                .seq(postId)
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user)
                .build();

        given(postLikeRedisRepository.addLikeUser(postId, userId)).willReturn(true);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(outBoxEventService.createOutbox(any(), any())).willReturn(createOutboxEvent());

        // When
        postLikeService.savePostLike(postId, userId);

        // Then
        ArgumentCaptor<PostLike> captor = ArgumentCaptor.forClass(PostLike.class);
        then(postLikeRepository).should().save(captor.capture());
        PostLike savedPostLike = captor.getValue();

        assertThat(savedPostLike.getPost()).isEqualTo(post);
        assertThat(savedPostLike.getUser()).isEqualTo(user);
        then(postLikeRedisRepository).should(never()).removeLikeUser(anyLong(), anyString());
        then(outBoxEventRepository).should().save(any(OutboxEvent.class));
    }

    @Test
    @DisplayName("저장 실패 시 Redis 롤백")
    void rollback_redis_when_save_post_like_fail() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        Users user = Users.builder()
                .userId(userId)
                .nickname("가오리")
                .build();
        Post post = Post.builder()
                .seq(postId)
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user)
                .build();

        given(postLikeRedisRepository.addLikeUser(postId, userId)).willReturn(true);
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        willThrow(new RuntimeException("DB 저장 실패")).given(postLikeRepository).save(any(PostLike.class));

        // When & Then
        assertThatThrownBy(() -> postLikeService.savePostLike(postId, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB 저장 실패");
        then(postLikeRedisRepository).should().removeLikeUser(postId, userId);
    }

    private OutboxEvent createOutboxEvent() {
        return OutboxEvent.builder()
                .eventType(EventType.NOTIFICATION)
                .payload("{}")
                .status(OutBoxStatus.PENDING)
                .build();
    }

    @Test
    @DisplayName("좋아요 취소시 존재하지 않는 게시글이면 예외가 발생한다")
    void throw_exception_when_delete_post_like_post_not_found() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postLikeService.deletePostLike(postId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_NOT_FOUND.getMessage());
        then(userRepository).shouldHaveNoInteractions();
        then(postLikeRepository).shouldHaveNoInteractions();
        then(postLikeRedisRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("좋아요 취소시 존재하지 않는 사용자면 예외가 발생한다")
    void throw_exception_when_delete_post_like_user_not_found() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        Post post = Post.builder()
                .seq(postId)
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUserId(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postLikeService.deletePostLike(postId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
        then(postLikeRepository).shouldHaveNoInteractions();
        then(postLikeRedisRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("좋아요 취소시 존재하지 않는 좋아요면 예외가 발생한다")
    void throw_exception_when_delete_post_like_postLike_not_found() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        Users user = Users.builder()
                .userId(userId)
                .nickname("가오리")
                .build();
        Post post = Post.builder()
                .seq(postId)
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user)
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(postLikeRepository.findByPostAndUser(post, user)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postLikeService.deletePostLike(postId, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_LIKE_NOT_FOUND.getMessage());
        then(postLikeRedisRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("좋아요 취소 성공")
    void deletePostLike_success() {
        // Given
        Long postId = 1L;
        String userId = "dmswo";
        Users user = Users.builder()
                .userId(userId)
                .nickname("가오리")
                .build();
        Post post = Post.builder()
                .seq(postId)
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user)
                .build();
        PostLike postLike = PostLike.builder()
                .user(user)
                .post(post)
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(userRepository.findByUserId(userId)).willReturn(Optional.of(user));
        given(postLikeRepository.findByPostAndUser(post, user)).willReturn(Optional.of(postLike));

        // When
        postLikeService.deletePostLike(postId, userId);

        // Then
        then(postLikeRepository).should().delete(postLike);
        then(postLikeRedisRepository).should().removeLikeUser(postId, userId);
    }
}