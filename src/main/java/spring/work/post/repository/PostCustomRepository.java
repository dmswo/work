package spring.work.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.work.post.dto.request.PostSearchCondition;
import spring.work.post.dto.response.PostListResponse;
import spring.work.post.entity.Post;

import java.util.Optional;

public interface PostCustomRepository {
    Page<PostListResponse> postList(PostSearchCondition condition, Pageable pageable);
    Optional<Post> findPostDetail(Long postId);
}
