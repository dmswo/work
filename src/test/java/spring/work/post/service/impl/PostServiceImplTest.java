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
import spring.work.post.dto.request.CreatePost;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.request.UpdatePost;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.entity.Post;
import spring.work.post.repository.PostRepository;
import spring.work.user.entity.Users;
import spring.work.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private PostServiceImpl postService;

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
        then(postRepository).should(times(1)).save(captor.capture());
        Post savedPost = captor.getValue();

        assertThat(savedPost.getTitle()).isEqualTo("title");
        assertThat(savedPost.getContent()).isEqualTo("content");
        assertThat(savedPost.getUser().getUserId()).isEqualTo("dmswo");
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

        then(postRepository)
                .should(times(1))
                .delete(captor.capture());

        Post deletedPost = captor.getValue();

        assertThat(deletedPost.getSeq()).isEqualTo(1L);
        assertThat(deletedPost.getTitle()).isEqualTo("title");
        assertThat(deletedPost.getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("게시물 리스트 조회")
    void getPosts() {
        // Given
        PostSearchCondition condition = new PostSearchCondition();
        condition.setTitle("title");
        condition.setStartDate(LocalDate.of(2026, 1, 1));
        condition.setEndDate(LocalDate.of(2026, 12, 31));

        Pageable pageable = PageRequest.of(0, 10);

        List<PostListResponse> content = List.of(PostListResponse.builder()
                .title("title")
                .content("content")
                .build());

        Page<PostListResponse> page = new PageImpl<>(content, pageable, 1);

        given(postRepository.postList(condition, pageable)).willReturn(page);

        // When
        Page<PostListResponse> result = postRepository.postList(condition, pageable);

        // Then
        then(postRepository).should(times(1)).postList(condition, pageable);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("title");
        assertThat(result.getContent().get(0).getContent()).isEqualTo("content");
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}