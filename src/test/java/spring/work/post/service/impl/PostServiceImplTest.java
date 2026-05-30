package spring.work.post.service.impl;

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
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.PageResponse;
import spring.work.global.exception.BusinessException;
import spring.work.global.redis.PostLikeRedisRepository;
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.dto.response.PostResponse;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private PostLikeRedisRepository postLikeRedisRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    @DisplayName("게시물 저장시 존재하지 않는 사용자이면 예외가 발생한다")
    void throw_exception_when_post_save_user_not_found() {
        // Given
        CreatePost post = CreatePost.builder()
                .title("title")
                .content("content")
                .build();
        String userId = "dmswo";

        given(userRepository.findByUserId(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.savePost(post, userId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_NOT_FOUND.getMessage());
        then(postRepository).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("게시물 저장 성공")
    void post_save_success() {
        // Given
        CreatePost post = CreatePost.builder()
                .title("title")
                .content("content")
                .build();
        String userId = "dmswo";

        Users users = Users.builder()
                .userId(userId)
                .build();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(users));

        // When
        postService.savePost(post, userId);

        // Then
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        then(postRepository).should().save(captor.capture());
        Post savedPost = captor.getValue();

        assertThat(savedPost.getTitle()).isEqualTo("title");
        assertThat(savedPost.getContent()).isEqualTo("content");
        assertThat(savedPost.getUser().getUserId()).isEqualTo("dmswo");
    }

    @Test
    @DisplayName("게시물 수정시 존재하지 않는 게시물이면 예외가 발생한다")
    void throw_exception_when_post_update_post_not_found() {
        // Given
        UpdatePost post = UpdatePost.builder()
                .title("title")
                .content("content")
                .build();
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.updatePost(postId, post))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시물 수정 성공")
    void post_update_success() {
        // Given
        UpdatePost updatePost = UpdatePost.builder()
                .title("title2")
                .content("content2")
                .build();
        Post post = Post.builder()
                .seq(1L)
                .title("title")
                .content("content")
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // When
        postService.updatePost(1L, updatePost);

        // Then
        assertThat(post.getTitle()).isEqualTo("title2");
        assertThat(post.getContent()).isEqualTo("content2");
    }

    @Test
    @DisplayName("게시물 삭제시 존재하지 않는 게시물이면 예외가 발생한다")
    void throw_exception_when_post_delete_post_not_found() {
        // Given
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.deletePost(postId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시물 삭제 성공")
    void post_delete_success() {
        // Given
        Post post = Post.builder()
                .seq(1L)
                .title("title")
                .content("content")
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        // When
        postService.deletePost(1L);

        // Then
        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        then(postRepository).should().delete(captor.capture());
        Post deletedPost = captor.getValue();

        assertThat(deletedPost.getSeq()).isEqualTo(1L);
        assertThat(deletedPost.getTitle()).isEqualTo("title");
        assertThat(deletedPost.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("게시물 리스트 조회 성공")
    void getPosts_success() {
        // Given
        PostSearchCondition condition = new PostSearchCondition();
        condition.setTitle("title");
        condition.setStartDate(LocalDate.of(2026, 1, 1));
        condition.setEndDate(LocalDate.of(2026, 12, 31));

        Pageable pageable = PageRequest.of(0, 10);

        List<PostListResponse> content = List.of(PostListResponse.builder()
                .seq(1L)
                .title("title")
                .content("content")
                .build());

        String userId = "dmswo";
        Page<PostListResponse> page = new PageImpl<>(content, pageable, 1);

        given(postRepository.postList(condition, pageable)).willReturn(page);
        given(postLikeRedisRepository.getLiked(1L, userId)).willReturn(true);
        given(postLikeRedisRepository.getLikeUserCount(1L)).willReturn(10L);

        // When
        PageResponse<PostListResponse> result = postService.getPosts(condition, pageable, userId);

        // Then
        PostListResponse post = result.getContent().get(0);
        then(postRepository).should().postList(condition, pageable);
        assertThat(post.getTitle()).isEqualTo("title");
        assertThat(post.getContent()).isEqualTo("content");
        assertThat(post.isLiked()).isTrue();
        assertThat(post.getLikeCount()).isEqualTo(10L);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시물 단건 조회시 존재하지 않는 게시물이면 예외가 발생한다")
    void throw_exception_when_getPost_post_not_found() {
        // Given
        Long postId = 1L;
        given(postRepository.findPostDetail(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.getPost(postId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("게시물 단건 조회 성공")
    void getPost_success() {
        // Given
        Long postId = 1L;
        Post post = Post.builder()
                .seq(postId)
                .title("제목")
                .content("재미있는내용")
                .build();
        given(postRepository.findPostDetail(postId)).willReturn(Optional.of(post));

        // When
        PostResponse result = postService.getPost(postId);

        // Then
        assertThat(result.getSeq()).isEqualTo(post.getSeq());
        assertThat(result.getTitle()).isEqualTo(post.getTitle());
        assertThat(result.getContent()).isEqualTo(post.getContent());
    }
}